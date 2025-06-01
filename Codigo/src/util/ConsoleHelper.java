package util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import model.Musica;
import repository.*;
import algorithms.compression.Compression;
import algorithms.sorting.IntercalacaoBalanceada;

public class ConsoleHelper {
    
    private static Scanner scan;

    private ConsoleHelper(){}

    /*
     * Método para a execução do programa
     */
    public static void run() throws FileNotFoundException, IOException{

        scan = new Scanner(System.in);

        int tipoDeCRUD;

        do{

            Menus.clear();
            Menus.tipoMenu();

            tipoDeCRUD = scan.nextInt();

            if(tipoDeCRUD != 0){

                if(tipoDeCRUD == 1){

                    Menus.clear();
                    runSequencial();
    
                }else if(tipoDeCRUD == 2){
    
                    Menus.clear();
                    runIndexado();
    
                }else if (tipoDeCRUD == 3) {

                    Menus.clear();
                    runCompressao();
                
                } else {
                    
                    Menus.clear();
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
    
            Menus.menu(1, 0);
    
            op = scan.nextInt();
    
            switch(op){
    
                case 0:

                    break;
                
                case 1:
    
                    Menus.clear();
                    CsvHandler.preencherCatalogo();
                    break;

                case 2:
                
                    scan.nextLine();
                    Menus.clear();
    
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
    
                    Menus.clear();
                    System.out.print("Informe o index da música procurada: ");
                    id = scan.nextInt();

                    Musica musica = CRUD.read(id);
    
                    Menus.clear();

    
                    if(musica == null){
    
                        System.out.println();
                        System.out.println("Registro não encontrado!");
                        System.out.println();
    
                    }else{
    
                        System.out.println(musica);
    
                    }
    
                    break;
    
                case 4:
    
                    Menus.clear();
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
    
                    Menus.clear();
                    System.out.print("Informe o index da música que deseja remover: ");
                    id = scan.nextInt();

                    Menus.clear();
                    
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

                    Menus.clear();
                    CRUD.read('D');
                    break;

                case 7:

                    Menus.clear();
                    System.out.print("Informe o número de caminhos: ");
                    int numCaminhos = scan.nextInt();

                    IntercalacaoBalanceada.ordenar(numCaminhos);
                    
                    break;
    
                case 8:

                    CRUD.read('O');

                    break;
                
                default:
                    
                    Menus.clear();
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

            Menus.clear();
            Menus.menuDeIndices();
            indice = scan.nextInt();
            Menus.clear();

            if(indice != 0){

                if(indice == 1 || indice == 2){

                    do{
        
                        Menus.menu(2, indice);
                
                        op = scan.nextInt();
                
                        switch(op){
                
                            case 0:
            
                                break;
                            
                            case 1:
                
                                Menus.clear();

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

                                Menus.clear();
                                CsvHandler.preencherCatalogoIndexado(indice, ordem);
                                break;
            
                            case 2:
                            
                                scan.nextLine();
                                Menus.clear();
                
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
                
                                Menus.clear();
                                System.out.print("Informe o index da música procurada: ");
                                id = scan.nextInt();
            
                                Musica musica = CRUDI.read(id, indice);
                
                                Menus.clear();
            
                
                                if(musica == null){
                
                                    System.out.println();
                                    System.out.println("Registro não encontrado!");
                                    System.out.println();
                
                                }else{
                
                                    System.out.println(musica);
                
                                }
                
                                break;
                
                            case 4:
                
                                Menus.clear();
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
                
                                Menus.clear();
                                System.out.print("Informe o index da música que deseja remover: ");
                                id = scan.nextInt();
            
                                Menus.clear();
                                
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
                                
                                Menus.clear();
                                System.out.println("Opção inválida!");
                                break;
                
                        }
                
                    }while(op != 0);
    
                }else if(indice == 3){

                    do{

                        Menus.menu(3, indice);
                
                        op = scan.nextInt();

                        switch(op){

                            case 0:

                                break;

                            case 1:

                                Menus.clear();
                                CsvHandler.preencherCatalogoIndexado(indice, 0);
                                break;

                            case 2:

                                scan.nextLine();
                                Menus.clear();
            
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
                                Menus.clear();

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

                                Menus.clear();
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

    private static void runCompressao(){

        int tipoCompressao = -1;
        int op = -1;

        do{

            Menus.menuDeCompressao();
            tipoCompressao = scan.nextInt();

            if(tipoCompressao == 1){

                //Huffman
                Compression compression = new Compression(tipoCompressao);

                do{

                    Menus.clear();
                    Menus.menuTipoCompressao(tipoCompressao);

                    op = scan.nextInt();
                    File[] arquivos;

                    switch(op){

                        case 0:
                            break;

                        case 1:

                            arquivos = compression.listarArquivos(true);
                            Menus.menuDeArquivos(op, arquivos, null);

                            int escolhidoParaComprimir = scan.nextInt();
                            String arquivoParaComprimir = "";

                            if(escolhidoParaComprimir >= 1 && escolhidoParaComprimir <= arquivos.length){

                                arquivoParaComprimir += arquivos[escolhidoParaComprimir - 1].getName();

                            }else{

                                System.out.println("Opção inválida!");
                                break;

                            }

                            compression.comprimir(arquivoParaComprimir);

                            break;
                        
                        case 2:

                            arquivos = compression.listarArquivos(false);
                            Menus.menuDeArquivos(op, arquivos, "Huffman");

                            int escolhidoParaDescomprimir = scan.nextInt();
                            String arquivoParaDescomprimir = "";

                            if(escolhidoParaDescomprimir >= 1 && escolhidoParaDescomprimir <= arquivos.length){

                                arquivoParaDescomprimir += arquivos[escolhidoParaDescomprimir - 1].getName();

                            }else{

                                System.out.println("Opção inválida!");
                                break;

                            }

                            compression.descomprimir(arquivoParaDescomprimir);

                            break;
                            
                        case 3:
                            break;
                        
                        default:
                            System.out.println("Opção inválida!");

                    }

                }while(op != 0);

            }else if(tipoCompressao == 2){

                //LZW
                Compression compression = new Compression(tipoCompressao);

                do{

                    Menus.clear();
                    Menus.menuTipoCompressao(tipoCompressao);

                    op = scan.nextInt();
                    File[] arquivos;

                    switch(op){

                        case 0:
                            break;

                        case 1:

                            arquivos = compression.listarArquivos(true);
                            Menus.menuDeArquivos(op, arquivos, null);

                            int escolhidoParaComprimir = scan.nextInt();
                            String arquivoParaComprimir = "";

                            if(escolhidoParaComprimir >= 1 && escolhidoParaComprimir <= arquivos.length){

                                arquivoParaComprimir += arquivos[escolhidoParaComprimir - 1].getName();

                            }else{

                                System.out.println("Opção inválida!");
                                break;

                            }

                            compression.comprimir(arquivoParaComprimir);

                            break;
                        
                        case 2:

                            arquivos = compression.listarArquivos(false);
                            Menus.menuDeArquivos(op, arquivos, "LZW");

                            int escolhidoParaDescomprimir = scan.nextInt();
                            String arquivoParaDescomprimir = "";

                            if(escolhidoParaDescomprimir >= 1 && escolhidoParaDescomprimir <= arquivos.length){

                                arquivoParaDescomprimir += arquivos[escolhidoParaDescomprimir - 1].getName();

                            }else{

                                System.out.println("Opção inválida!");
                                break;

                            }

                            compression.descomprimir(arquivoParaDescomprimir);

                            break;
                            
                        case 3:
                            break;
                        
                        default:
                            System.out.println("Opção inválida!");

                    }

                }while(op != 0);

            }else if(tipoCompressao != 0 && (tipoCompressao < 1 || tipoCompressao > 2)){

                Menus.clear();
                System.out.println("Opção inválida!");

            }

        }while(tipoCompressao != 0);

    }

}