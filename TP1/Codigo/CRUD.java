import java.io.*;
import java.nio.ByteBuffer;

public final class CRUD {
    
    private static String arquivo = "DataBase/rock.db";

    private CRUD(){}


/////////////////////////////////////////////////CREATE//////////////////////////////////////////////////////

    public static void create(Musica musica, boolean conf) throws FileNotFoundException, IOException{

        try(RandomAccessFile file = new RandomAccessFile(arquivo, "rw")){

            file.seek(0);
            file.writeInt(musica.getIndex());
            
            file.seek(file.length());
            byte[] array = musica.toByteArray();

            file.writeBoolean(false);
            file.writeInt(array.length);
            file.write(array);

            if(conf){

                System.out.println("Música adicionada com sucesso!");

            }

            file.close();

        }catch(IOException e){

            System.out.println("Erro: CRUD.create - " + e.getMessage());

        }

    }

/////////////////////////////////////////////////READ//////////////////////////////////////////////////////

    public static void read() throws FileNotFoundException, IOException{

        try(RandomAccessFile file = new RandomAccessFile(arquivo, "r")){

            file.readInt();

            while(file.getFilePointer() < file.length()){

                boolean lapide = file.readBoolean();
                int tamanho = file.readInt();
                byte[] array = new byte[tamanho];
                file.readFully(array);

                Musica nova = new Musica();
                nova.fromByteArray(array);

                if(!lapide){

                    System.out.println(nova);

                }

            }

            file.close();

        }catch(IOException e){

            System.out.println("Erro: CRUD.readAll - " + e.getMessage());

        }

    }

    public static Musica read(int id) throws IOException{

        try(RandomAccessFile file = new RandomAccessFile(arquivo, "r")){

            file.readInt();

            while(file.getFilePointer() < file.length()){

                boolean lapide = file.readBoolean();
                int tamanho = file.readInt();
                byte[] array = new byte[tamanho];
                file.readFully(array);

                Musica nova = new Musica();
                nova.fromByteArray(array);

                if(nova.getIndex() == id){

                    if(lapide){

                        return null;

                    }else{

                        return nova;

                    }

                }

            }

            file.close();

        }catch(IOException e){

            System.out.println("Erro: CRUD.read - " + e.getMessage());

        }

        return null;

    }

/////////////////////////////////////////////////UPDATE//////////////////////////////////////////////////////

public static int obterProximoId() throws IOException{

    try(RandomAccessFile file = new RandomAccessFile(arquivo, "rw")){

        file.seek(0);
        int ultimoId = file.readInt();
        return ultimoId + 1;

    }

}

public static void atualizarUltimoId(int novoId) throws IOException{

    try(RandomAccessFile file = new RandomAccessFile(arquivo, "rw")){

        file.seek(0);
        file.writeInt(novoId);

    }

}

public static byte[] modificarIdNoByteArray(byte[] array, int novoId){

    byte[] novoArray = array.clone(); 
    byte[] idBytes = ByteBuffer.allocate(4).putInt(novoId).array();

    System.arraycopy(idBytes, 0, novoArray, 0, 4);

    return novoArray;

}

    public static boolean update(int id, Musica novaMusica) throws IOException{

        try(RandomAccessFile file = new RandomAccessFile(arquivo, "rw")){

            file.readInt(); 

            while(file.getFilePointer() < file.length()){

                long posicao = file.getFilePointer();
                boolean lapide = file.readBoolean();
                int tamanhoAntigo = file.readInt();
                byte[] array = new byte[tamanhoAntigo];
                file.readFully(array);

                Musica musicaAntiga = new Musica();
                musicaAntiga.fromByteArray(array);

                if(musicaAntiga.getIndex() == id && !lapide){

                    byte[] novoArray = novaMusica.toByteArray();
                    int tamanhoNovo = novoArray.length;

                    if(tamanhoNovo <= tamanhoAntigo){

                        file.seek(posicao + 1 + 4); 
                        file.write(novoArray);

                        if(tamanhoNovo < tamanhoAntigo){

                            file.write(new byte[tamanhoAntigo - tamanhoNovo]);

                        }

                    }else{

                        file.seek(posicao);
                        file.writeBoolean(true);

                        int novoId = obterProximoId();
                        byte[] novoArrayComId = modificarIdNoByteArray(novoArray, novoId);


                        file.seek(file.length());
                        file.writeBoolean(false); 
                        file.writeInt(novoArrayComId.length);
                        file.write(novoArrayComId);

                        atualizarUltimoId(novoId);

                        return true;
                        
                    }

                    return true;

                }

            }

        }catch(IOException e){

            System.out.println("Erro: CRUD.update - " + e.getMessage());

        }

        return false;

    }


    public static int ultimoId() throws IOException{

        try(RandomAccessFile file = new RandomAccessFile(arquivo, "rw")){

            file.seek(0);
            int id = file.readInt();

            return id;

        }

    }
 
/////////////////////////////////////////////////DELETE//////////////////////////////////////////////////////

    public static boolean delete(int id) throws FileNotFoundException, IOException{

        try(RandomAccessFile file = new RandomAccessFile(arquivo, "rw")){

            file.readInt();

            while(file.getFilePointer() < file.length()){

                long posicao = file.getFilePointer();
                boolean lapide = file.readBoolean();
                int tamanho = file.readInt();
                byte[] array = new byte[tamanho];
                file.readFully(array);

                Musica nova = new Musica();
                nova.fromByteArray(array);

                if(nova.getIndex() == id && !lapide){

                    file.seek(posicao);
                    file.writeBoolean(true);

                    return true;

                }

            }

            file.close();

        }catch(IOException e){

            System.out.println("Erro: CRUD.delete - " + e.getMessage());

        }

        return false;

    }

}
