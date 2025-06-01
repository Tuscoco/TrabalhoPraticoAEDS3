package util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


import model.Musica;
import repository.*;
import algorithms.compression.huffman.Huffman;
import algorithms.sorting.IntercalacaoBalanceada;

public class ConsoleHelper {
    
    private static Scanner scan;
    private static String tipoIndice;

    private ConsoleHelper(){}

    /*
     * Método para imprimir o menu de escolhas do usuário
     */
    private static void tipoMenu(){

        System.out.println("=========================BEM=VINDO=AO=DB=DO=ROCK=N=ROLL=========================");
        System.out.println("Escolha o tipo de método que será utilizado:");
        System.out.println("1 -> CRUD Sequencial");
        System.out.println("2 -> CRUD Indexado");
        System.out.println("3 -> Compressão de Huffman");
        System.out.println("0 -> Encerrar");
        System.out.println("================================================================================");

    }

    /*
     * Método para imprimir o menu de escolhas do usuário
     */
    private static void menu(int tipo, int indice){

        if(tipo == 1){

            System.out.println("===================================SEQUENCIAL===================================");
            System.out.println("Escolha uma opção: ");
            System.out.println("1 -> Carregar base de dados no arquivo");
            System.out.println("2 -> Criar um registro");
            System.out.println("3 -> Ler um registro");
            System.out.println("4 -> Atualizar registro");
            System.out.println("5 -> Deletar registro");
            System.out.println("6 -> Ler todos os registros");
            System.out.println("7 -> Ordenar arquivo");
            System.out.println("8 -> Ler arquivo ordenado");
            System.out.println("0 -> Voltar");
            System.out.println("================================================================================");

        }else if(tipo == 2){

            if(indice == 1){
                        
                tipoIndice = "====================================ArvoreB=====================================";

            }else if(indice == 2){

                tipoIndice = "===================================TabelaHash===================================";

            }

            System.out.println("====================================INDEXADO====================================");
            System.out.println(tipoIndice);
            System.out.println("Escolha uma opção: ");
            System.out.println("1 -> Carregar base de dados no arquivo");
            System.out.println("2 -> Criar um registro");
            System.out.println("3 -> Ler um registro");
            System.out.println("4 -> Atualizar registro");
            System.out.println("5 -> Deletar registro");
            System.out.println("0 -> Voltar");
            System.out.println("================================================================================");

        }else if(tipo == 3){

            System.out.println("====================================INDEXADO====================================");
            System.out.println("=================================ListaInvertida=================================");
            System.out.println("Escolha uma opção: ");
            System.out.println("1 -> Carregar base de dados no arquivo");
            System.out.println("2 -> Criar um registro");
            System.out.println("3 -> Ler um conjunto de registros");
            System.out.println("0 -> Voltar");
            System.out.println("================================================================================");

        }

    }

    /*
     * Método para imprimir o menu de escolhas do usuário
     */
    private static void menuDeIndices(){

        System.out.println("================================================================================");
        System.out.println("Escolha o tipo de índice que será utilizado:");
        System.out.println("1 -> Árvore B");
        System.out.println("2 -> Tabela Hash Extensível");
        System.out.println("3 -> Lista Invertida");
        System.out.println("0 -> Voltar");
        System.out.println("================================================================================");

    }

    /*
     * Método "limpar" o terminal
     * 
     * Funcionamento: 
     * -Imprime várias linhas em branco em sequencia para "limpar" o terminal
     */
    private static void clear(){

        System.out.print("\033[H\033[2J");
        System.out.flush();

        }

       public static void executarCompressao() {
    Scanner scan = new Scanner(System.in);

    System.out.println("Lendo músicas do arquivo rocki.db...");

    String texto = CRUDI.lerTudoComoTexto();

    if (texto.isEmpty()) {
        System.out.println("Nenhuma música foi encontrada no banco.");
        return;
    }

    Huffman huffman = new Huffman();
    huffman.compress(texto);

    System.out.println("\nDeseja descomprimir o texto agora? (s/n)");
    String resposta = scan.nextLine().trim().toLowerCase();

    if (resposta.equals("s")) {
        System.out.println("Digite o texto compactado (em binário):");
        String encoded = scan.nextLine();
        huffman.decompress(encoded);
        System.out.println("\nPressione ENTER para voltar ao menu.");
        scan.nextLine();
    }
}

    /*
     * Método para a execução do programa
     */
    public static void run() throws FileNotFoundException, IOException{

        scan = new Scanner(System.in);

        int tipoDeCRUD;

        do{

            clear();
            tipoMenu();

            tipoDeCRUD = scan.nextInt();

            if(tipoDeCRUD != 0){

                if(tipoDeCRUD == 1){

                    clear();
                    runSequencial();
    
                }else if(tipoDeCRUD == 2){
    
                    clear();
                    runIndexado();
    
                }else if (tipoDeCRUD == 3) {
                    ConsoleHelper.executarCompressao();
                
                } else {
                    
                    clear();
                    System.out.println("Opção inválida!");
    
                }

            }

        }while(tipoDeCRUD != 0);

        scan.close();

    }

    /*
     * Método para a interação com o usuário para CRUD Sequencial
     * 
     * Funcionamento: 
     * -Chama o método menu e recebe a opção do usuário
     * -Recebe informações do usuário e chama o método necessário pra cada caso
     */
    public static void runSequencial() throws FileNotFoundException, IOException{

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy"); //Reconhece o formato que a data será lida e exibida ao usuário para utilizar nas transformações para Timestamp
    
        int op = -1;
        int id = -1;
    
        do{
    
            menu(1, 0);
    
            op = scan.nextInt();
    
            switch(op){
    
                case 0:

                    break;
                
                case 1:
    
                    clear();
                    CsvHandler.preencherCatalogo();
                    break;

                case 2:
                
                    scan.nextLine();
                    clear();
    
                    System.out.print("Nome: ");
                    String nome = scan.nextLine();
                    System.out.print("Artista: ");
                    String artista = scan.nextLine();
                    System.out.print("Data dd-MM-yyyy: ");
                    String dataStr = scan.nextLine();
                    long data = 0;

                    try{

                        Date parsedDate = dateFormat.parse(dataStr);
                        data = parsedDate.getTime();

                    }catch(ParseException e){

                        System.out.println("Erro: " + e.getMessage());
                        
                    }

                    System.out.print("Duração: ");
                    double duracao = scan.nextDouble();
                    scan.nextLine();
                    
                    System.out.print("Artistas relacionados (separados por vírgula): ");
                    String[] artistas = scan.nextLine().split(",\\s*");

                    Musica addNovaMusica = new Musica(nome, artista, data, duracao, artistas);
                    CRUD.create(addNovaMusica, true);
        
                    break;
                
                case 3:
    
                    clear();
                    System.out.print("Informe o index da música procurada: ");
                    id = scan.nextInt();

                    Musica musica = CRUD.read(id);
    
                    clear();

    
                    if(musica == null){
    
                        System.out.println();
                        System.out.println("Registro não encontrado!");
                        System.out.println();
    
                    }else{
    
                        System.out.println(musica);
    
                    }
    
                    break;
    
                case 4:
    
                    clear();
                    System.out.print("Informe o index da música a ser atualizada: ");
                    id = scan.nextInt();
                    scan.nextLine();

                    System.out.print("Novo nome: ");
                    String name = scan.nextLine();
                    System.out.print("Novo artista: ");
                    String artist = scan.nextLine();
                    System.out.print("Nova data dd-MM-yyyy: ");
                    String dateStr = scan.nextLine();
                    long date = 0;

                    try{

                        Date parsedDate = dateFormat.parse(dateStr);
                        date = parsedDate.getTime();
                        
                    }catch(ParseException e){

                        System.out.println("Erro: " + e.getMessage());

                    }

                    System.out.print("Nova duração: ");
                    double length = scan.nextDouble();
                    scan.nextLine();
                    
                    System.out.print("Novos artistas relacionados (separados por vírgula): ");
                    String[] fArtists = scan.nextLine().split(",\\s*");

                    Musica novaMusica = new Musica(id, name, artist, date, length, fArtists);

                    if(CRUD.update(id, novaMusica)){

                        System.out.println();
                        System.out.println("Registro atualizado com sucesso!");
                        System.out.println();

                    }else{

                        System.out.println();
                        System.out.println("Erro ao atualizar o registro!");
                        System.out.println();

                    }
                    
                    break;
    
                case 5:
    
                    clear();
                    System.out.print("Informe o index da música que deseja remover: ");
                    id = scan.nextInt();

                    clear();
                    
                    if(CRUD.delete(id)){

                        System.out.println();
                        System.out.println("Música removida com sucesso!");
                        System.out.println();

                    }else{

                        System.out.println();
                        System.out.println("Erro ao remover a música!");
                        System.out.println();

                    }

                    System.out.println();

                    break;

                case 6:

                    clear();
                    CRUD.read('D');
                    break;

                case 7:

                    clear();
                    System.out.print("Informe o número de caminhos: ");
                    int numCaminhos = scan.nextInt();

                    IntercalacaoBalanceada.ordenar(numCaminhos);
                    
                    break;
    
                case 8:

                    CRUD.read('O');

                    break;
                
                default:
                    
                    clear();
                    System.out.println("Opção inválida!");
                    break;
    
            }
    
        }while(op != 0);

    }

    /*
     * Método para a interação com o usuário para CRUD Indexado
     * 
     * Funcionamento: 
     * -Chama o método menu e recebe a opção do usuário
     * -Recebe informações do usuário e chama o método necessário pra cada caso
     */
    private static void runIndexado() throws FileNotFoundException, IOException{

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy"); //Reconhece o formato que a data será lida e exibida ao usuário para utilizar nas transformações para Timestamp
    
        int op = -1;
        int id = -1;
        int indice = -1;

        do{

            clear();
            menuDeIndices();
            indice = scan.nextInt();
            clear();

            if(indice != 0){

                if(indice == 1 || indice == 2){

                    do{
        
                        menu(2, indice);
                
                        op = scan.nextInt();
                
                        switch(op){
                
                            case 0:
            
                                break;
                            
                            case 1:
                
                                clear();

                                int ordem = 0;

                                if(indice == 1){

                                    System.out.println("Informe a ordem da árvore B (acima de 2):");

                                    ordem = scan.nextInt();

                                    while(ordem <= 2){

                                        System.out.println("Ordem inválida!");
                                        ordem = scan.nextInt();

                                    }

                                } else if (indice == 2) {
                                    System.out.println("Informe o tamanho de cada bucket da tabela Hash (acima de 2):");
                                
                                    int tamanhoBucket = scan.nextInt();
                                
                                    while (tamanhoBucket <= 2) {
                                        System.out.println("Tamanho inválido!");
                                        tamanhoBucket = scan.nextInt();
                                    }
                                
                                
                                }

                                clear();
                                CsvHandler.preencherCatalogoIndexado(indice, ordem);
                                break;
            
                            case 2:
                            
                                scan.nextLine();
                                clear();
                
                                System.out.print("Nome: ");
                                String nome = scan.nextLine();
                                System.out.print("Artista: ");
                                String artista = scan.nextLine();
                                System.out.print("Data dd-MM-yyyy: ");
                                String dataStr = scan.nextLine();
                                long data = 0;
            
                                try{
            
                                    Date parsedDate = dateFormat.parse(dataStr);
                                    data = parsedDate.getTime();
            
                                }catch(ParseException e){
            
                                    System.out.println("Erro: " + e.getMessage());
                                    
                                }
            
                                System.out.print("Duração: ");
                                double duracao = scan.nextDouble();
                                scan.nextLine();
                                
                                System.out.print("Artistas relacionados (separados por vírgula): ");
                                String[] artistas = scan.nextLine().split(",\\s*");
            
                                Musica addNovaMusica = new Musica(nome, artista, data, duracao, artistas);
                                CRUDI.create(addNovaMusica, true, indice);
                    
                                break;
                            
                            case 3:
                
                                clear();
                                System.out.print("Informe o index da música procurada: ");
                                id = scan.nextInt();
            
                                Musica musica = CRUDI.read(id, indice);
                
                                clear();
            
                
                                if(musica == null){
                
                                    System.out.println();
                                    System.out.println("Registro não encontrado!");
                                    System.out.println();
                
                                }else{
                
                                    System.out.println(musica);
                
                                }
                
                                break;
                
                            case 4:
                
                                clear();
                                System.out.print("Informe o index da música a ser atualizada: ");
                                id = scan.nextInt();
                                scan.nextLine();
            
                                System.out.print("Novo nome: ");
                                String name = scan.nextLine();
                                System.out.print("Novo artista: ");
                                String artist = scan.nextLine();
                                System.out.print("Nova data dd-MM-yyyy: ");
                                String dateStr = scan.nextLine();
                                long date = 0;
            
                                try{
            
                                    Date parsedDate = dateFormat.parse(dateStr);
                                    date = parsedDate.getTime();
                                    
                                }catch(ParseException e){
            
                                    System.out.println("Erro: " + e.getMessage());
            
                                }
            
                                System.out.print("Nova duração: ");
                                double length = scan.nextDouble();
                                scan.nextLine();
                                
                                System.out.print("Novos artistas relacionados (separados por vírgula): ");
                                String[] fArtists = scan.nextLine().split(",\\s*");
            
                                Musica novaMusica = new Musica(id, name, artist, date, length, fArtists);
            
                                if(CRUDI.update(id, novaMusica, indice)){
            
                                    System.out.println();
                                    System.out.println("Registro atualizado com sucesso!");
                                    System.out.println();
            
                                }else{
            
                                    System.out.println();
                                    System.out.println("Erro ao atualizar o registro!");
                                    System.out.println();
            
                                }
                                
                                break;
                
                            case 5:
                
                                clear();
                                System.out.print("Informe o index da música que deseja remover: ");
                                id = scan.nextInt();
            
                                clear();
                                
                                if(CRUDI.delete(id, indice)){
            
                                    System.out.println();
                                    System.out.println("Música removida com sucesso!");
                                    System.out.println();
            
                                }else{
            
                                    System.out.println();
                                    System.out.println("Erro ao remover a música!");
                                    System.out.println();
            
                                }
            
                                System.out.println();
            
                                break;
                            
                            default:
                                
                                clear();
                                System.out.println("Opção inválida!");
                                break;
                
                        }
                
                    }while(op != 0);
    
                }else if(indice == 3){

                    do{

                        menu(3, indice);
                
                        op = scan.nextInt();

                        switch(op){

                            case 0:

                                break;

                            case 1:

                                clear();
                                CsvHandler.preencherCatalogoIndexado(indice, 0);
                                break;

                            case 2:

                                scan.nextLine();
                                clear();
            
                                System.out.print("Nome: ");
                                String nome = scan.nextLine();
                                System.out.print("Artista: ");
                                String artista = scan.nextLine();
                                System.out.print("Data dd-MM-yyyy: ");
                                String dataStr = scan.nextLine();
                                long data = 0;
        
                                try{
        
                                    Date parsedDate = dateFormat.parse(dataStr);
                                    data = parsedDate.getTime();
        
                                }catch(ParseException e){
        
                                    System.out.println("Erro: " + e.getMessage());
                                
                                }
        
                                System.out.print("Duração: ");
                                double duracao = scan.nextDouble();
                                scan.nextLine();
                            
                                System.out.print("Artistas relacionados (separados por vírgula): ");
                                String[] artistas = scan.nextLine().split(",\\s*");
        
                                Musica addNovaMusica = new Musica(nome, artista, data, duracao, artistas);
                                CRUDI.create(addNovaMusica, true, indice);

                                break;

                            case 3:

                                scan.nextLine();
                                clear();

                                System.out.print("Informe o nome do artista: ");
                                String banda = scan.nextLine();

                                List<Musica> lista = CRUDI.read(banda);

                                if(!lista.isEmpty()){

                                    for(Musica x : lista){

                                        System.out.println(x.toString());

                                    }

                                }else{

                                    System.out.println("Elementos não encontrados!");

                                }

                                break;

                            default:

                                clear();
                                System.out.println("Opção inválida!");
                                break;

                        }

                    }while(op != 0);

                }else{
    
                    System.out.println("Opção inválida!");
    
                }

            }

        }while(indice != 0);

    }

}