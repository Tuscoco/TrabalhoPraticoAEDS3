package repository;

import java.io.*;

import util.*;
import model.*;

public final class CRUD {
    
    private static String arquivo = "data/database/rock.db";
    //private static String arquivo = "data/database/final.db";

    private CRUD(){}//Construtor privado para impedir instanciações

/////////////////////////////////////////////////CREATE//////////////////////////////////////////////////////

    /*
     * Método para adicionar um registro do arquivo. 
     * 
     * Funcionamento: 
     * -Recebe um objeto "Musica" e organiza o registro.
     * -O arquivo é aberto, o ponteiro é movido para o início, onde escreve o ultimo índice incerido.
     * -O ponteiro é movido para o final do arquivo e escreve o registro, que é organizado em: lápide, tamanho do registro e dados.
     * -O arquivo é fechado.
     */
    public static void create(Musica musica, boolean conf) throws FileNotFoundException, IOException{

        Logger.log(LogLevel.INFO, "CREATE chamado!");

        try(RandomAccessFile file = new RandomAccessFile(arquivo, "rw")){

            musica.setIndex(obterProximoId());

            file.seek(0);
            file.writeInt(musica.getIndex());
            
            file.seek(file.length());
            byte[] array = musica.toByteArray();

            file.writeBoolean(false);
            file.writeInt(array.length);
            file.write(array);

            if(conf){

                System.out.println();
                System.out.println("Música adicionada com sucesso! ID = " + musica.getIndex());
                System.out.println();

            }

            file.close();

        }catch(IOException e){

            Logger.log(LogLevel.ERROR, "Erro CRUD.create: " + e.getMessage());

        }

    }

/////////////////////////////////////////////////READ//////////////////////////////////////////////////////

    /*
     * Método para ler todos os registros do arquivo. 
     * 
     * Funcionamento: 
     * -O arquivo é aberto e o ponteiro lê registro por registro.
     * -Se a lápide estiver falsa, um novo objeto "Musica" é criado com os dados lidos do arquivo e imprimidos na tela.
     * -Se a lápide estiver verdadeira(registro "removido"), os dados não são imprimidos na tela.
     * -O arquivo é fechado.
     */
    public static void read() throws FileNotFoundException, IOException{

        Logger.log(LogLevel.INFO, "READALL chamado!");

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

            Logger.log(LogLevel.ERROR, "Erro CRUD.readAll: " + e.getMessage());

        }

    }

    /*
     * Método para ler um registro do arquivo. 
     * 
     * Funcionamento: 
     * -Recebe o índice do registro desejado.
     * -O arquivo é aberto e o ponteiro lê registro por registro até achar o índice procurado.
     *     -Se o índice for encontrado.
     *         -Se a lápide estiver falsa, um novo objeto "Musica" é criado com os dados lidos do arquivo e retornado.
     *         -Se a lápide estiver verdadeira(registro "removido"), o método retorna null(registro não encontrado).
     *     -Se o índice não for encontrado, o método retorna null(registro não encontrado).
     * -O arquivo é fechado.
     */
    public static Musica read(int id) throws IOException{

        Logger.log(LogLevel.INFO, "READ chamado!");

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

                    if(!lapide){

                    
                        return nova;

                    }

                }

            }

            file.close();

        }catch(IOException e){

            Logger.log(LogLevel.ERROR, "Erro CRUD.read: " + e.getMessage());

        }

        return null;

    }

/////////////////////////////////////////////////UPDATE//////////////////////////////////////////////////////

    /*
     * Método para atualizar um registro do arquivo. 
     * 
     * Funcionamento: 
     * -Recebe o id da música a ser atualizada e o novo objeto de música.
     * -Identifica se a música está "viva" ou "morta" e se o novo tamanho é menor ou igual ao antigo. Se estiver "morta retorna false."
     * -Se o novo tamanho é menor ou igual ao antigo, o registro é atualizado.
     * -Se o novo tamanho é maior que o antigo, a lápide é marcada como verdadeira e um novo registro é adicionado no final do arquivo com o mesmo índice da música a ser atualizada.
     */
     public static boolean update(int id, Musica novaMusica) throws IOException{

        Logger.log(LogLevel.INFO, "UPDATE chamado!");

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

                        file.seek(file.length());
                        file.writeBoolean(false); 
                        file.writeInt(novoArray.length);
                        file.write(novoArray);                        
                    }

                    return true;

                }

            }

        }catch(IOException e){

            Logger.log(LogLevel.ERROR, "Erro CRUD.update: " + e.getMessage());

        }

        return false;

    }
 
/////////////////////////////////////////////////DELETE//////////////////////////////////////////////////////

    /*
     * Método para "deletar" um registro do arquivo. 
     * 
     * Funcionamento: 
     * -Recebe o índice do registro que se deseja "deletar".
     * -O arquivo é aberto e o ponteiro lê registro por registro até achar o índice procurado.
     *     -Se o índice for encontrado, o ponteiro retorna até a lápide do registro .
     *         -Se a lápide estiver falsa(registro não removido) ela é marcada como verdadeira e o método retorna true(registro "removido").
     *         -Se a lápide estiver verdadeira(registro removido) o método retorna false(registro já "removido").
     *     -Se o índice não for encontrado, o método retorna false(registro não encontrado).
     * -O arquivo é fechado.
     */
    public static boolean delete(int id) throws FileNotFoundException, IOException{

        Logger.log(LogLevel.INFO, "DELETE chamado!");

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

            Logger.log(LogLevel.ERROR, "Erro CRUD.delete: " + e.getMessage());

        }

        return false;

    }

/////////////////////////////////////////////////AUXILIARES//////////////////////////////////////////////////////

    /*
     * Método para obter o próximo índice. 
     * 
     * Funcionamento: 
     * -O arquivo é aberto e o ponteiro é movido para o início do arquivo.
     * -O ponteiro lê o ultimo índice inserido, soma 1 e retorna.
     * -O arquivo é fechado.
     */
    private static int obterProximoId() throws IOException{

        try(RandomAccessFile file = new RandomAccessFile(arquivo, "rw")){

            if(file.length() == 0){

                return 0;

            }else{

                file.seek(0);
                int ultimoId = file.readInt();
                return ultimoId + 1;

            }

        }

    }

    /*
     * Método para apagar database
     * 
     * Funcionamento: 
     * -Abre o arquivo .db e apaga todo o conteúdo
     */
    public static void reiniciarBD() throws FileNotFoundException, IOException{

        try(RandomAccessFile file = new RandomAccessFile(arquivo, "rw")){

            file.setLength(0);

            file.close();

        }

    }

}
