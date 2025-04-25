package algorithms.hash;

import model.Registro;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/*
 * Classe que implementa um Bucket da Tabela Hash Extensível
 */
public class Bucket {
    public int profundidadeLocal;
    public List<Registro> registros;
    public int maxRegistros;

    public Bucket(int maxRegistros) {
        this.profundidadeLocal = 1;
        this.registros = new ArrayList<>();
        this.maxRegistros = maxRegistros;
    }

    /*
     * Método para verificar se o bucket está cheio
     */
    public boolean isFull() {
        return registros.size() >= maxRegistros;
    }

    /*
     * Método para inserir um registro no bucket
     * 
     * Funcionamento:
     * - Verifica se o bucket está cheio
     * - Se estiver, retorna falso
     * - Se não, adiciona o registro e retorna true
     */
    public boolean inserir(Registro r) {
        if (!isFull()) {
            registros.add(r);
            return true;
        }
        return false;
    }

    /*
     * Método de busca de um registro no bucket
     * 
     * Funcionamento:
     * - Verifica se o bucket foi inicializado
     * - Se não, retorna nulo
     * - Se sim, procura o registro no bucket
     * - Se achar o registro, o retorna
     * - Se não, retorna nulo
     */
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

    /*
     * Método para transformar um objeto Bucket em um array de bytes
     */
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

    /*
     * Método para transformar um array de bytes em um objeto Bucket
     */
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