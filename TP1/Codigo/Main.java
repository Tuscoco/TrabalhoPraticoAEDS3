import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.*;

public class Main {

    public static void preencherCatalogo(){

        String arquivo = "rock.csv";

        try{

            RandomAccessFile file = new RandomAccessFile(arquivo, "r");
            //Charset charset = Charset.forName("UTF-8");

            file.readLine();

            String linha = "";

            while((linha = file.readLine()) != null){

                String[] dados = linha.split(",");

                int index = Integer.parseInt(dados[0]);
                String name = dados[1];
                String artist = dados[2];
                String date = dados[3];
                //float length = Float.parseFloat(dados[4]);
                float length = 2.00f;
                String fArtist = "TESTE";

                Musica musica = new Musica(index, name, artist, date, length, fArtist);

                CRUD.criarMusica(musica);

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

    public static void main(String[] args) throws FileNotFoundException, IOException {

        Scanner scan = new Scanner(System.in);

        int op = 1;

        while(op != 0){

            menu();

            op = scan.nextInt();

            switch(op){

                case 1:

                    preencherCatalogo();
                    break;
                
                case 2:

                    //Ler registro
                    break;

                case 3:

                    //Atualizar registro
                    break;

                case 4:

                    //Deletar registro
                    break;

                default:
                    
                    op = 0;
                    break;

            }

        }

        scan.close();

    }
    
}