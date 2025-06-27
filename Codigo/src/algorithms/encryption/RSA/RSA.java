package algorithms.encryption.RSA;

import java.io.RandomAccessFile;
import repository.CRUDI;

public class RSA {

    private String diretorio;

    private final int p = 61;
    private final int q = 53;
    private final int n = p * q; // 3233
    private final int z = (p - 1) * (q - 1); // 3120
    private final int e = 17;
    private final int d = 2753;

    public RSA(String diretorio) {
        this.diretorio = diretorio;
    }

    public void criptografar() {
        try {
            String str = CRUDI.lerTudoComoTexto(diretorio);
            int tam = str.length();

            RandomAccessFile file = new RandomAccessFile(
                "data/encrypted/arquivoCriptografadoRSA" + System.currentTimeMillis() + ".db",
                "rw"
            );

            // Para cada caractere, cifrar e salvar (armazenado como números separados por vírgula)
            for (int i = 0; i < tam; i++) {
                int P = (int) str.charAt(i);
                // Método "int" transforma o caractere em seu valor ASCII e o atribui a P
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

    public void descriptografar() {
        try {
            RandomAccessFile file = new RandomAccessFile(diretorio, "r");
            String[] mensagem = file.readLine().split(",+");
            file.close();

            StringBuilder descriptografada = new StringBuilder();

            // Para cada número cifrado, decifrar e converter em caractere
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

    // Método de exponenciação modular (evita overflow)
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
