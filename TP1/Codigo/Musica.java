import java.io.*;

public class Musica {
    
    private int index;
    private String name;
    private String artist;
    private String date;
    private double length;
    private String[] fArtists;


    public Musica(){}


    public Musica(int index, String name, String artist, String date, double length, String[] fArtists){

        this.index = index;
        this.name = name;
        this.artist = artist;
        this.date = date;
        this.length = length;
        this.fArtists = fArtists;

    }

    public int getIndex(){

        return index;

    }

    @Override
    public String toString(){

        return "\n|Index: " + index + "||Name: " + name + "||Artist: " + artist + "||Date: " + date + "||Length: " + length + "||Featured_Artists: " + String.join(", ", fArtists) + "|\n";

    }

    public byte[] toByteArray() throws IOException{

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        dos.writeInt(index);
        dos.writeUTF(name);
        dos.writeUTF(artist);
        dos.writeUTF(date);
        dos.writeDouble(length);
        dos.writeInt(fArtists.length);

        for(String artist : fArtists){
            
            byte[] artistBytes = artist.getBytes("UTF-8");
            dos.writeInt(artistBytes.length); 
            dos.write(artistBytes);
        }

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

        int fArtistsLength = dis.readInt();
        this.fArtists = new String[fArtistsLength];

        for(int i = 0; i < fArtistsLength; i++){

            int artistSize = dis.readInt(); 
            byte[] artistBytes = new byte[artistSize];
            dis.readFully(artistBytes);
            this.fArtists[i] = new String(artistBytes, "UTF-8");

        }

    }

}
