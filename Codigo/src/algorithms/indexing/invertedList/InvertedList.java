package algorithms.indexing.invertedList;

import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

import model.Musica;

/*
 * Classe que implementa o algoritmo de Lista Invertida em memória secundária
 */
public class InvertedList {

    private static final String arquivo = "data/indexes/InvertedList.db";
    public static RandomAccessFile file;

    /*
     * Construtor usado quando a lista já está construída
     * 
     * Funcionamento:
     * - Abre o arquivo de índice
     */
    public InvertedList(){

        try{

            file = new RandomAccessFile(arquivo, "rw");

        }catch(Exception e){

            e.printStackTrace();

        }

    }

    /*
     * Construtor usado quando a lista não está construída
     * 
     * Funcionamento:
     * - Abre o arquivo de índice e apaga tudo
     */
    public InvertedList(int i){

        try{

            file = new RandomAccessFile(arquivo, "rw");

            file.setLength(0);

        }catch(Exception e){

            e.printStackTrace();

        }

    }

    /*
     * Método de inserir um registro na lista invertida
     * 
     * Funcionamento:
     * - Vai para o início do arquivo e procura a banda da musica a ser inserida
     * - Se não achar, vai para o final do arquivo e insere
     * - Se achar, verifica o ponteiro para o próximo até achar o -1
     * - Insere no final
     * - Organizado em: Banda - ID - ponteiro para o próximo
     */
    public void inserir(Musica musica){

        try{

            file.seek(0);

            if(file.length() != 0){

                String banda;
                boolean adicionou = false;
                while(file.getFilePointer() < file.length() && (banda = file.readUTF()) != null && !adicionou){

                    if(banda.trim().toLowerCase().equals(musica.getArtist().trim().toLowerCase())){

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

    /*
     * Método para procurar um conjunto de índices de musicas
     * 
     * Funcionamento:
     * - Anda pelo arquivo até achar a primeira aparição da banda
     * - Se não houver, retorna uma lista vazia
     * - Depois da primeira aparição, anda pelos ponteiros guardando os índices na lista
     * - Retorna a lista
     */
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

    /*
     * Método que limita o tamanho do nome recebido
     */
    private String tratarTamanho(String str, int tamanho){

        if(str.length() > tamanho){

            return str.substring(0, tamanho);

        }

        return String.format("%-" + tamanho + "s", str);

    }
    
}