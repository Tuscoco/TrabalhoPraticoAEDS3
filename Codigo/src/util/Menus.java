package util;

import java.io.File;
import java.util.List;

public class Menus {
    
    /*
     * Método para imprimir o menu de escolhas do usuário
     */
    public static void tipoMenu(){

        System.out.println("=========================BEM=VINDO=AO=DB=DO=ROCK=N=ROLL=========================");
        System.out.println("Escolha o que será feito:");
        System.out.println("1 -> CRUD Sequencial");
        System.out.println("2 -> CRUD Indexado");
        System.out.println("3 -> Compressão");
        System.out.println("4 -> Casamento de Padrões");
        System.out.println("0 -> Encerrar");
        System.out.println("================================================================================");

    }

    /*
     * Método para imprimir o menu de escolhas do usuário
     */
    public static void menu(int tipo, int indice){

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

            String tipoIndice = "";

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
    public static void menuDeIndices(){

        System.out.println("================================================================================");
        System.out.println("Escolha o tipo de índice que será utilizado:");
        System.out.println("1 -> Árvore B");
        System.out.println("2 -> Tabela Hash Extensível");
        System.out.println("3 -> Lista Invertida");
        System.out.println("0 -> Voltar");
        System.out.println("================================================================================");

    }

    public static void menuDeCompressao(){

        System.out.println("================================================================================");
        System.out.println("Escolha um dos algoritmos de compressão:");
        System.out.println("1 - Huffman");
        System.out.println("2 - LZW");
        System.out.println("0 - Voltar");
        System.out.println("================================================================================");

    }

    public static void menuTipoCompressao(int tipo){

        String tipoCompressao = "";

        if(tipo == 1){
                        
            tipoCompressao = "====================================Huffman=====================================";

        }else if(tipo == 2){

            tipoCompressao = "======================================LZW=======================================";
            

        }

        System.out.println("================================================================================");
        System.out.println(tipoCompressao);
        System.out.println("Informe o que deseja fazer:");
        System.out.println("1 - Comprimir");
        System.out.println("2 - Descomprimir");
        System.out.println("3 - Ler arquivo descomprimido");
        System.out.println("0 - Voltar");
        System.out.println("================================================================================");

    }

    public static void menuCasamentoDePadroes(){

        System.out.println("================================================================================");
        System.out.println("Escolha um dos algoritmos de casamento de padrões:");
        System.out.println("1 - KMP");
        System.out.println("2 - Boyer Moore");
        System.out.println("0 - Voltar");
        System.out.println("================================================================================");

    }

    public static void menuTipoCasamento(int tipo){

        String tipoCompressao = "";

        if(tipo == 1){
                        
            tipoCompressao = "======================================KMP=======================================";

        }else if(tipo == 2){

            tipoCompressao = "==================================Boyer-Moore===================================";
            

        }

        System.out.println("================================================================================");
        System.out.println(tipoCompressao);
        System.out.println("Informe o que deseja fazer:");
        System.out.println("1 - Procurar um padrão por todo o arquivo");
        System.out.println("0 - Voltar");
        System.out.println("================================================================================");

    }

    /*
     * Método "limpar" o terminal
     * 
     * Funcionamento: 
     * -Imprime várias linhas em branco em sequencia para "limpar" o terminal
     */
    public static void clear(){

        System.out.print("\033[H\033[2J");
        System.out.flush();

    }

    public static void menuDeArquivos(int op, List<File> arquivos){

        System.out.println("================================================================================");

        if(arquivos == null || arquivos.size() == 0){

            System.out.println("Nenhum arquivo encontrado!");
            return;

        }

        System.out.println("Informe uma opção:");

        for(int i = 0;i < arquivos.size();i++){

            System.out.println((i + 1) + " - " + arquivos.get(i).getName());
    
        }

        System.out.println("0 - Voltar");

        System.out.println("================================================================================");

    }

}
