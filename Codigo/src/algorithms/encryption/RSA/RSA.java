package algorithms.encryption.RSA;

import java.io.RandomAccessFile;
import repository.CRUDI;

/**
 * Classe que implementa a criptografia e descriptografia usando o algoritmo RSA.
 */
public class RSA {

    private String diretorio;

    /*
     * Inicialização das chaves RSA.
     */
    private final int p = 61;
    private final int q = 53;
    private final int n = p * q; // 3233
    private final int z = (p - 1) * (q - 1); // 3120
    private final int e = 17;
    private final int d = 2753;

    public RSA(String diretorio) {
        this.diretorio = diretorio;
    }

    /**
     * Criptografa o arquivo utilizando o algoritmo RSA e salva o resultado em um novo arquivo.
     * Para cada caractere do arquivo original, converte para o código ASCII, cifra usando a chave pública e salva como números separados por vírgula.
     */
    public void criptografar() {
        try {
            String str = CRUDI.lerTudoComoTexto(diretorio);
            int tam = str.length();

            RandomAccessFile file = new RandomAccessFile(
                "data/encrypted/arquivoCriptografadoRSA" + System.currentTimeMillis() + ".db",
                "rw"
            );

            for (int i = 0; i < tam; i++) {
                int P = (int) str.charAt(i);
                // C = P^e mod n
                int C = modPow(P, e, n);
                file.writeBytes(String.valueOf(C));
                if (i < tam - 1) {
                    file.writeBytes(",");
                }
            }

            file.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Descriptografa o arquivo criptografado e salva o resultado em um novo arquivo.
     * Para cada número cifrado, decifra usando a chave privada e converte de volta para caractere.
     */
    public void descriptografar() {
        try {
            RandomAccessFile file = new RandomAccessFile(diretorio, "r");
            String[] mensagem = file.readLine().split(",+");
            file.close();

            StringBuilder descriptografada = new StringBuilder();

            for (String numStr : mensagem) {
                int C = Integer.parseInt(numStr.trim());
                // P = C^d mod n
                int P = modPow(C, d, n);
                descriptografada.append((char) P);
            }

            RandomAccessFile fileD = new RandomAccessFile(
                "data/decrypted/arquivoDescriptografadoRSA" + System.currentTimeMillis() + ".db",
                "rw"
            );
            fileD.write(descriptografada.toString().getBytes());
            fileD.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Método auxiliar para calcular a exponenciação modular.
     */
    private int modPow(int base, int exp, int mod) {
        long result = 1;
        long b = base;
        while (exp > 0) {
            if ((exp & 1) == 1) {
                result = (result * b) % mod;
            }
            b = (b * b) % mod;
            exp >>= 1;
        }
        return (int) result;
    }
}
