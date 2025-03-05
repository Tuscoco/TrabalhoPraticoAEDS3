package algorithms.sorting;
import java.io.*;
import java.util.*;
import java.nio.ByteBuffer;

public class IntercalacaoBalanceada {
    private static int M = 4; // Número de registros por bloco (tamanho do bloco)
    private static int N = 2; // Número de fitas

    public static void reordenarArquivo(int novoM, int novoN, String arquivo) throws IOException {
        M = novoM;
        N = novoN;
        ordenarArquivo(arquivo);
    }

    private static void ordenarArquivo(String arquivo) throws IOException {
        dividirEmFitas(arquivo);  // Dividindo os dados nas fitas
        intercalar(arquivo);  // Intercalando os dados ordenados
    }

    private static void dividirEmFitas(String arquivo) throws IOException {
        List<BufferedWriter> fitas = new ArrayList<>();
        
        // Criando as fitas temporárias
        for (int i = 0; i < N; i++) {
            fitas.add(new BufferedWriter(new FileWriter("fita" + i + ".txt")));
        }

        List<List<Integer>> blocos = lerBlocos(arquivo);  // Lê os blocos de registros
        int fitaAtual = 0;

        // Escreve os blocos ordenados nas fitas temporárias
        for (List<Integer> bloco : blocos) {
            Collections.sort(bloco);  // Ordena o bloco antes de escrever
            for (int index : bloco) {
                fitas.get(fitaAtual).write(index + "\n");
            }

            // Alterna entre as fitas para balanceamento
            fitaAtual = (fitaAtual + 1) % N;
        }

        // Fecha as fitas temporárias
        for (BufferedWriter writer : fitas) {
            writer.close();
        }
    }

    private static void intercalar(String arquivo) throws IOException {
        PriorityQueue<ElementoFita> heap = new PriorityQueue<>();
        List<BufferedReader> fitas = new ArrayList<>();
        BufferedWriter writer = new BufferedWriter(new FileWriter(arquivo));

        // Abrindo as fitas temporárias
        for (int i = 0; i < N; i++) {
            BufferedReader br = new BufferedReader(new FileReader("fita" + i + ".txt"));
            fitas.add(br);
            String linha = br.readLine();
            if (linha != null) {
                heap.add(new ElementoFita(Integer.parseInt(linha), i));  // Adiciona o primeiro índice de cada fita no heap
            }
        }

        // Intercalando as fitas
        while (!heap.isEmpty()) {
            ElementoFita menor = heap.poll();
            writer.write(menor.valor + "\n");

            String linha = fitas.get(menor.fita).readLine();
            if (linha != null) {
                heap.add(new ElementoFita(Integer.parseInt(linha), menor.fita));  // Adiciona o próximo índice da fita
            }
        }

        // Fecha as fitas temporárias
        for (BufferedReader br : fitas) {
            br.close();
        }
        writer.close();
    }

    // Lê os blocos de índices do arquivo e retorna uma lista de blocos de tamanho M
    private static List<List<Integer>> lerBlocos(String arquivo) throws IOException {
        List<List<Integer>> blocos = new ArrayList<>();

        try (RandomAccessFile file = new RandomAccessFile(arquivo, "r")) {
            file.readInt();  // Lê o índice inicial (não usado diretamente)

            List<Integer> bloco = new ArrayList<>();
            while (file.getFilePointer() < file.length()) {
                // Lê o "lápide" que indica se o registro foi apagado
                boolean lapide = file.readBoolean();
                // Lê o tamanho do array de bytes
                int tamanho = file.readInt();
                // Lê os bytes
                byte[] array = new byte[tamanho];
                file.readFully(array);
                // Extrai o índice do array de bytes (ajuste conforme a estrutura dos dados)
                int index = ByteBuffer.wrap(array).getInt(0);
                if (!lapide) {  // Ignora registros apagados
                    bloco.add(index);
                }

                // Quando o bloco atingir o tamanho M, finaliza e cria um novo bloco
                if (bloco.size() == M) {
                    blocos.add(new ArrayList<>(bloco));
                    bloco.clear();
                }
            }

            // Se houver um último bloco que não atinja M, adiciona ao final
            if (!bloco.isEmpty()) {
                blocos.add(bloco);
            }
        }
        return blocos;
    }

    // Classe auxiliar para comparar os elementos das fitas
    static class ElementoFita implements Comparable<ElementoFita> {
        int valor;
        int fita;

        ElementoFita(int valor, int fita) {
            this.valor = valor;
            this.fita = fita;
        }

        @Override
        public int compareTo(ElementoFita o) {
            return Integer.compare(this.valor, o.valor);
        }
    }
}
