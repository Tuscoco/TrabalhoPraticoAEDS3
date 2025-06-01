package algorithms.compression.lzw;

import java.io.FileInputStream;
import java.io.InputStreamReader;

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

            FileInputStream fis = new FileInputStream(diretorio);
            InputStreamReader isr = new InputStreamReader(fis, "UTF-8");

        }catch(Exception e){

            e.printStackTrace();
            
        }

    }

    public void extrair(){}

}
