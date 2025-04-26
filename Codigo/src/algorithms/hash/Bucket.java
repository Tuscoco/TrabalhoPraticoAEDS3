package algorithms.hash;

import model.Registro;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Bucket {
    public List<Registro> registros;

    public Bucket(int maxRegistros) {
        this.registros = new ArrayList<>(maxRegistros);
    }

    public boolean inserir(Registro r) {
        registros.add(r);
        return true;
    }

    public Registro buscar(int id) {
        for (Registro r : registros) {
            if (r != null && r.id == id) {
                return r;
            }
        }
        return null;
    }

    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        dos.writeInt(registros.size());

        for (Registro r : registros) {
            dos.writeInt(r.id);
            dos.writeLong(r.end);
        }

        return baos.toByteArray();
    }

    public void fromByteArray(byte[] array) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(array);
        DataInputStream dis = new DataInputStream(bais);

        int size = dis.readInt();
        registros = new ArrayList<>(size);

        for (int i = 0; i < size; i++) {
            Registro r = new Registro();
            r.id = dis.readInt();
            r.end = dis.readLong();
            registros.add(r);
        }
    }
}