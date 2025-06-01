package algorithms.compression.lzw;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;

import repository.CRUDI;

@SuppressWarnings("unused")
public class LZW {
    
    private Dicionario dicionario;
    private String diretorio;

    public LZW(String diretorio){

        dicionario = new Dicionario();
        this.diretorio = diretorio;

    }

    public void comprimir(){

        try{

            char[] texto = CRUDI.lerTudoComoTexto(diretorio).toCharArray();

            int contDeCaracteres = 0;

            RandomAccessFile file = new RandomAccessFile("data/compressed/arquivoComprimidoLZW" + System.currentTimeMillis() + ".db", "rw");

            do{

                boolean achouSequencia = true;
                String sequencia = "" + texto[contDeCaracteres];
                int ultimoIndiceCerto = 0;

                while(achouSequencia){

                    int indice = dicionario.procurarIndice(sequencia);

                    if(indice == -1){
                        
                        dicionario.adicionar(sequencia);
                        file.writeInt(ultimoIndiceCerto);
                        achouSequencia = false;

                    }else{

                        ultimoIndiceCerto = indice;
                        ++contDeCaracteres;

                        if(contDeCaracteres < texto.length){

                            sequencia += texto[contDeCaracteres];
                            achouSequencia = true;

                        }else{

                            achouSequencia = false;

                        }

                    }

                }

            }while(contDeCaracteres < texto.length);

            file.close();

        }catch(Exception e){

            e.printStackTrace();
            
        }

    }

    public void descomprimir(){

        try{
            
            RandomAccessFile arquivoOrigem = new RandomAccessFile(diretorio, "r");
            RandomAccessFile arquivoDestino = new RandomAccessFile("data/database/arquivoDescomprimidoLZW" + System.currentTimeMillis() + ".db", "rw");
            String texto = "";

            while(arquivoOrigem.getFilePointer() < arquivoOrigem.length()){

                int indice = arquivoOrigem.readInt();
                boolean achouSequencia = true;
                String ultimoCerto = "";

                while(achouSequencia){

                    String sequencia = ""; 
                    sequencia += ultimoCerto;
                    sequencia += dicionario.procurarSequencia(indice);

                    if(sequencia == null){
                        
                        dicionario.adicionar(sequencia);
                        file.write(ultimoIndiceCerto);
                        achouSequencia = false;

                    }else{

                        arquivoDestino.writeBytes(sequencia);
                        ultimoCerto += sequencia;
                        indice = arquivoOrigem.readInt();
                        achouSequencia = true;

                    }

                }

            }

            arquivoDestino.close();
            arquivoOrigem.close();

        }catch(Exception e){

            e.printStackTrace();

        }

    }

}
