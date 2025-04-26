package algorithms.hash;

import java.io.*;
import java.util.Arrays;

public class Diretorio {
    public int profundidadeGlobal;
    public long[] enderecosBuckets;

    public Diretorio(int profundidadeGlobal) {
        this.profundidadeGlobal = profundidadeGlobal;
        this.enderecosBuckets = new long[1 << profundidadeGlobal];
        Arrays.fill(enderecosBuckets, -1);
    }

    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        dos.writeInt(profundidadeGlobal);
        dos.writeInt(enderecosBuckets.length);

        for (long end : enderecosBuckets) {
            dos.writeLong(end);
        }

        return baos.toByteArray();
    }

    public void fromByteArray(byte[] array) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(array);
        DataInputStream dis = new DataInputStream(bais);

        profundidadeGlobal = dis.readInt();
        int tamanho = dis.readInt();
        enderecosBuckets = new long[tamanho];

        for (int i = 0; i < tamanho; i++) {
            enderecosBuckets[i] = dis.readLong();
        }
    }
}