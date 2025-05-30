package algorithms.compression.huffman;
import java.util.*;

/*
 * Algoritmo de compressão Huffman
 */
public class Huffman {
    private Map<Character, String> huffmanCodes = new HashMap<>();
    private Node root;

    public void compress(String text) {
        Map<Character, Integer> freqMap = new HashMap<>();

        for (char c : text.toCharArray()) {
            freqMap.put(c, freqMap.getOrDefault(c, 0) + 1);
        }

        PriorityQueue<Node> pq = new PriorityQueue<>();
        for (var entry : freqMap.entrySet()) {
            pq.add(new Node(entry.getKey(), entry.getValue()));
        }

        /*
         * Constrói a árvore de Huffman
         * Enquanto houver mais de um nó na fila de prioridade, combine os dois nós com menor frequência
         * em um novo nó pai e adicione-o de volta à fila. Quando restar apenas um nó, este será a raiz da árvore de Huffman.
         */
        while (pq.size() > 1) {
            Node left = pq.poll();
            Node right = pq.poll();
            Node parent = new Node(left.frequency + right.frequency, left, right);
            pq.add(parent);
        }

        root = pq.poll();
        buildCodeMap(root, "");

        StringBuilder encoded = new StringBuilder();
        for (char c : text.toCharArray()) {
            encoded.append(huffmanCodes.get(c));
        }

        System.out.println("Texto compactado (binário): " + encoded);
        System.out.println("Tabela de códigos Huffman:");
        for (var entry : huffmanCodes.entrySet()) {
            System.out.println("'" + entry.getKey() + "': " + entry.getValue());
        }
    }

    /*
     * Constrói o mapa de códigos Huffman a partir da árvore. Cada folha da árvore representa um caractere e o caminho para essa folha
     * representa o código Huffman correspondente.
     */
    private void buildCodeMap(Node node, String code) {
        if (node.isLeaf()) {
            huffmanCodes.put(node.character, code);
        } else {
            buildCodeMap(node.left, code + "0");
            buildCodeMap(node.right, code + "1");
        }
    }

    public void decompress(String binaryText) {
        if (root == null) {
            System.out.println("Erro: nenhuma árvore de Huffman construída ainda.");
            return;
        }

        StringBuilder decoded = new StringBuilder();
        Node current = root;
        for (char bit : binaryText.toCharArray()) {
            current = (bit == '0') ? current.left : current.right;
            if (current.isLeaf()) {
                decoded.append(current.character);
                current = root;
            }
        }

        System.out.println("Texto descompactado: " + decoded.toString());
    }
}
