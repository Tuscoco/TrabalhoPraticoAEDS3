package algorithms.compression.huffman;

import java.io.*;
import java.util.*;
import repository.CRUDI;

public class Huffman {

    private Map<Character, String> huffmanCodes = new HashMap<>();
    private Node root;
    private String diretorio;

    public Huffman(String diretorio) {
        this.diretorio = diretorio;
    }

    /**
     * Comprime o arquivo usando o algoritmo de Huffman.
     * Lê o conteúdo do arquivo, constrói a árvore de Huffman, gera os códigos e grava o resultado em um novo arquivo.
     */
    public void compress() {
        try {
            String text = CRUDI.lerTudoComoTexto(diretorio);

            Map<Character, Integer> freqMap = new HashMap<>();
            for (char c : text.toCharArray()) {
                freqMap.put(c, freqMap.getOrDefault(c, 0) + 1);
            }

            PriorityQueue<Node> pq = new PriorityQueue<>();
            for (var entry : freqMap.entrySet()) {
                pq.add(new Node(entry.getKey(), entry.getValue()));
            }

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

            long timestamp = System.currentTimeMillis();
            String nomeBase = "arquivoComprimidoHuffman" + timestamp;

            RandomAccessFile arquivoDestino = new RandomAccessFile("data/compressed/" + nomeBase + ".db", "rw");
            arquivoDestino.write(encoded.toString().getBytes());
            arquivoDestino.close();

            // Grava os códigos da árvore em .txt
            PrintWriter writer = new PrintWriter("data/compressed/" + nomeBase + ".txt");
            for (Map.Entry<Character, String> entry : huffmanCodes.entrySet()) {
                writer.println((int) entry.getKey() + ":" + entry.getValue()); // salvando código ASCII
            }
            writer.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void buildCodeMap(Node node, String code) {
        if (node.isLeaf()) {
            huffmanCodes.put(node.character, code);
        } else {
            buildCodeMap(node.left, code + "0");
            buildCodeMap(node.right, code + "1");
        }
    }

    /**
     * Descomprime o arquivo usando o algoritmo de Huffman.
     * Lê o texto binário do arquivo comprimido, reconstrói a árvore de Huffman e decodifica o texto.
     * O resultado é gravado em um novo arquivo.
     */
    public void decompress() {
        try {

            // Lê texto binário
            RandomAccessFile arquivoOrigem = new RandomAccessFile(diretorio, "r");
            byte[] msgCodificada = new byte[(int) arquivoOrigem.length()];
            arquivoOrigem.readFully(msgCodificada); 
            arquivoOrigem.close();
            String binaryText = new String(msgCodificada);

            // Lê os códigos da árvore
            Map<Character, String> codigos = new HashMap<>();
            Scanner leitor = new Scanner(new File(diretorio));
            while (leitor.hasNextLine()) {
                String linha = leitor.nextLine();
                String[] partes = linha.split(":");
                if (partes.length == 2) {
                    char caractere = (char) Integer.parseInt(partes[0]);
                    String codigo = partes[1];
                    codigos.put(caractere, codigo);
                }
            }
            leitor.close();

            reconstruirArvore(codigos);

            // Decodifica
            StringBuilder decoded = new StringBuilder();
            Node current = root;
            for (char bit : binaryText.toCharArray()) {
                current = (bit == '0') ? current.left : current.right;
                if (current.isLeaf()) {
                    decoded.append(current.character);
                    current = root;
                }
            }

            // Escreve resultado
            String saida = "data/descompressed/arquivoDescomprimidoHuffman" + System.currentTimeMillis() + ".db";
            RandomAccessFile arquivoDestino = new RandomAccessFile(saida, "rw");
            arquivoDestino.write(decoded.toString().getBytes());
            arquivoDestino.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Reconstrói árvore Huffman com base nos códigos
    private void reconstruirArvore(Map<Character, String> codigos) {
        root = new Node('\0', 0);
        for (Map.Entry<Character, String> entry : codigos.entrySet()) {
            char caractere = entry.getKey();
            String codigo = entry.getValue();
            Node atual = root;
            for (char bit : codigo.toCharArray()) {
                if (bit == '0') {
                    if (atual.left == null) atual.left = new Node('\0', 0);
                    atual = atual.left;
                } else {
                    if (atual.right == null) atual.right = new Node('\0', 0);
                    atual = atual.right;
                }
            }
            atual.character = caractere;
        }
    }
}
