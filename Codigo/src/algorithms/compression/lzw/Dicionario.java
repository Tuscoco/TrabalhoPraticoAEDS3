package algorithms.compression.lzw;

import java.util.HashMap;
import java.util.Map;

public class Dicionario {

    private Map<String, Integer> dicionarioLZW;
    private int cont;

    public Dicionario(){

        dicionarioLZW = new HashMap<>();
        cont = 1;

        inicializarDicionario();

    }

    private void inicializarDicionario(){

        for(int i = 32;i < 127;i++){

            char c = (char) i;
            String str = "" + c;

            dicionarioLZW.put(str, cont);

            cont++;

        }

    }

    public void adicionar(String seq){

        dicionarioLZW.put(seq, cont);

        cont++;

    }

    public int procurar(String s){

        return dicionarioLZW.getOrDefault(s, -1);

    }
    
}