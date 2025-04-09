package algorithms.hash;

import java.io.IOException;
import java.io.RandomAccessFile;

public class Diretorio {

    private final String nomeArquivo = "diretorio.dat";

    public Diretorio(int profundidadeInicial) throws IOException {
        try (RandomAccessFile arq = new RandomAccessFile(nomeArquivo, "rw")) {
            arq.setLength(0);
            arq.writeInt(profundidadeInicial); // profundidade global

            int entradas = 1 << profundidadeInicial;
            for (int i = 0; i < entradas; i++) {
                arq.writeInt(i); // cada posição aponta para um bucket diferente no começo
            }
        }
    }

    public int getProfundidadeGlobal() throws IOException {
        try (RandomAccessFile arq = new RandomAccessFile(nomeArquivo, "r")) {
            return arq.readInt();
        }
    }

    public void setProfundidadeGlobal(int profundidade) throws IOException {
        try (RandomAccessFile arq = new RandomAccessFile(nomeArquivo, "rw")) {
            arq.seek(0);
            arq.writeInt(profundidade);
        }
    }

    public int getPonteiro(int posicao) throws IOException {
        try (RandomAccessFile arq = new RandomAccessFile(nomeArquivo, "r")) {
            arq.seek(4 + posicao * 4);
            return arq.readInt();
        }
    }

    public void setPonteiro(int posicao, int bucketID) throws IOException {
        try (RandomAccessFile arq = new RandomAccessFile(nomeArquivo, "rw")) {
            arq.seek(4 + posicao * 4);
            arq.writeInt(bucketID);
        }
    }

    public int tamanho() throws IOException {
        return 1 << getProfundidadeGlobal();
    }

    public void duplicar() throws IOException {
        int profundidadeAtual = getProfundidadeGlobal();
        int tamanhoAtual = 1 << profundidadeAtual;
        int novoTamanho = tamanhoAtual * 2;

        try (RandomAccessFile arq = new RandomAccessFile(nomeArquivo, "rw")) {
            // Lê todos os ponteiros antigos
            int[] ponteiros = new int[tamanhoAtual];
            arq.seek(4);
            for (int i = 0; i < tamanhoAtual; i++) {
                ponteiros[i] = arq.readInt();
            }

            // Atualiza profundidade
            arq.seek(0);
            arq.writeInt(profundidadeAtual + 1);

            // Escreve ponteiros duplicados
            arq.seek(4);
            for (int i = 0; i < tamanhoAtual; i++) {
                arq.writeInt(ponteiros[i]);
                arq.writeInt(ponteiros[i]); // duplica
            }
        }
    }
}
