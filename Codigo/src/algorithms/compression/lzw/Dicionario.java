package algorithms.compression.lzw;

public class Dicionario {

    private String[] unitarios;
    private int contU;
    private String[] sequencias;
    private int contS;

    public Dicionario(){

        unitarios = new String[95];
        contU = 0;
        sequencias = new String[4001];
        contS = 0;

        inicializarDicionario();

    }

    private void inicializarDicionario(){

        for(int i = 32;i < 127;i++){

            char c = (char) i;

            unitarios[contU] = "";
            unitarios[contU] += c;

            contU++;

        }

    }

    public void adicionarSequencia(String seq){

        if(contS >= 4001){

            return;

        }

        sequencias[contS] = seq;

    }

    public int procurar(String s){

        if(s.length() == 1){

            return procurarUnitario(s);

        }

        return procurarSequencia(s);

    }

    private int procurarUnitario(String c){

        for(int i = 0;i < contU;i++){

            if(unitarios[i].equals(c)){

                return i + 1;

            }

        }

        return -1;

    }

    private int procurarSequencia(String seq){

        for(int i = 0;i < contS;i++){

            if(sequencias[i].equals(seq)){

                return i + 1;

            }

        }

        return -1;

    }
    
}