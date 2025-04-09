package algorithms.hash;

import java.io.*;
import model.RegistroSort;
import model.Musica;

public class Bucket {
    private String nomeArquivo;

    public Bucket(int id) {
        this.nomeArquivo = "bucket_" + id + ".dat";
    }

    public void criar(int profundidadeLocal) throws IOException {
        try (RandomAccessFile arq = new RandomAccessFile(nomeArquivo, "rw")) {
            arq.setLength(0);
            arq.writeInt(profundidadeLocal); // profundidade local
            arq.writeInt(0); // quantidade de registros
        }
    }

    public int getProfundidadeLocal() throws IOException {
        try (RandomAccessFile arq = new RandomAccessFile(nomeArquivo, "r")) {
            arq.seek(0);
            return arq.readInt();
        }
    }

    public void setProfundidadeLocal(int novaProfundidade) throws IOException {
        try (RandomAccessFile arq = new RandomAccessFile(nomeArquivo, "rw")) {
            arq.seek(0);
            arq.writeInt(novaProfundidade);
        }
    }

    public int getQuantidadeRegistros() throws IOException {
        try (RandomAccessFile arq = new RandomAccessFile(nomeArquivo, "r")) {
            arq.seek(4);
            return arq.readInt();
        }
    }

    public boolean adicionar(RegistroSort r, int capacidadeMaxima) throws IOException {
        try (RandomAccessFile arq = new RandomAccessFile(nomeArquivo, "rw")) {
            arq.seek(4);
            int quantidade = arq.readInt();

            if (quantidade >= capacidadeMaxima) {
                return false;
            }

            arq.seek(arq.length()); // fim do arquivo
            byte[] dados = r.toByteArray();
            arq.writeInt(dados.length);
            arq.write(dados);

            // Atualiza quantidade
            arq.seek(4);
            arq.writeInt(quantidade + 1);

            return true;
        }
    }

    public boolean remover(int index) throws IOException {
        File temp = new File(nomeArquivo + ".tmp");
        boolean removido = false;

        try (RandomAccessFile original = new RandomAccessFile(nomeArquivo, "r");
             RandomAccessFile novo = new RandomAccessFile(temp, "rw")) {

            int profundidade = original.readInt();
            int quantidade = original.readInt();

            novo.writeInt(profundidade);
            novo.writeInt(0); // será atualizado

            int novaQuantidade = 0;

            while (original.getFilePointer() < original.length()) {
                int tam = original.readInt();
                byte[] dados = new byte[tam];
                original.readFully(dados);

                Musica m = new Musica(dados);
                if (m.getIndex() == index) {
                    removido = true;
                    continue;
                }

                novo.writeInt(tam);
                novo.write(dados);
                novaQuantidade++;
            }

            novo.seek(4);
            novo.writeInt(novaQuantidade);
        }

        if (removido) {
            new File(nomeArquivo).delete();
            temp.renameTo(new File(nomeArquivo));
        } else {
            temp.delete();
        }

        return removido;
    }

    public void listarRegistros() throws IOException {
        try (RandomAccessFile arq = new RandomAccessFile(nomeArquivo, "r")) {
            arq.seek(8); // após cabecalho
            while (arq.getFilePointer() < arq.length()) {
                int tam = arq.readInt();
                byte[] dados = new byte[tam];
                arq.readFully(dados);

                Musica m = new Musica(dados);
                System.out.println("Index: " + m.getIndex());
            }
        }
    }
}
