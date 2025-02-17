import java.io.*;

public final class CRUD {
    
    private static String arquivo = "rock.db";

    private CRUD(){}


    public static void criarMusica(Musica musica) throws FileNotFoundException, IOException{

        try(RandomAccessFile file = new RandomAccessFile(arquivo, "rw")){

            file.seek(file.length());

            byte[] array = musica.toByteArray();

            file.writeInt(array.length);
            file.write(array);

            //System.out.println("Música adicionada com sucesso!");

        }catch(IOException e){

            System.out.println("Erro: " + e.getMessage());

        }

    }

    public static void listarMusicas() throws FileNotFoundException, IOException{

        try(RandomAccessFile file = new RandomAccessFile(arquivo, "r")){

            while(file.getFilePointer() < file.length()){

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

    public static Musica buscarMusica(int id) throws IOException{

        try(RandomAccessFile file = new RandomAccessFile(arquivo, "r")){

            while(file.getFilePointer() < file.length()){

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

}
