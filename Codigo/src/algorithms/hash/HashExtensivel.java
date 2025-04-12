package algorithms.hash;

import model.Registro;

import java.io.*;

public class HashExtensivel {
    private static final String ARQUIVO = "data/indexes/Hash.db";
    private RandomAccessFile file;
    private Diretorio diretorio;
    private int bucketSize;

    public HashExtensivel(int bucketSize) {
        this.bucketSize = bucketSize;
        try {
            file = new RandomAccessFile(ARQUIVO, "rw");

            if (file.length() == 0) {
                // Inicializa o diretório e salva o tamanho do bucket
                file.seek(0);
                file.writeInt(bucketSize);

                diretorio = new Diretorio(1);
                Bucket b1 = new Bucket(bucketSize);
                Bucket b2 = new Bucket(bucketSize);

                long end1 = escreverBucket(b1);
                long end2 = escreverBucket(b2);

                diretorio.enderecosBuckets[0] = end1;
                diretorio.enderecosBuckets[1] = end2;

                salvarDiretorio();
            } else {
                file.seek(0);
                this.bucketSize = file.readInt();
                carregarDiretorio();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void salvarDiretorio() throws IOException {
        file.seek(4); // após o int do bucketSize
        byte[] bytes = diretorio.toByteArray();
        file.writeInt(bytes.length);
        file.write(bytes);
    }

    private void carregarDiretorio() throws IOException {
        file.seek(4); // após o int do bucketSize
        int tam = file.readInt();
        byte[] bytes = new byte[tam];
        file.readFully(bytes);

        diretorio = new Diretorio(1); // placeholder
        diretorio.fromByteArray(bytes);
    }

    private long escreverBucket(Bucket b) throws IOException {
        long pos = file.length();
        byte[] bytes = b.toByteArray();
        file.seek(pos);
        file.writeInt(bytes.length);
        file.write(bytes);
        return pos;
    }

    private Bucket lerBucket(long pos) throws IOException {
        file.seek(pos);
        int tam = file.readInt();
        byte[] bytes = new byte[tam];
        file.readFully(bytes);
        Bucket b = new Bucket(bucketSize);
        b.fromByteArray(bytes);
        return b;
    }

    public void inserir(Registro r) {
        try {
            int hash = diretorio.hash(r.id);
            long endereco = diretorio.enderecosBuckets[hash];
            Bucket b = lerBucket(endereco);

            if (!b.inserir(r)) {
                // Bucket cheio: tratar split
                System.out.println("Bucket cheio, split necessário");
                // -> implementar split, duplicação de diretório, etc.
            } else {
                file.seek(endereco);
                byte[] bytes = b.toByteArray();
                file.writeInt(bytes.length);
                file.write(bytes);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Registro buscar(int id) {
        try {
            int hash = diretorio.hash(id);
            long endereco = diretorio.enderecosBuckets[hash];
            Bucket b = lerBucket(endereco);
            return b.buscar(id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
