package algorithms.compression.lzw;

import java.util.HashMap;
import java.util.Map;

public class Dicionario {

    private Map<String, Integer> dicionarioLZW;
    private Map<Integer, String> dicionarioInvertidoLZW;
    private int cont;

    public Dicionario(){

        dicionarioLZW = new HashMap<>();
        dicionarioInvertidoLZW = new HashMap<>();
        cont = 1;

        inicializarDicionario();

    }

    private void inicializarDicionario(){

        for(int i = 32;i < 127;i++){

            char c = (char) i;
            String str = "" + c;

            dicionarioLZW.put(str, cont);
            dicionarioInvertidoLZW.put(cont, str);

            cont++;

        }

    }

    public void adicionar(String seq){

        dicionarioLZW.put(seq, cont);
        dicionarioInvertidoLZW.put(cont, seq);

        cont++;

    }

    public int procurarIndice(String s){

        return dicionarioLZW.getOrDefault(s, -1);

    }

    public String procurarSequencia(int indice){

        return dicionarioInvertidoLZW.getOrDefault(indice, null);

    }
    
}