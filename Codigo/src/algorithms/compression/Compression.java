package algorithms.compression;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import algorithms.compression.huffman.Huffman;
import algorithms.compression.lzw.LZW;

/*
 * Classe para controlar os algoritmos de compressão
 * Essa classe basicamente tem um atributo "tipo" e quando a compressão e descompressão é chamada
 * o tipo seleciona o algoritmo escolhido
 */
public class Compression {

    private String diretorioDataBase = "data/database";
    private String diretorioComprimidos = "data/compressed";
    private int tipo;

    public Compression(int tipo){

        this.tipo = tipo;

    }

    /*
     * Chama os métodos de compressão do algoritmo escolhido 
     */
    public void comprimir(String arquivo){

        String diretorioArquivoOrigem = diretorioDataBase + "/" + arquivo;

        if(tipo == 1){

            Huffman huffman = new Huffman(diretorioArquivoOrigem);
            huffman.compress();

        }else if(tipo == 2){

            LZW lzw = new LZW(diretorioArquivoOrigem);
            lzw.comprimir();

        }

    }

    /*
     * Chama os métodos de descompressão do algoritmo escolhido
     */
    public void descomprimir(String arquivo){

        String diretorioArquivoOrigem = diretorioComprimidos + "/" + arquivo;

        if(tipo == 1){

            Huffman huffman = new Huffman(diretorioArquivoOrigem);
            huffman.decompress();

        }else if(tipo == 2){

            LZW lzw = new LZW(diretorioArquivoOrigem);
            lzw.descomprimir();

        }

    }

    /*
     * Lista os arquivos de uma pasta
     * -Se for para compressão, mostra todos os arquivos da pasta data/database
     * -Se for para descompressão, mostra apenas os arquivos de acordo com o nome do algoritmo escolhido
     */
    public List<File> listarArquivos(boolean comprimirOuDescomprimir, String tipo){

        File pasta;

        /*
         * Lista todos da pasta database
         */
        if(comprimirOuDescomprimir){

            pasta = new File(diretorioDataBase);
            File[] temp = pasta.listFiles();
            List<File> arquivos = new ArrayList<>();

            for(int i = 0;i < temp.length;i++){

                arquivos.add(temp[i]);

            }

            if(arquivos == null || arquivos.size() == 0){

                System.out.println("Arquivos não encontrados!");
                return null;

            }

            return arquivos;

        }

        /*
         * Lista apenas de acordo com o nome
         */
        pasta = new File(diretorioComprimidos);
        File[] temp = pasta.listFiles();
        List<File> arquivos = new ArrayList<>();

        for(int i = 0;i < temp.length;i++){

            if(temp[i].getName().contains(tipo)){
                
                arquivos.add(temp[i]);

            }

        }

        return arquivos;

    }

}
