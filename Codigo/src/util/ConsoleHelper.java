package util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.io.File;

import model.Musica;
import repository.CRUD;
import repository.CsvHandler;
import algorithms.sorting.IntercalacaoBalanceada;

public class ConsoleHelper {
    
    public ConsoleHelper(){}

    /*
     * Método para imprimir o menu de escolhas do usuário
     * 
     * Funcionamento: 
     * -Imprime na tela todas as opções de ações do usuário
     */
    private void menu(){

        System.out.println("=========================BEM=VINDO=AO=DB=DO=ROCK=N=ROLL=========================");
        System.out.println("Escolha uma opção: ");
        System.out.println("1 -> Carregar base de dados no arquivo");
        System.out.println("2 -> Criar um registro");
        System.out.println("3 -> Ler um registro");
        System.out.println("4 -> Atualizar registro");
        System.out.println("5 -> Deletar registro");
        System.out.println("6 -> Ler todos os registros");
        System.out.println("7 -> Ordenar arquivo");
        System.out.println("8 -> Ler arquivo ordenado");
        System.out.println("0 -> Encerrar");
        System.out.println("================================================================================");

    }

    /*
     * Método "limpar" o terminal
     * 
     * Funcionamento: 
     * -Imprime várias linhas em branco em sequencia para "limpar" o terminal
     */
    private void clear(){

        System.out.print("\033[H\033[2J");
        System.out.flush();

    }

    /*
     * Método para a interação com o usuário
     * 
     * Funcionamento: 
     * -Chama o método menu e recebe a opção do usuário
     * -Recebe informações do usuário e chama o método necessário pra cada caso
     */
    public void run() throws FileNotFoundException, IOException{

        Scanner scan = new Scanner(System.in);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy"); //Reconhece o formato que a data será lida e exibida ao usuário para utilizar nas transformações para Timestamp
    
        int op = -1;
        int id = -1;
    
        while(op != 0){
    
            menu();
    
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
                    String[] artistas = scan.nextLine().split(",\s*");

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
                    String[] fArtists = scan.nextLine().split(",\s*");

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
                    String arquivoInicial = "data/database/rock.db";
                    String arquivoFinal = "data/database/final.db";
                    String arquivoTemp = "data/database/temp.db";

                    //Partimos de um arquivo não ordenado e chamamos a ordenação de forma iterativa para que, a cada iteração, o arquivo vá trazendo os registros para uma organização cada vez mais ordenada. 
                    boolean ordenado = false;

                    //Optamos por deixar 100 elementos no bloco (fixo) para não acontecer de o usuário colocar poucos elementos e a ordenação rodar por muito tempo
                    //Fizemos testes com 4 elementos e a ordenação rodou por mais de 5 minutos
                    while (!ordenado) {
                        IntercalacaoBalanceada.ordenar(arquivoInicial, numCaminhos, 100, arquivoFinal);
                        ordenado = IntercalacaoBalanceada.isSorted(arquivoFinal);

                        if (!ordenado) {
                            // Se não estiver ordenado, o arquivo final se torna o inicial para a próxima iteração
                            arquivoInicial = arquivoFinal;
                            // Cria um novo arquivo temporário para a próxima iteração
                            arquivoFinal = arquivoTemp;
                            arquivoTemp = "data/database/temp" + System.currentTimeMillis() + ".db";
                        }
                    }

                    // Se o arquivo final estiver ordenado, renomeia para o arquivo final desejado
                    File finalFile = new File(arquivoFinal);
                    File desiredFinalFile = new File("data/database/final.db");
                    if (desiredFinalFile.exists()) {
                        desiredFinalFile.delete();
                    }
                    finalFile.renameTo(desiredFinalFile);

                    System.out.println("Arquivo ordenado com sucesso!");
                    
                    break;
    
                case 8:

                    CRUD.read('O');

                    break;
                
                    default:
                    
                    clear();
                    op = 0;
                    break;
    
            }
    
        }
    
        scan.close();

    }

}