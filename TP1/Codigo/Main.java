import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Scanner;

public class Main {

    public static void preencherCatalogo(){

        String arquivo = "CSV/rock5.csv";

        try{

            RandomAccessFile file = new RandomAccessFile(arquivo, "r");

            file.readLine();

            String linha;

            while((linha = file.readLine()) != null){

                String[] dados = linha.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);

                int index = Integer.parseInt(dados[0]);
                String name = dados[1];
                String artist = dados[2];
                String date = dados[3];
                double length = Double.parseDouble(dados[4]);
                String[] fArtist = new String[0];

                if(dados.length > 5){

                    String featuredArtists = dados[5];
                    if(featuredArtists.startsWith("\"")){

                        featuredArtists = featuredArtists.substring(1, featuredArtists.length() - 1);

                    }

                   fArtist = featuredArtists.split(",\\s*"); 

                }             

                Musica musica = new Musica(index, name, artist, date, length, fArtist);
                CRUD.create(musica, false);

            }

            file.close();

        }catch(IOException e){

            System.out.println("Erro: " + e.getMessage());

        } 

    }

    public static void menu(){

        System.out.println("===============================================================================");
        System.out.println("Escolha uma opção: ");
        System.out.println("1 -> Carregar base de dados no arquivo");
        System.out.println("2 -> Ler um registro");
        System.out.println("3 -> Atualizar registro");
        System.out.println("4 -> Deletar registro");
        System.out.println("0 -> Encerrar");
        System.out.println("===============================================================================");

    }

    public static void clear(){

        System.out.print("\033[H\033[2J");
        System.out.flush();

    }

    public static void main(String[] args) throws FileNotFoundException, IOException {
    
        Scanner scan = new Scanner(System.in);
    
        int op = -1;
        int id = -1;
    
        while(op != 0){
    
            menu();
    
            op = scan.nextInt();
    
            switch(op){
    
                case 1:
    
                    preencherCatalogo();
                    break;
                
                case 2:
    
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
    
                case 3:
    
                    //Atualizar registro
                    break;
    
                case 4:
    
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
    
                default:
                    
                    op = 0;
                    break;
    
            }
    
        }
    
        scan.close();
    
    }
    
}