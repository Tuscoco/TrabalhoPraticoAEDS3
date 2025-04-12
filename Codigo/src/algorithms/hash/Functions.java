package algorithms.hash;

import java.io.IOException;
import java.util.Scanner;

import model.Musica;
import model.Registro;

public class Functions {

    private HashExtensivel hashExtensivel;

    public Functions(int capacidadeBucket, int profundidadeInicial) throws IOException {
        // Inicializa o Hash Extensível com os parâmetros fornecidos
        this.hashExtensivel = new HashExtensivel(capacidadeBucket, profundidadeInicial);
    }

    public void menuHash() throws IOException {
        Scanner scanner = new Scanner(System.in);
        int opcao;

        do {
            System.out.println("Escolha uma operação com o Hash Extensível:");
            System.out.println("1 -> Adicionar registro");
            System.out.println("2 -> Listar registros");
            System.out.println("3 -> Remover registro");
            System.out.println("0 -> Voltar");
            System.out.print("Opção: ");
            opcao = scanner.nextInt();
            scanner.nextLine(); // Consumir a nova linha

            switch (opcao) {
                case 1: // Adicionar registro
                    System.out.print("Informe o ID do registro: ");
                    int id = scanner.nextInt();
                    scanner.nextLine(); // Consumir a nova linha
                    System.out.print("Informe o nome da música: ");
                    String nome = scanner.nextLine();
                    System.out.print("Informe o artista: ");
                    String artista = scanner.nextLine();
                    System.out.print("Informe a duração: ");
                    double duracao = scanner.nextDouble();
                    scanner.nextLine(); // Consumir a nova linha
                    System.out.print("Informe os artistas relacionados (separados por vírgula): ");
                    String[] artistas = scanner.nextLine().split(",\\s*");

                    Musica novaMusica = new Musica(nome, artista, System.currentTimeMillis(), duracao, artistas);
                    Registro registro = new Registro(id, end);
                    hashExtensivel.inserir(registro);
                    System.out.println("Registro adicionado com sucesso!");
                    break;

                case 2: // Listar registros
                    System.out.println("Listando todos os registros no Hash Extensível:");
                    hashExtensivel.listarTodos();
                    break;

                case 3: // Remover registro
                    System.out.print("Informe o ID do registro a ser removido: ");
                    int idRemover = scanner.nextInt();
                    boolean removido = hashExtensivel.remover(idRemover);
                    if (removido) {
                        System.out.println("Registro removido com sucesso!");
                    } else {
                        System.out.println("Registro não encontrado.");
                    }
                    break;

                case 0: // Voltar
                    System.out.println("Voltando ao menu anterior...");
                    break;

                default:
                    System.out.println("Opção inválida!");
            }
        } while (opcao != 0);
    }
}
