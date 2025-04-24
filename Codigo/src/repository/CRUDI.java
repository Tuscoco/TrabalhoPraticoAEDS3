package repository;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import util.*;
import model.*;
import algorithms.btree.BTree;
import algorithms.hash.*;
import algorithms.invertedList.InvertedList;

@SuppressWarnings("unused")
public final class CRUDI {
    
    private static String arquivo = "data/database/rockI.db";
    private static String hashExtensivel = "data/indexes/Hash.db";
    private static String listaInvertida = "data/indexes/ListaInvertida.db";

    private CRUDI(){}//Construtor privado para impedir instanciações

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
    public static void create(Musica musica, boolean conf, int indice) throws FileNotFoundException, IOException{

        Logger.log(LogLevel.INFO, "CREATE chamado!");

        try(RandomAccessFile file = new RandomAccessFile(arquivo, "rw")){

            musica.setIndex(obterProximoId());

            file.seek(0);
            file.writeInt(musica.getIndex());
            
            long endereco = file.length();
            file.seek(endereco);
            byte[] array = musica.toByteArray();

            file.writeBoolean(false);
            file.writeInt(array.length);
            file.write(array);

            Registro registro = new Registro(musica.getIndex(), endereco);

            if(indice == 1){

                BTree btree = new BTree();

                btree.inserir(registro);

            }else if(indice == 2){

                HashExtensivel hash = new HashExtensivel(2);
                hash.inserir(registro);
                

            }else if(indice == 3){

                InvertedList lista = new InvertedList();

                lista.inserir(musica);

            }

            if(conf){

                System.out.println();
                System.out.println("Música adicionada com sucesso! ID = " + musica.getIndex());
                System.out.println();

            }

            file.close();

        }catch(IOException e){

            Logger.log(LogLevel.ERROR, "Erro CRUDI.create: " + e.getMessage());

        }

    }

/////////////////////////////////////////////////READ//////////////////////////////////////////////////////

    /*
     * Método para ler um registro do arquivo. 
     * 
     * Funcionamento: 
     * -Recebe o índice do registro desejado.
     * -O arquivo é aberto e o ponteiro lê registro por registro até achar o índice procurado.
     * -Se o índice for encontrado.
     * -Se a lápide estiver falsa, um novo objeto "Musica" é criado com os dados lidos do arquivo e retornado.
     * -Se a lápide estiver verdadeira(registro "removido"), o método retorna null(registro não encontrado).
     * -Se o índice não for encontrado, o método retorna null(registro não encontrado).
     * -O arquivo é fechado.
     */
    public static Musica read(int id, int indice) throws IOException{

        Logger.log(LogLevel.INFO, "READ chamado!");

        try(RandomAccessFile file = new RandomAccessFile(arquivo, "r")){

            Registro registro = null;

            if(indice == 1){

                BTree btree = new BTree();
                registro = btree.procurar(id);

            }else if(indice == 2){
                
                //Procurar na hash

            }

            if(registro != null){

                file.seek(registro.end);
                boolean lapide = file.readBoolean();

                if(!lapide){

                    int tamanho = file.readInt();
                    byte[] array = new byte[tamanho];
                    file.readFully(array);
    
                    Musica nova = new Musica();
                    nova.fromByteArray(array);

                    return nova;

                }else{

                    return null;

                }

            }else{

                return null;

            }

        }catch(IOException e){

            Logger.log(LogLevel.ERROR, "Erro CRUDI.read: " + e.getMessage());

        }

        return null;

    }

    public static List<Musica> read(String artista){

        try{

            List<Integer> elementos = new ArrayList<>();
            List<Musica> resultado = new ArrayList<>();
            InvertedList lista = new InvertedList();

            elementos = lista.procurar(artista);

            if(!elementos.isEmpty()){

                for(Integer x : elementos){

                    resultado.add(read(x, 1));

                }

            }

            return resultado;

        }catch(Exception e){

            e.printStackTrace();

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
     public static boolean update(int id, Musica novaMusica, int indice) throws IOException{

        Logger.log(LogLevel.INFO, "UPDATE chamado!");

        try(RandomAccessFile file = new RandomAccessFile(arquivo, "rw")){

            Registro registro = null;

            if(indice == 1){

                BTree btree = new BTree();
                registro = btree.procurar(id);

            }else if(indice == 2){
                
                //Procurar na hash

            }

            if(registro != null){

                long endereco = registro.end;
                file.seek(endereco);
                boolean lapide = file.readBoolean();

                if(!lapide){

                    int tamanhoAntigo = file.readInt();
                    byte[] array = new byte[tamanhoAntigo];
                    file.readFully(array);
    
                    Musica musicaAntiga = new Musica();
                    musicaAntiga.fromByteArray(array);

                    byte[] novoArray = novaMusica.toByteArray();
                    int tamanhoNovo = novoArray.length;

                    if(tamanhoNovo <= tamanhoAntigo){

                        file.seek(endereco + 1 + 4); 
                        file.write(novoArray);

                        if(tamanhoNovo < tamanhoAntigo){

                            file.write(new byte[tamanhoAntigo - tamanhoNovo]);

                        }

                    }else{

                        file.seek(endereco);
                        file.writeBoolean(true);

                        long enderecoNovo = file.length();

                        file.seek(enderecoNovo);
                        file.writeBoolean(false); 
                        file.writeInt(novoArray.length);
                        file.write(novoArray);

                        if(indice == 1){

                            BTree btree = new BTree();

                            btree.atualizar(id, enderecoNovo);
    
                        }else if(indice == 2){
    
                            //Atualizar na Hash
    
                        }

                    }

                    return true;

                }else{

                    return false;

                }

            }else{

                return false;

            }

        }catch(IOException e){

            Logger.log(LogLevel.ERROR, "Erro CRUDI.update: " + e.getMessage());

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
     * -Se o índice for encontrado, o ponteiro retorna até a lápide do registro .
     * -Se a lápide estiver falsa(registro não removido) ela é marcada como verdadeira e o método retorna true(registro "removido").
     * -Se a lápide estiver verdadeira(registro removido) o método retorna false(registro já "removido").
     * -Se o índice não for encontrado, o método retorna false(registro não encontrado).
     * -O arquivo é fechado.
     */
    public static boolean delete(int id, int indice) throws FileNotFoundException, IOException{

        Logger.log(LogLevel.INFO, "DELETE chamado!");

        try(RandomAccessFile file = new RandomAccessFile(arquivo, "rw")){

            Registro registro = null;

            if(indice == 1){

                BTree btree = new BTree();
                registro = btree.procurar(id);

            }else if(indice == 2){
                
                //Procurar na hash

            }else if(indice == 3){

                //Procurar na Lista Invertida 

            }

            if(registro != null){

                long endereco = registro.end;
                file.seek(endereco);
                boolean lapide = file.readBoolean();

                if(!lapide){

                    file.seek(endereco);
                    file.writeBoolean(true);

                    if(indice == 1){

                        BTree btree = new BTree();

                        btree.deletar(id);

                    }else if(indice == 2){

                        //Deletar na hash

                    }

                    return true;

                }else{

                    return false;

                }

            }else{

                return false;

            }

        }catch(IOException e){

            Logger.log(LogLevel.ERROR, "Erro CRUDI.delete: " + e.getMessage());

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
