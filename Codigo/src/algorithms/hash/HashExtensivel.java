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
    private static final int BUCKET_SIZE = 5500;

    public HashExtensivel() throws IOException {
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
        
        // Todos os ponteiros apontam para a posição 0
        for (int i = 0; i < diretorio.enderecosBuckets.length; i++) {
            diretorio.enderecosBuckets[i] = -1;
        }
        
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
        bucketsFile.seek(pos);
        int tamanho = bucketsFile.readInt();
        byte[] bucketBytes = new byte[tamanho];
        bucketsFile.readFully(bucketBytes);
        
        Bucket bucket = new Bucket(BUCKET_SIZE);
        bucket.fromByteArray(bucketBytes);
        return bucket;
    }

    public synchronized void inserir(Registro registro) throws IOException {
        if (registro == null || registro.id < 0) {
            return;
        }
    
        
        // Sempre usa o primeiro endereço do diretório
        long enderecoBucket = diretorio.enderecosBuckets[0];
        Bucket bucket;
        
        if (enderecoBucket == -1) {
            // Primeira inserção - cria novo bucket
            bucket = new Bucket(BUCKET_SIZE);
            enderecoBucket = escreverBucket(bucket);
            diretorio.enderecosBuckets[0] = enderecoBucket;
        } else {
            // Lê o bucket existente
            bucket = lerBucket(enderecoBucket);
        }
        
        bucket.inserir(registro);
        
        bucketsFile.seek(enderecoBucket);
        byte[] bucketBytes = bucket.toByteArray();
        bucketsFile.writeInt(bucketBytes.length);
        bucketsFile.write(bucketBytes);
        
        salvarDiretorio();
    }

    public Registro buscar(int id) throws IOException {
        long endereco = diretorio.enderecosBuckets[0];
        Bucket bucket = lerBucket(endereco);
        
        Registro encontrado = bucket.buscar(id);
        if (encontrado != null) {
        } else {
        }
        return encontrado;
    }

    private void salvarDiretorio() throws IOException {
        dirFile.seek(0);
        dirFile.write(diretorio.toByteArray());
        dirFile.getFD().sync();
    }

    public void close() throws IOException {
        dirFile.close();
        bucketsFile.close();
    }
}