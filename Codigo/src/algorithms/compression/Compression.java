package algorithms.compression;

import java.io.File;

import algorithms.compression.huffman.Huffman;
import algorithms.compression.lzw.LZW;

public class Compression {

    private String diretorioDataBase = "data/database";
    private String diretorioComprimidos = "data/compressed";
    private int tipo;

    public Compression(int tipo){

        this.tipo = tipo;

    }

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

    public File[] listarArquivos(boolean comprimirOuDescomprimir){

        File pasta;

        if(comprimirOuDescomprimir){

            pasta = new File(diretorioDataBase);
            File[] arquivos = pasta.listFiles();

            if(arquivos == null || arquivos.length == 0){

                System.out.println("Arquivos não encontrados!");
                return null;

            }

            return arquivos;

        }

        pasta = new File(diretorioComprimidos);
        File[] arquivos = pasta.listFiles();

        return arquivos;

    }

}
