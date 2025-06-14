package algorithms.encryption;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import algorithms.encryption.morse.Morse;

public class Encryption {

    private int tipo;
    private String diretorioDataBase = "data/database/";
    private String diretorioCriptografados = "data/encrypted/";

    public Encryption(int tipo){

        this.tipo = tipo;

    }

    public void criptografar(String arquivo){

        String diretorioArquivoOrigem = diretorioDataBase + arquivo;

        if(tipo == 1){

            //RSA

        }else if(tipo == 2){

            Morse morse = new Morse(diretorioArquivoOrigem);

            morse.criptografar();

        }

    }

    public void descriptografar(String arquivo){

        String diretorioArquivoOrigem = diretorioCriptografados + arquivo;

        if(tipo == 1){

            //RSA

        }else if(tipo == 2){

            Morse morse = new Morse(diretorioArquivoOrigem);

            morse.descriptografar();

        }

    }

    public List<File> listarArquivos(boolean criptografarOuDescriptografar){

        File pasta;

        /*
         * Lista todos da pasta database
         */
        if(criptografarOuDescriptografar){

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
        pasta = new File(diretorioCriptografados);
        File[] temp = pasta.listFiles();
        List<File> arquivos = new ArrayList<>();
        String tipoC = "";

        if(tipo == 1){

            tipoC = "RSA";

        }else{

            tipoC = "MORSE";

        }

        for(int i = 0;i < temp.length;i++){

            if(temp[i].getName().contains(tipoC)){
                
                arquivos.add(temp[i]);

            }

        }

        return arquivos;

    }
    
}