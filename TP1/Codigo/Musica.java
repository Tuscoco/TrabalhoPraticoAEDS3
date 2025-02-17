import java.io.*;

public class Musica {
    
    private int index;
    private String name;
    private String artist;
    private String date;
    private double length;
    private String fArtists;

    private boolean removido;


    public Musica(){}


    public Musica(int index, String name, String artist, String date, double length, String fArtists){

        this.index = index;
        this.name = name;
        this.artist = artist;
        this.date = date;
        this.length = length;
        this.fArtists = fArtists;
        this.removido = false;

    }

    public int getIndex(){

        return index;

    }

    public boolean isRemovido(){

        return removido;

    }

    public void setRemovido(boolean removido){

        this.removido = removido;

    }

    @Override
    public String toString(){

        return "index: " + index + ", name: " + name + ", artist: " + artist + ", date: " + date + ", length: " + length + ", fArtists: " + fArtists;

    }

    public byte[] toByteArray() throws IOException{

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        dos.writeInt(index);
        dos.writeUTF(name);
        dos.writeUTF(artist);
        dos.writeUTF(date);
        dos.writeDouble(length);
        dos.writeUTF(fArtists);
        dos.writeBoolean(removido);

        return baos.toByteArray();

    }

    public void fromByteArray(byte[] array) throws IOException{

        ByteArrayInputStream bais = new ByteArrayInputStream(array);
        DataInputStream dis = new DataInputStream(bais);

        this.index = dis.readInt();
        this.name = dis.readUTF();
        this.artist = dis.readUTF();
        this.date = dis.readUTF();
        this.length = dis.readDouble();
        this.fArtists = dis.readUTF();
        this.removido = dis.readBoolean();

    }

}
