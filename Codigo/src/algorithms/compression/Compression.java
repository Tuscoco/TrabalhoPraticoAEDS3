package algorithms.compression;

import java.io.File;

import algorithms.compression.huffman.Huffman;
import algorithms.compression.lzw.LZW;
import repository.CRUDI;

public class Compression {

    private String diretorioDataBase = "data/database";
    private String diretorioComprimidos = "data/compressed";
    private int tipo;

    public Compression(int tipo){

        this.tipo = tipo;

    }

    public void comprimir(String arquivo){

        String diretorioArquivoOrigem = diretorioDataBase + "/" + arquivo;
        String texto = CRUDI.lerTudoComoTexto();

        if(tipo == 1){

            Huffman huffman = new Huffman();
            huffman.compress(texto);

        }else if(tipo == 2){

            LZW lzw = new LZW(diretorioArquivoOrigem);
            lzw.comprimir();

        }

    }
    
    /*
    * Método para executar a compressão de Huffman
    * 
    * Funcionamento:
    * -Lê as músicas do arquivo rockI.db
    * -Se não houver músicas, informa ao usuário
    * -Se houver, chama o método de compressão da classe Huffman
    * -Pergunta se o usuário deseja descomprimir o texto agora
    * -Se sim, lê o texto compactado e chama o método de descompressão da classe Huffman
    */
    /* 
    public static void executarCompressao() {

        Scanner scan = new Scanner(System.in);

        System.out.println("Lendo músicas do arquivo rockI.db...");

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

        scan.close();
    }
    */

    public File[] listarArquivos(boolean comprimirOuDescomprimir){

        File pasta;

        if(comprimirOuDescomprimir){

            pasta = new File(diretorioDataBase);
            File[] arquivos = pasta.listFiles();

            if(arquivos == null || arquivos.length == 0){

                System.out.println("Arquivos não encontrados!");
                return null;

            }

            return arquivos;

        }

        pasta = new File(diretorioComprimidos);
        File[] arquivos = pasta.listFiles();

        return arquivos;

    }

}
