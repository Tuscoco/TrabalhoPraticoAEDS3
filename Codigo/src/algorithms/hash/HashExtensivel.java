package algorithms.hash;

<<<<<<< Updated upstream
import model.RegistroSort;
=======
import model.Registro;
>>>>>>> Stashed changes
import model.Musica;

import java.io.IOException;
import java.io.RandomAccessFile;

public class HashExtensivel {

    private Diretorio diretorio;
    private int capacidadeBucket;

    public HashExtensivel(int capacidadeBucket) throws IOException {
        this.capacidadeBucket = capacidadeBucket;

        // Inicializa o diretório se ele não existir
        try {
            new RandomAccessFile("diretorio.dat", "r").close();
            this.diretorio = new Diretorio(0); // abre normalmente, já existe
        } catch (IOException e) {
            this.diretorio = new Diretorio(1); // profundidade inicial 1
            new Bucket(0).criar(1);
            new Bucket(1).criar(1);
        }
    }

    private int hash(int chave, int profundidade) {
        return chave & ((1 << profundidade) - 1);
    }

<<<<<<< Updated upstream
    public void inserir(RegistroSort registro) throws IOException {
=======
    public void inserir(Registro registro) throws IOException {
>>>>>>> Stashed changes
        int profundidadeGlobal = diretorio.getProfundidadeGlobal();
        int chave = registro.getIndex();
        int posicao = hash(chave, profundidadeGlobal);
        int bucketID = diretorio.getPonteiro(posicao);
        Bucket bucket = new Bucket(bucketID);

        boolean inserido = bucket.adicionar(registro, capacidadeBucket);

        if (inserido) return;

        // Bucket cheio — dividir
        int profundidadeLocal = bucket.getProfundidadeLocal();

        if (profundidadeLocal == profundidadeGlobal) {
            diretorio.duplicar();
            profundidadeGlobal++;
        }

        int novoBucketID = criarNovoBucket(profundidadeLocal + 1);
        Bucket novoBucket = new Bucket(novoBucketID);

        bucket.setProfundidadeLocal(profundidadeLocal + 1);
        novoBucket.setProfundidadeLocal(profundidadeLocal + 1);

        // Atualiza ponteiros do diretório
        int entradas = 1 << profundidadeGlobal;
        for (int i = 0; i < entradas; i++) {
            int bID = diretorio.getPonteiro(i);
            if (bID == bucketID) {
                int hashLocal = hash(i, profundidadeLocal + 1);
                if ((hashLocal & 1) == 1) {
                    diretorio.setPonteiro(i, novoBucketID);
                }
            }
        }

        // Redistribuição (releitura e regravação)
        reescreverBucket(bucketID, bucket, novoBucketID, novoBucket);

        // Tenta inserir novamente
        inserir(registro);
    }

    private void reescreverBucket(int antigoID, Bucket antigo, int novoID, Bucket novo) throws IOException {
        String tempArquivo = "temp_bucket.dat";

        try (RandomAccessFile original = new RandomAccessFile("bucket_" + antigoID + ".dat", "r");
             RandomAccessFile temp = new RandomAccessFile(tempArquivo, "rw")) {

            int profundidade = original.readInt();
            int quantidade = original.readInt();

            temp.writeInt(profundidade); // o mesmo, pois bucket será regravado
            temp.writeInt(0); // será contado

            int novaQtd = 0;

            while (original.getFilePointer() < original.length()) {
                int tam = original.readInt();
                byte[] dados = new byte[tam];
                original.readFully(dados);

                Musica musica = new Musica(dados);
                int hashLocal = hash(musica.getIndex(), profundidade + 1);

<<<<<<< Updated upstream
                RegistroSort reg = new RegistroSort(musica, -1);
=======
                Registro reg = new Registro(musica, -1);
>>>>>>> Stashed changes

                if ((hashLocal & 1) == 1) {
                    novo.adicionar(reg, capacidadeBucket); // manda pro novo
                } else {
                    temp.writeInt(tam);
                    temp.write(dados);
                    novaQtd++;
                }
            }

            temp.seek(4);
            temp.writeInt(novaQtd);
        }

        // Substitui o antigo bucket pelo novo temp
        new java.io.File("bucket_" + antigoID + ".dat").delete();
        new java.io.File(tempArquivo).renameTo(new java.io.File("bucket_" + antigoID + ".dat"));
    }

    private int criarNovoBucket(int profundidade) throws IOException {
        int novoID = maiorIDBucket() + 1;
        Bucket novo = new Bucket(novoID);
        novo.criar(profundidade);
        return novoID;
    }

    private int maiorIDBucket() throws IOException {
        int max = -1;
        int tamanho = diretorio.tamanho();
        for (int i = 0; i < tamanho; i++) {
            int id = diretorio.getPonteiro(i);
            if (id > max) max = id;
        }
        return max;
    }

    public boolean remover(int chave) throws IOException {
        int profundidadeGlobal = diretorio.getProfundidadeGlobal();
        int posicao = hash(chave, profundidadeGlobal);
        int bucketID = diretorio.getPonteiro(posicao);
        Bucket bucket = new Bucket(bucketID);

        return bucket.remover(chave);
    }

    public void listarTodos() throws IOException {
        int tamanho = diretorio.tamanho();
        boolean[] vistos = new boolean[maiorIDBucket() + 1];

        for (int i = 0; i < tamanho; i++) {
            int id = diretorio.getPonteiro(i);
            if (!vistos[id]) {
                System.out.println("Bucket " + id + ":");
                new Bucket(id).listarRegistros();
                vistos[id] = true;
            }
        }
    }
}
