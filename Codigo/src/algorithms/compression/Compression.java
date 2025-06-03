package algorithms.compression;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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

    public List<File> listarArquivos(boolean comprimirOuDescomprimir, String tipo){

        File pasta;

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
