package repository;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import algorithms.indexing.btree.*;
import algorithms.indexing.hash.*;
import algorithms.indexing.invertedList.*;
import model.Musica;

@SuppressWarnings("unused")
public class CsvHandler {
    
    private static String arquivo = "data/csv/rock6.csv";
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private CsvHandler(){}

    /*
     * Método para preencher arquivo com dados do csv para CRUD Sequencial
     * 
     * Funcionamento: 
     * -Abre o arquivo .csv e le a primeira linha(cabeçalho)
     * -Le o arquivo linha a linha, trata e organiza dados 
     * -Cria um objeto com os dados e os envia para o arquivo atraves do método create da classe CRUD
     */
    public static void preencherCatalogo(){

        try{

            RandomAccessFile file = new RandomAccessFile(arquivo, "r");

            CRUD.reiniciarBD();

            file.readLine();//Cabeçalho(não é necessário)

            String linha;

            while((linha = file.readLine()) != null){

                String[] dados = linha.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);

                if(dados.length >= 2){

                    String name = dados[0];
                    String artist = String.format("%-" + 15 + "s", dados[1]);
                    long date = 0;

                    try{
                        Date parsedDate = dateFormat.parse(dados[2]);
                        date = parsedDate.getTime();

                    } catch (ParseException e){

                        System.out.println("Erro: " + e.getMessage());
                    }

                    double length = Double.parseDouble(dados[3]);
                    String[] fArtist = new String[0];

                    if(dados.length > 4){

                        String featuredArtists = dados[4];
                        if(featuredArtists.startsWith("\"")){

                            featuredArtists = featuredArtists.substring(1, featuredArtists.length() - 1);

                        }

                    fArtist = featuredArtists.split(",\\s*"); 

                    }             

                    Musica musica = new Musica(name, artist, date, length, fArtist);

                    CRUD.create(musica, false);

                }

            }

            file.close();

            System.out.println();
            System.out.println("Base de dados carregada no arquivo!");
            System.out.println();

        }catch(IOException e){

            System.out.println("Erro: " + e.getMessage());

        }

    }

    /*
     * Método para preencher arquivo com dados do csv para CRUD Indexado
     * 
     * Funcionamento: 
     * - Abre o arquivo .csv e le a primeira linha(cabeçalho)
     * - Le o arquivo linha a linha, trata e organiza dados 
     * - Cria um objeto com os dados e os envia para o arquivo atraves do método create da classe CRUD
     * - Cria um objeto Registro e insere no arquivo de índice escolhido
     */
    public static void preencherCatalogoIndexado(int indice, int ordem){

        try{

            RandomAccessFile file = new RandomAccessFile(arquivo, "r");

            if(indice == 1){

                BTree btree = new BTree(ordem);

            }else if (indice == 2) {
           
                HashExtensivel hash = new HashExtensivel();
                
            }else if(indice == 3){

                InvertedList lista = new InvertedList(ordem);

            }

            CRUDI.reiniciarBD();

            file.readLine();

            String linha;

            while((linha = file.readLine()) != null){

                String[] dados = linha.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);

                String name = dados[0];
                String artist = String.format("%-" + 15 + "s", dados[1]);
                long date = 0;

                try{

                    Date parsedDate = dateFormat.parse(dados[2]);
                    date = parsedDate.getTime();

                } catch (ParseException e){

                    System.out.println("Erro: " + e.getMessage());

                }

                double length = Double.parseDouble(dados[3]);
                String[] fArtist = new String[0];

                if(dados.length > 4){

                    String featuredArtists = dados[4];
                    if(featuredArtists.startsWith("\"")){

                        featuredArtists = featuredArtists.substring(1, featuredArtists.length() - 1);

                    }

                   fArtist = featuredArtists.split(",\\s*"); 

                }             

                Musica musica = new Musica(name, artist, date, length, fArtist);

                CRUDI.create(musica, false, indice);

            }

            file.close();

            System.out.println();
            System.out.println("Base de dados carregada no arquivo de dados e no índice!");
            System.out.println();

        }catch(IOException e){

            System.out.println("Erro: " + e.getMessage());

        }

    }

}
