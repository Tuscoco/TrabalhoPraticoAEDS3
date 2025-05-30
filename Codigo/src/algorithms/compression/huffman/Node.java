package algorithms.compression.huffman;

public class Node implements Comparable<Node> {
    char character;
    int frequency;
    Node left, right;

    public Node(char character, int frequency) {
        this.character = character;
        this.frequency = frequency;
        this.left = this.right = null;
    }

    public Node(int frequency, Node left, Node right) {
        this.character = '\0'; // marcador para nó interno
        this.frequency = frequency;
        this.left = left;
        this.right = right;
    }

    @Override
    public int compareTo(Node other) {
        return this.frequency - other.frequency;
    }

    public boolean isLeaf() {
        return (left == null && right == null);
    }
}
