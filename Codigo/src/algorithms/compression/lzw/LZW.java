package algorithms.compression.lzw;

@SuppressWarnings("unused")
public class LZW {
    
    private Dicionario dicionario;
    private String diretorioDataBase = "data/database/";
    private String diretorioComprimidos = "data/compressed/";

    public LZW(String arquivo, boolean compOuDes){

        dicionario = new Dicionario();

        if(compOuDes){

            this.diretorioDataBase += arquivo;

        }else{

            this.diretorioComprimidos += arquivo;

        }

    }

    public void comprimir(){}

    public void extrair(){}

}
