package algorithms.sorting;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.*;

import model.Musica;
import model.Registro;
import util.*;

public class IntercalacaoBalanceada {
    
    private static String diretorio = "data/temp/";
    private static int ultimoId = 0;

    public static void ordenar(int numCaminhos, int registros, String arquivoFinal) throws FileNotFoundException, IOException{

        Logger.log(LogLevel.INFO, "Ordenar chamado!");

        try{

            distribuirBlocos("data/database/rock.db", numCaminhos, registros);

            intercalar(numCaminhos, arquivoFinal, registros);

        }catch(Exception e){

            System.out.println("Falha ao ordenar arquivo!");

        }

        System.out.println("Arquivo ordenado com sucesso!");
        System.out.println();

        Logger.log(LogLevel.INFO, "Ordenar encerrado!");

    }

    private static void distribuirBlocos(String input, int numCaminhos, int registros) throws FileNotFoundException, IOException{

        Logger.log(LogLevel.INFO, "Distribuir Blocos chamado!");

        try(RandomAccessFile file = new RandomAccessFile(input, "r")){

            int id = file.readInt();
            setUltimoId(id);
            List<Musica> buffer = new ArrayList<>();

            int atual = 0;
            int contador = 0;

            while(file.getFilePointer() < file.length()){

                boolean lapide = file.readBoolean();
                int tamanho = file.readInt();
                byte[] array = new byte[tamanho];
                file.readFully(array);

                if(!lapide){

                    Musica nova = new Musica();
                    nova.fromByteArray(array);
                    buffer.add(nova);
                    contador++;

                }
                
                //Salva o bloco no arquivo temporário
                if(contador == registros){

                    salvarBloco(buffer, atual);
                    buffer.clear();
                    contador = 0;
                    atual = (atual + 1) % numCaminhos;

                }

            }

            if(!buffer.isEmpty()){

                salvarBloco(buffer, atual);

            }

        }

        Logger.log(LogLevel.INFO, "Distribuir Blocos encerrado!");

    }

    private static void salvarBloco(List<Musica> buffer, int index) throws FileNotFoundException, IOException{

        Logger.log(LogLevel.INFO, "Salvar Bloco chamado!");

        Collections.sort(buffer, Comparator.comparing(Musica::getIndex));

        String nome = diretorio + "temp" + index + ".db";

        try(RandomAccessFile file = new RandomAccessFile(nome, "rw")){

            file.seek(file.length());

            for(Musica x : buffer){

                byte[] array = x.toByteArray();
                file.writeBoolean(false);
                file.writeInt(array.length);
                file.write(array);

            }

        }

    }

    private static void intercalar(int numCaminhos, String arquivoFinal, int registros) throws IOException {

        Logger.log(LogLevel.INFO, "Intercalar chamado!");
    
        List<File> temp = new ArrayList<>();
        for (int i = 0; i < numCaminhos; i++) {
            String nome = diretorio + "temp" + i + ".db";
            temp.add(new File(nome));
        }
    
        int rodada = 0;
        while (temp.size() > 1) {
            List<File> novosTemp = new ArrayList<>();
            int numArquivos = temp.size();
            for (int i = 0; i < numArquivos; i += numCaminhos) {
                List<RandomAccessFile> arquivos = new ArrayList<>();
                PriorityQueue<Registro> fila = new PriorityQueue<>(Comparator.comparing(Registro::getIndex));
    
                for (int j = 0; j < numCaminhos && (i + j) < numArquivos; j++) {
                    arquivos.add(new RandomAccessFile(temp.get(i + j), "r"));
                }
    
                for (int j = 0; j < arquivos.size(); j++) {
                    RandomAccessFile file = arquivos.get(j);
                    if (file.getFilePointer() < file.length()) {
                        Registro registro = carregarRegistro(file, j);
                        fila.add(registro);
                    }
                }
    
                String novoNome = diretorio + "temp_rodada" + rodada + "_" + i / numCaminhos + ".db";
                try (RandomAccessFile novoArquivo = new RandomAccessFile(novoNome, "rw")) {
                    while (!fila.isEmpty()) {
                        Registro registro = fila.poll();
                        byte[] array = registro.toByteArray();
    
                        novoArquivo.writeBoolean(false);
                        novoArquivo.writeInt(array.length);
                        novoArquivo.write(array);
    
                        int index = registro.getOrigem();
                        RandomAccessFile origem = arquivos.get(index);
    
                        if (origem.getFilePointer() < origem.length()) {
                            Registro novo = carregarRegistro(origem, index);
                            fila.add(novo);
                        }
                    }
                }
    
                for (RandomAccessFile x : arquivos) {
                    x.close();
                }
    
                novosTemp.add(new File(novoNome));
            }
    
            for (File x : temp) {
                if (x.exists()) {
                    x.delete();
                }
            }
    
            temp = novosTemp;
            rodada++;
        }
    
        if (!temp.isEmpty()) {
            File arquivoFinalTemp = temp.get(0);
            arquivoFinalTemp.renameTo(new File(arquivoFinal));
        }
    
        Logger.log(LogLevel.INFO, "Intercalar encerrado!");
    }

    private static Registro carregarRegistro(RandomAccessFile arquivo, int index) throws IOException{

        Logger.log(LogLevel.INFO, "Carregar registro chamado!");

        boolean lapide = arquivo.readBoolean();
        int tamanho = arquivo.readInt();
        byte[] array = new byte[tamanho];
        arquivo.readFully(array);

        Musica nova = new Musica();
        nova.fromByteArray(array);

        return new Registro(nova, index);

    }

    private static void setUltimoId(int id){

        ultimoId = id;

    }

}
