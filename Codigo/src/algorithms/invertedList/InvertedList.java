package algorithms.invertedList;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

import model.Musica;

public class InvertedList {

    private static final String arquivo = "data/indexes/InvertedList.db";
    public static RandomAccessFile file;

    public InvertedList(){

        try{

            file = new RandomAccessFile(arquivo, "rw");

        }catch(Exception e){

            e.printStackTrace();

        }

    }

    public InvertedList(int i){

        try{

            file = new RandomAccessFile(arquivo, "rw");

            file.setLength(0);

        }catch(Exception e){

            e.printStackTrace();

        }

    }

    public void inserir(Musica musica){

        try{

            file.seek(0);

            if(file.length() != 0){

                String banda;
                boolean adicionou = false;
                while(file.getFilePointer() < file.length() && (banda = file.readUTF()) != null && !adicionou){

                    if(banda.equals(musica.getArtist())){

                        file.readInt();
                        long end1 = file.getFilePointer();
                        long end = file.readLong();

                        while(end != -1){

                            file.seek(end);
                            file.readUTF();
                            file.readInt();
                            end1 = file.getFilePointer();
                            end = file.readLong();

                        }

                        file.seek(end1);
                        file.writeLong(file.length());
                        file.seek(file.length());

                        file.writeUTF(musica.getArtist());
                        file.writeInt(musica.getIndex());
                        file.writeLong(-1);

                        adicionou = true;

                    }else{

                        file.seek(file.getFilePointer() + 12);

                    }

                }

                if(!adicionou){

                    file.seek(file.length());
                    file.writeUTF(musica.getArtist());
                    file.writeInt(musica.getIndex());
                    file.writeLong(-1);

                }

            }else{

                file.writeUTF(musica.getArtist());
                file.writeInt(musica.getIndex());
                file.writeLong(-1);

            }

        }catch(Exception e){

            e.printStackTrace();

        }

    }

    public List<Integer> procurar(String artista){

        try{

            List<Integer> lista = new ArrayList<>();
            String banda = tratarTamanho(artista, 15);

            file.seek(0);

            if(file.length() != 0){

                String bandaArq;
                boolean acabou = false;
                while(file.getFilePointer() < file.length() && !acabou){

                    bandaArq = file.readUTF();
                    int id = file.readInt();
                    long end = file.readLong();

                    if(bandaArq.trim().toLowerCase().equals(banda.trim().toLowerCase())){

                        lista.add(id);

                        while(end != -1){

                            file.seek(end);
                            file.readUTF();
                            lista.add(file.readInt());
                            end = file.readLong();

                        }

                        acabou = true;

                    }

                }

                return lista;

            }

        }catch(Exception e){

            e.printStackTrace();

        }

        return null;

    }

    public void lerTudo() throws IOException{

        file.seek(0);
        while(file.getFilePointer() < file.length()){

            String banda = file.readUTF();
            int id = file.readInt();
            long end = file.readLong();

            System.out.println(banda + " -- " + id + " -- " + end);

        }

    }

    private String tratarTamanho(String str, int tamanho){

        if(str.length() > tamanho){

            return str.substring(0, tamanho);

        }

        return String.format("%-" + tamanho + "s", str);

    }
    
}