package model;

import java.io.IOException;

public class Registro {
    
    private Musica musica;
    private int origem;

    public Registro(Musica musica, int origem){

        this.musica = musica;
        this.origem = origem;

    }

    public int getIndex(){

        return this.musica.getIndex();

    }

    public int getOrigem(){

        return this.origem;

    }

    public byte[] toByteArray() throws IOException{

        return this.musica.toByteArray();

    }

}
