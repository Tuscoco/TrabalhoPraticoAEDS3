package algorithms.hash;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.BufferedReader;
import java.io.FileReader;

import model.Musica;
import model.Registro;
import model.RegistroSort;

public class HashExtensivel {

    private Diretorio diretorio;
    private int capacidadeBucket;

    public HashExtensivel(int capacidadeBucket, int profundidadeInicial) throws IOException {
        this.capacidadeBucket = capacidadeBucket;

        // Inicializa o diretório com a profundidade inicial informada
        try {
            new RandomAccessFile("diretorio.dat", "r").close();
            this.diretorio = new Diretorio(profundidadeInicial); // abre normalmente, já existe
        } catch (IOException e) {
            this.diretorio = new Diretorio(profundidadeInicial); // profundidade inicial informada
            int numBuckets = 1 << profundidadeInicial; // 2^profundidadeInicial
            for (int i = 0; i < numBuckets; i++) {
                Bucket bucket = new Bucket(i);
                bucket.criar(profundidadeInicial); // Cria o bucket com profundidade local igual à global
            }
        }
    }

    private int hash(int chave, int profundidade) {
        return chave & ((1 << profundidade) - 1);
    }

    
    // Conversão de int para byte[]
    private byte[] intToByteArray(int value) {
        return new byte[] {
            (byte) (value >> 24),
            (byte) (value >> 16),
            (byte) (value >> 8),
            (byte) value
        };
    }
    
    // Conversão de long para byte[]
    private byte[] longToByteArray(long value) {
        return new byte[] {
            (byte) (value >> 56),
            (byte) (value >> 48),
            (byte) (value >> 40),
            (byte) (value >> 32),
            (byte) (value >> 24),
            (byte) (value >> 16),
            (byte) (value >> 8),
            (byte) value
        };
    }


    
    public Registro fromByteArray(byte[] byteArray) {
        int id = byteArray[0]; // Exemplo simplificado, ajuste conforme necessário
        long end = byteArray[1]; // Exemplo simplificado, ajuste conforme necessário
        return new Registro(id, end);
    }

    public byte[] toByteArray(Registro registro) {
        byte[] bytes = new byte[12]; // Assumindo 12 bytes, ajuste conforme necessário
        bytes[0] = (byte) registro.id;
        bytes[1] = (byte) (registro.end & 0xFF); // Exemplo simplificado
        return bytes;
    }

    public void carregarCsvNoHash(String caminhoCsv) {
        try (BufferedReader br = new BufferedReader(new FileReader(caminhoCsv))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                String[] dados = linha.split(",");
                int id = Integer.parseInt(dados[0].trim());
                long end = Long.parseLong(dados[1].trim());
                Registro registro = new Registro(id, end);
                inserir(registro); // Você pode precisar criar um método inserir que aceite Registro
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void inserir(Registro registro) throws IOException {
        int profundidadeGlobal = diretorio.getProfundidadeGlobal();
        int chave = registro.id; // Usando o ID como chave
        int posicao = hash(chave, profundidadeGlobal);
        int bucketID = diretorio.getPonteiro(posicao);
        Bucket bucket = new Bucket(bucketID);

        boolean inserido = bucket.adicionar(registro, capacidadeBucket); // Certifique-se de que o método adicionar suporta Registro.

        if (inserido) return;

        // Bucket cheio — dividir
        int profundidadeLocal = bucket.getProfundidadeLocal();

        if (profundidadeLocal == profundidadeGlobal) {
            diretorio.duplicar();
            profundidadeGlobal++;
        }

        int novoBucketID = criarNovoBucket(profundidadeLocal + 1);
        Bucket novoBucket = new Bucket(novoBucketID);

        bucket.setProfundidadeLocal(profundidadeLocal + 1);
        novoBucket.setProfundidadeLocal(profundidadeLocal + 1);

        // Atualiza ponteiros do diretório
        int entradas = 1 << profundidadeGlobal;
        for (int i = 0; i < entradas; i++) {
            int bID = diretorio.getPonteiro(i);
            if (bID == bucketID) {
                int hashLocal = hash(i, profundidadeLocal + 1);
                if ((hashLocal & 1) == 1) {
                    diretorio.setPonteiro(i, novoBucketID);
                }
            }
        }

        // Redistribuição (releitura e regravação)
        reescreverBucket(bucketID, bucket, novoBucketID, novoBucket);

        // Tenta inserir novamente
        inserir(registro);
    }

    private void reescreverBucket(int antigoID, Bucket antigo, int novoID, Bucket novo) throws IOException {
        String tempArquivo = "temp_bucket.dat";

        try (RandomAccessFile original = new RandomAccessFile("bucket_" + antigoID + ".dat", "r");
             RandomAccessFile temp = new RandomAccessFile(tempArquivo, "rw")) {

            int profundidade = original.readInt();
            int quantidade = original.readInt();

            temp.writeInt(profundidade); // o mesmo, pois bucket será regravado
            temp.writeInt(0); // será contado

            int novaQtd = 0;

            while (original.getFilePointer() < original.length()) {
                int tam = original.readInt();
                byte[] dados = new byte[tam];
                original.readFully(dados);

                Musica musica = new Musica(dados);
                int hashLocal = hash(musica.getIndex(), profundidade + 1);

                Registro reg = new Registro(musica.getIndex(), 0);

                if ((hashLocal & 1) == 1) {
                    novo.adicionar(reg, capacidadeBucket); // manda pro novo
                } else {
                    temp.writeInt(tam);
                    temp.write(dados);
                    novaQtd++;
                }
            }

            temp.seek(4);
            temp.writeInt(novaQtd);
        }

        // Substitui o antigo bucket pelo novo temp
        new java.io.File("bucket_" + antigoID + ".dat").delete();
        new java.io.File(tempArquivo).renameTo(new java.io.File("bucket_" + antigoID + ".dat"));
    }

    private int criarNovoBucket(int profundidade) throws IOException {
        int novoID = maiorIDBucket() + 1;
        Bucket novo = new Bucket(novoID);
        novo.criar(profundidade); // Cria o bucket com a nova profundidade local
        return novoID;
    }

    private int maiorIDBucket() throws IOException {
        int max = -1;
        int tamanho = diretorio.tamanho();
        for (int i = 0; i < tamanho; i++) {
            int id = diretorio.getPonteiro(i);
            if (id > max) max = id;
        }
        return max;
    }

    public boolean remover(int chave) throws IOException {
        int profundidadeGlobal = diretorio.getProfundidadeGlobal();
        int posicao = hash(chave, profundidadeGlobal);
        int bucketID = diretorio.getPonteiro(posicao);
        Bucket bucket = new Bucket(bucketID);

        return bucket.remover(chave);
    }

    public void listarTodos() throws IOException {
        int tamanho = diretorio.tamanho();
        boolean[] vistos = new boolean[maiorIDBucket() + 1];

        for (int i = 0; i < tamanho; i++) {
            int id = diretorio.getPonteiro(i);
            if (!vistos[id]) {
                System.out.println("Bucket " + id + ":");
                new Bucket(id).listarRegistros();
                vistos[id] = true;
            }
        }
    }
}

