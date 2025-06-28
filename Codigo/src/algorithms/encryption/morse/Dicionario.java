package algorithms.encryption.morse;

import java.util.HashMap;
import java.util.Map;

/**
 * Classe que implementa o dicionário para conversão entre números e código Morse.
 */
public class Dicionario {
    
    private Map<Integer, String> dicionario;
    private Map<String, Integer> dicionarioReverso;
    private final String a0 = "-----";
    private final String a1 = ".----";
    private final String a2 = "..---";
    private final String a3 = "...--";
    private final String a4 = "....-";
    private final String a5 = ".....";
    private final String a6 = "-....";
    private final String a7 = "--...";
    private final String a8 = "---..";
    private final String a9 = "----.";

    /*
     * Construtor que mapeia o dicionário com os códigos Morse correspondentes aos dígitos de 0 a 9.
     */
    public Dicionario(){

        dicionario = new HashMap<>();
        dicionarioReverso = new HashMap<>();

        dicionario.put(0, a0);
        dicionarioReverso.put(a0, 0);
        dicionario.put(1, a1);
        dicionarioReverso.put(a1, 1);
        dicionario.put(2, a2);
        dicionarioReverso.put(a2, 2);
        dicionario.put(3, a3);
        dicionarioReverso.put(a3, 3);
        dicionario.put(4, a4);
        dicionarioReverso.put(a4, 4);
        dicionario.put(5, a5);
        dicionarioReverso.put(a5, 5);
        dicionario.put(6, a6);
        dicionarioReverso.put(a6, 6);
        dicionario.put(7, a7);
        dicionarioReverso.put(a7, 7);
        dicionario.put(8, a8);
        dicionarioReverso.put(a8, 8);
        dicionario.put(9, a9);
        dicionarioReverso.put(a9, 9);

    }

    public String procurar(int i){

        return dicionario.get(i);

    }

    public int procurarReverso(String i){

        return dicionarioReverso.get(i);

    }

}
