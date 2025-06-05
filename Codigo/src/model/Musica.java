package model;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/*
 * Classe que guarda um registro do arquivo
 * Organizado em: id, nome, artista, data, tamanho e artistasRelacionados
 */
public class Musica {
    
    private int index;
    private String name; //Tamanho variável
    private String artist; //Tamanho fixo
    private long date;
    private double length;
    private String[] fArtists;

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); //reconhece o formato que a data será lida e exibida ao usuário para utilizar nas transformações para Timestamp

    public Musica(){}


    public Musica(int index, String name, String artist, long date, double length, String[] fArtists){

        this.index = index;
        this.name = name;
        this.artist = tratarTamanho(artist, 15);
        this.date = date;
        this.length = length;
        this.fArtists = fArtists;

    }

    public Musica(String name, String artist, long date, double length, String[] fArtists){

        this.index = -1;
        this.name = name;
        this.artist = tratarTamanho(artist, 15);
        this.date = date;
        this.length = length;
        this.fArtists = fArtists;

    }

    public int getIndex(){

        return index;

    }

    public void setIndex(int index){

        this.index = index;

    }

    public String getArtist(){

        return artist;

    }

    @Override
    public String toString(){

        return "|Index: " + index + "||Name: " + name + "||Artist: " + artist + "||Date: " + dateFormat.format(new Date(date)) + "||Length: " + length + "||Featured_Artists: " + String.join(", ", fArtists) + "|\n";

    }

    /*
     * Método que limita o tamanho da string
     * 
     * -Recebe uma String e um tamanho
     * -Se a String for menor, ela é formatada com espaços até completar o tamanho máximo
     * -Se a String for maior, ela é "cortada"
     * -Se a String for igual, ela é mantida
     * -Retorna a String
     */
    public String tratarTamanho(String str, int tamanho){

        if(str.length() > tamanho){

            return str.substring(0, tamanho);

        }

        return String.format("%-" + tamanho + "s", str);

    }

    /*
     * Método que "transforma" o objeto em um array de bytes
     */
    public byte[] toByteArray() throws IOException{

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        dos.writeInt(index);
        dos.writeUTF(name);
        dos.writeUTF(artist);
        dos.writeLong(date);
        dos.writeDouble(length);
        dos.writeInt(fArtists.length);

        for(String artist : fArtists){
            
            byte[] artistBytes = artist.getBytes("UTF-8");
            dos.writeInt(artistBytes.length); 

            dos.write(artistBytes);
        }

        return baos.toByteArray();

    }

    /*
     * Método que "transforma" um array de bytes em um objeto
     */
    public void fromByteArray(byte[] array) throws IOException{

        ByteArrayInputStream bais = new ByteArrayInputStream(array);
        DataInputStream dis = new DataInputStream(bais);

        this.index = dis.readInt();
        this.name = dis.readUTF();
        this.artist = dis.readUTF();
        this.date = dis.readLong();
        this.length = dis.readDouble();

        int fArtistsLength = dis.readInt();
        this.fArtists = new String[fArtistsLength];

        for(int i = 0; i < fArtistsLength; i++){

            int artistSize = dis.readInt(); 
            byte[] artistBytes = new byte[artistSize];
            dis.readFully(artistBytes);
            this.fArtists[i] = new String(artistBytes, "UTF-8");

        }

    }

    public Musica(byte[] array) throws IOException {
        fromByteArray(array);
    }


    public String getName() {
        return name;
    }

    
}
