import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.*;

public class Main {

    public static void preencherCatalogo(){

        String arquivo = "rock5.csv";

        try{

            RandomAccessFile file = new RandomAccessFile(arquivo, "r");

            file.readLine();

            String linha;

            while((linha = file.readLine()) != null){

                String[] dados = linha.split(",");

                int index = Integer.parseInt(dados[0]);
                String name = dados[1];
                String artist = dados[2];
                String date = dados[3];
                double length = Double.parseDouble(dados[4]);
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

        System.out.println();
        System.out.println("===============================================================================");
        System.out.println("Escolha uma opção: ");
        System.out.println("1 -> Carregar base de dados no arquivo");
        System.out.println("2 -> Ler um registro");
        System.out.println("3 -> Atualizar registro");
        System.out.println("4 -> Deletar registro");
        System.out.println("0 -> Encerrar");
        System.out.println("===============================================================================");
        System.out.println();

    }

    public static void clear(){

        for(int i = 0;i < 15;i++){

            System.out.println();

        }

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

                    System.out.print("Informe o index da música procurada: ");
                    int id = scan.nextInt();

                    Musica musica = CRUD.buscarMusica(id);

                    clear();

                    if(musica == null){

                        System.out.println("Registro não encontrado!");

                    }else{

                        System.out.println(musica);

                    }

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