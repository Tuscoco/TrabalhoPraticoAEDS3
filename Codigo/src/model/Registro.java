package model;

/*
 * Classe para ser utilizada nos algoritmos de BTree e Hash Extensível
 */
public class Registro{

    public int id;
    public long end;

    public Registro(){

        id = -1;
        end = -1;

    }

    public Registro(int id, long end){

        this.id = id;
        this.end = end;

    }

}
