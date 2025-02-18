import java.io.*;

public final class CRUD {
    
    private static String arquivo = "DataBase/rock.db";

    private CRUD(){}


/////////////////////////////////////////////////CREATE//////////////////////////////////////////////////////

    public static void create(Musica musica, boolean conf) throws FileNotFoundException, IOException{

        try(RandomAccessFile file = new RandomAccessFile(arquivo, "rw")){

            file.seek(file.length());

            byte[] array = musica.toByteArray();

            file.writeBoolean(musica.isRemovido());
            file.writeInt(array.length);
            file.write(array);

            if(conf){

                System.out.println("Música adicionada com sucesso!");

            }

        }catch(IOException e){

            System.out.println("Erro: " + e.getMessage());

        }

    }

/////////////////////////////////////////////////READ//////////////////////////////////////////////////////

    public static void read() throws FileNotFoundException, IOException{

        try(RandomAccessFile file = new RandomAccessFile(arquivo, "r")){

            while(file.getFilePointer() < file.length()){

                boolean lapide = file.readBoolean();
                int tamanho = file.readInt();
                byte[] array = new byte[tamanho];
                file.readFully(array);

                Musica nova = new Musica();
                nova.fromByteArray(array);

                if(!nova.isRemovido()){

                    System.out.println(nova);

                }

            }

        }catch(IOException e){

            System.out.println("Erro: " + e.getMessage());

        }

    }

    public static Musica read(int id) throws IOException{

        try(RandomAccessFile file = new RandomAccessFile(arquivo, "r")){
            System.out.println("Entrei na função");

            while(file.getFilePointer() < file.length()){

                boolean lapide = file.readBoolean();
                int tamanho = file.readInt();
                byte[] array = new byte[tamanho];
                file.readFully(array);

                Musica nova = new Musica();
                nova.fromByteArray(array);

                if(nova.getIndex() == id){

                    return nova;

                }

            }

        }catch(IOException e){

            System.out.println("Erro: " + e.getMessage());

        }

        return null;

    }

/////////////////////////////////////////////////UPDATE//////////////////////////////////////////////////////

    public static void update(){}
 
/////////////////////////////////////////////////DELETE//////////////////////////////////////////////////////

    public static void delete(){}

}
