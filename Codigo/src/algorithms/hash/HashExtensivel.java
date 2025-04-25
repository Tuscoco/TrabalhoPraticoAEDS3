package algorithms.hash;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import model.Registro;

public class HashExtensivel {
    private static final String ARQUIVO_DIRETORIO = "data/indexes/HashDirectory.db";
    private static final String ARQUIVO_BUCKETS = "data/indexes/HashBuckets.db";
    private RandomAccessFile dirFile;
    private RandomAccessFile bucketsFile;
    private Diretorio diretorio;
    private int bucketSize;

    public HashExtensivel(int bucketSize) throws IOException {
        this.bucketSize = bucketSize;
        
        new File("data/indexes").mkdirs();
        
        dirFile = new RandomAccessFile(ARQUIVO_DIRETORIO, "rw");
        bucketsFile = new RandomAccessFile(ARQUIVO_BUCKETS, "rw");

        if (dirFile.length() == 0) {
            inicializarNovoHash();
        } else {
            carregarEstruturaExistente();
        }
    }

    private void inicializarNovoHash() throws IOException {
        diretorio = new Diretorio(1);
        
        Bucket b1 = new Bucket(bucketSize);
        Bucket b2 = new Bucket(bucketSize);
        b1.inserir(new Registro(-1, -1));
        b2.inserir(new Registro(-1, -1));
        long pos1 = escreverBucket(b1);
        long pos2 = escreverBucket(b2);
        
        diretorio.enderecosBuckets[0] = pos1;
        diretorio.enderecosBuckets[1] = pos2;
        
        salvarDiretorio();
    }

    private void carregarEstruturaExistente() throws IOException {
        
        byte[] dirBytes = new byte[(int) dirFile.length()];
        dirFile.seek(0);
        dirFile.readFully(dirBytes);
        
        diretorio = new Diretorio(1); 
        diretorio.fromByteArray(dirBytes);
    }

    private long escreverBucket(Bucket bucket) throws IOException {
        long pos = bucketsFile.length();
        bucketsFile.seek(pos);
        
        byte[] bucketBytes = bucket.toByteArray();
        bucketsFile.writeInt(bucketBytes.length); 
        bucketsFile.write(bucketBytes);          
        
        return pos;
    }

    private Bucket lerBucket(long pos) throws IOException {
        if (pos <= 0 || pos >= bucketsFile.length()) {
            throw new IOException("Posição inválida para bucket: " + pos);
        }
        
        bucketsFile.seek(pos);
        int tamanho = bucketsFile.readInt();
        byte[] bucketBytes = new byte[tamanho];
        bucketsFile.readFully(bucketBytes);
        
        Bucket bucket = new Bucket(bucketSize);
        bucket.fromByteArray(bucketBytes);
        return bucket;
    }

    public synchronized void inserir(Registro registro) throws IOException {
        if (registro == null || registro.id < 0) {
            return;
        }

        int hash = diretorio.hash(registro.id);
        long enderecoBucket = diretorio.enderecosBuckets[hash];
        if (enderecoBucket <= 0) {
            // Cria novo bucket se o endereço for inválido
            Bucket novoBucket = new Bucket(bucketSize);
            novoBucket.inserir(registro);
            long novoEndereco = escreverBucket(novoBucket);
            diretorio.enderecosBuckets[hash] = novoEndereco;
            salvarDiretorio();
            return;
        }
        Bucket bucket = lerBucket(enderecoBucket);
        
        if (bucket.inserir(registro)) {
            // Atualiza o bucket no arquivo
            escreverBucket(bucket);
        } else {
            // Bucket cheio - precisa dividir
            dividirBucket(bucket, enderecoBucket, registro);
        }
    }

    private void dividirBucket(Bucket bucketAntigo, long enderecoAntigo, Registro novoRegistro) 
            throws IOException {
        // Remove o registro vazio de inicialização se existir
        bucketAntigo.registros.removeIf(r -> r.id == -1);
        
        bucketAntigo.profundidadeLocal++;
        
        if (bucketAntigo.profundidadeLocal > diretorio.profundidadeGlobal) {
            expandirDiretorio();
        }
        
        Bucket novoBucket = new Bucket(bucketSize);
        novoBucket.profundidadeLocal = bucketAntigo.profundidadeLocal;
        
        List<Registro> registrosParaRedistribuir = new ArrayList<>(bucketAntigo.registros);
        registrosParaRedistribuir.add(novoRegistro);
        bucketAntigo.registros.clear();
        
        int mascara = 1 << (bucketAntigo.profundidadeLocal - 1);
        
        for (Registro r : registrosParaRedistribuir) {
            if ((diretorio.hashComBits(r.id, bucketAntigo.profundidadeLocal) & mascara) == 0) {
                bucketAntigo.registros.add(r);
            } else {
                novoBucket.registros.add(r);
            }
        }
        
        // Escreve os buckets atualizados
        long novoEndereco = escreverBucket(novoBucket);
        escreverBucket(bucketAntigo);
        
        // Atualiza o diretório
        atualizarDiretorio(bucketAntigo, enderecoAntigo, novoEndereco);
    }

    private void expandirDiretorio() throws IOException {
        diretorio.duplicar();
        salvarDiretorio();
    }

    private void atualizarDiretorio(Bucket bucket, long enderecoOriginal, long enderecoNovo) 
            throws IOException {
        int bitsIgnorados = diretorio.profundidadeGlobal - bucket.profundidadeLocal;
        int passo = 1 << bitsIgnorados;
        
        for (int i = 0; i < diretorio.enderecosBuckets.length; i++) {
            if (diretorio.enderecosBuckets[i] == enderecoOriginal) {
                if (((i >> bitsIgnorados) & 1) == 1) {
                    diretorio.enderecosBuckets[i] = enderecoNovo;
                }
            }
        }
        
        salvarDiretorio();
    }

    public Registro buscar(int id) throws IOException {
        int hash = diretorio.hash(id);
        long endereco = diretorio.enderecosBuckets[hash];
        
        Bucket bucket = lerBucket(endereco);
        return bucket.buscar(id);
    }

    private void salvarDiretorio() throws IOException {
        dirFile.seek(0);
        dirFile.write(diretorio.toByteArray());
        dirFile.getFD().sync();
    }

    public void debugPrint() throws IOException {
        System.out.println("\n=== DEBUG HASH EXTENSÍVEL ===");
        System.out.println("Profundidade Global: " + diretorio.profundidadeGlobal);
        System.out.println("Número de Entradas: " + diretorio.enderecosBuckets.length);
        
        for (int i = 0; i < diretorio.enderecosBuckets.length; i++) {
            long endereco = diretorio.enderecosBuckets[i];
            System.out.printf("\n[%02d] Endereço: %d | ", i, endereco);
            
            if (endereco > 0) {
                Bucket b = lerBucket(endereco);
                System.out.printf("Profundidade Local: %d | Registros: %d", 
                        b.profundidadeLocal, b.registros.size());
                
                for (Registro r : b.registros) {
                    System.out.printf("\n   ID: %d, Endereço: %d", r.id, r.end);
                }
            }
        }
        System.out.println("\n=============================\n");
    }

    public void close() throws IOException {
        dirFile.close();
        bucketsFile.close();
    }
}