import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Scanner;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Main {

//////////////////////////////////////////////////////AUXILIARES//////////////////////////////////////////////////////

    public static void preencherCatalogo(){

        String arquivo = "CSV/rock5.csv";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        try{

            RandomAccessFile file = new RandomAccessFile(arquivo, "r");

            file.readLine();

            String linha;

            while((linha = file.readLine()) != null){

                String[] dados = linha.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);

                String name = dados[1];
                String artist = String.format("%-" + 15 + "s", dados[2]);
                long date = 0;

                try{
                    Date parsedDate = dateFormat.parse(dados[3]);
                    date = parsedDate.getTime();

                } catch (ParseException e){

                    System.out.println("Erro: " + e.getMessage());
                }

                double length = Double.parseDouble(dados[4]);
                String[] fArtist = new String[0];

                if(dados.length > 5){

                    String featuredArtists = dados[5];
                    if(featuredArtists.startsWith("\"")){

                        featuredArtists = featuredArtists.substring(1, featuredArtists.length() - 1);

                    }

                   fArtist = featuredArtists.split(",\\s*"); 

                }             

                Musica musica = new Musica(name, artist, date, length, fArtist);

                CRUD.create(musica, false);

            }

            file.close();

        }catch(IOException e){

            System.out.println("Erro: " + e.getMessage());

        } 

    }

    public static void menu(){

        System.out.println("==============BEM=VINDO=AO=DB=DO=ROCK=N=ROLL=============");
        System.out.println("Escolha uma opção: ");
        System.out.println("1 -> Carregar base de dados no arquivo");
        System.out.println("2 -> Criar um registro");
        System.out.println("3 -> Ler um registro");
        System.out.println("4 -> Atualizar registro");
        System.out.println("5 -> Deletar registro");
        System.out.println("6 -> Ler todos os registros");
        System.out.println("0 -> Encerrar");
        System.out.println("===============================================================================");

    }

    public static void clear(){

        System.out.print("\033[H\033[2J");
        System.out.flush();

    }

//////////////////////////////////////////////////////MAIN//////////////////////////////////////////////////////

    public static void main(String[] args) throws FileNotFoundException, IOException {
    
        Scanner scan = new Scanner(System.in);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy"); //reconhece o formato que a data será lida e exibida ao usuário para utilizar nas transformações para Timestamp
    
        int op = -1;
        int id = -1;
    
        while(op != 0){
    
            menu();
    
            op = scan.nextInt();
    
            switch(op){
    
                case 0:

                    break;
                
                case 1:
    
                    preencherCatalogo();
                    break;

                case 2:
                
                    scan.nextLine();
    
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
                    } catch (ParseException e){

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
    
                    System.out.print("Informe o index da música procurada: ");
                    id = scan.nextInt();

                    Musica musica = CRUD.read(id);
    
                    clear();

    
                    if(musica == null){
    
                        System.out.println("Registro não encontrado!");
                        System.out.println();
    
                    }else{
    
                        System.out.println(musica);
    
                    }
    
                    break;
    
                case 4:
    
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
                    } catch (ParseException e){

                        System.out.println("Erro: " + e.getMessage());
                    }

                    System.out.print("Nova duração: ");
                    double length = scan.nextDouble();
                    scan.nextLine();
                    
                    System.out.print("Novos artistas relacionados (separados por vírgula): ");
                    String[] fArtists = scan.nextLine().split(",\s*");

                    Musica novaMusica = new Musica(id, name, artist, date, length, fArtists);

                    if(CRUD.update(id, novaMusica)){

                        System.out.println("Registro atualizado com sucesso!");

                    }else{

                        System.out.println("Erro ao atualizar o registro!");

                    }
                    
                    break;
    
                case 5:
    
                    System.out.print("Informe o index da música que deseja remover: ");
                    id = scan.nextInt();

                    clear();
                    
                    if(CRUD.delete(id)){

                        System.out.println("Música removida com sucesso!");

                    }else{

                        System.out.println("Erro ao remover a música!");

                    }

                    System.out.println();

                    break;

                case 6:

                    CRUD.read();
                    break;
    
                default:
                    
                    op = 0;
                    break;
    
            }
    
        }
    
        scan.close();
    
    }
    
}