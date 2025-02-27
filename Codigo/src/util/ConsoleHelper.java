package util;

public class ConsoleHelper {
    
    private ConsoleHelper(){}

    /*
     * Método para imprimir o menu de escolhas do usuário
     * 
     * Funcionamento: 
     * -Imprime na tela todas as opções de ações do usuário
     */
    public static void menu(){

        System.out.println("=========================BEM=VINDO=AO=DB=DO=ROCK=N=ROLL=========================");
        System.out.println("Escolha uma opção: ");
        System.out.println("1 -> Carregar base de dados no arquivo");
        System.out.println("2 -> Criar um registro");
        System.out.println("3 -> Ler um registro");
        System.out.println("4 -> Atualizar registro");
        System.out.println("5 -> Deletar registro");
        System.out.println("6 -> Ler todos os registros");
        System.out.println("0 -> Encerrar");
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

}
