package algorithms.hash;

import model.Registro;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Bucket {
    public int profundidadeLocal;
    public List<Registro> registros;
    public int maxRegistros;

    public Bucket(int maxRegistros) {
        this.profundidadeLocal = 1;
        this.registros = new ArrayList<>();
        this.maxRegistros = maxRegistros;
    }

    public boolean isFull() {
        return registros.size() >= maxRegistros;
    }

    public boolean inserir(Registro r) {
        if (!isFull()) {
            registros.add(r);
            return true;
        }
        return false;
    }

    public Registro buscar(int id) {
        if (registros == null) {
            System.err.println("Lista de registros não inicializada");
            return null;
        }
    
        for (Registro r : registros) {
            if (r != null && r.id == id) {
                System.out.println("Registro encontrado no bucket - ID: " + r.id + ", End: " + r.end);
                return r;
            }
        }
        
        System.err.println("Registro com ID " + id + " não encontrado neste bucket");
        return null;
    }

    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        dos.writeInt(profundidadeLocal);
        dos.writeInt(maxRegistros);
        dos.writeInt(registros.size());

        for (Registro r : registros) {
            dos.writeInt(r.id);
            dos.writeLong(r.end);
        }

        return baos.toByteArray();
    }

    public void fromByteArray(byte[] array) throws IOException {
        if (array == null || array.length < 12) {
            throw new IOException("Dados inválidos para o bucket");
        }
    
        ByteArrayInputStream bais = new ByteArrayInputStream(array);
        DataInputStream dis = new DataInputStream(bais);
    
        try {
            profundidadeLocal = dis.readInt();
            maxRegistros = dis.readInt();
            int size = dis.readInt();
    
            if (size < 0 || size > maxRegistros) {
                throw new IOException("Número inválido de registros: " + size);
            }
    
            registros = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                Registro r = new Registro();
                r.id = dis.readInt();
                r.end = dis.readLong();
                registros.add(r);
            }
        } catch (EOFException e) {
            throw new IOException("Dados do bucket incompletos", e);
        }
    }
}