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

    public static void ordenar(String arquivoInicial, int numCaminhos, int registros, String arquivoFinal) throws FileNotFoundException, IOException{

        Logger.log(LogLevel.INFO, "Ordenar chamado!");

        try{

            distribuirBlocos(arquivoInicial, numCaminhos, registros);

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

    private static void intercalar(int numCaminhos, String arquivoFinal, int registros) throws IOException{

        Logger.log(LogLevel.INFO, "Intercalar chamado!");

        List<RandomAccessFile> arquivos = new ArrayList<>();
        List<File> temp = new ArrayList<>();
        PriorityQueue<Registro> fila = new PriorityQueue<>(Comparator.comparing(Registro::getIndex));

        for(int i = 0;i < numCaminhos;i++){

            String nome = diretorio + "temp" + i + ".db";
            arquivos.add(new RandomAccessFile(nome, "r"));
            temp.add(new File(nome));

        }

        for(int i = 0;i < numCaminhos;i++){

            RandomAccessFile file = arquivos.get(i);

            if(file.getFilePointer() < file.length()){

                Registro registro = carregarRegistro(file, i);
                fila.add(registro);

            }

        }

        try(RandomAccessFile finalizar = new RandomAccessFile(arquivoFinal, "rw")){

            finalizar.writeInt(ultimoId);

            while(!fila.isEmpty()){

                Registro registro = fila.poll();
                byte[] array = registro.toByteArray();

                finalizar.writeBoolean(false);
                finalizar.writeInt(array.length);
                finalizar.write(array);

                int index = registro.getOrigem();
                RandomAccessFile origem = arquivos.get(index);

                if(origem.getFilePointer() < origem.length()){

                    Registro novo = carregarRegistro(origem, index);
                    fila.add(novo);

                }

            }

        }catch(Exception e){

            e.printStackTrace();

        }finally{

            for(RandomAccessFile x : arquivos){

                x.close();
    
            }

            for(File x : temp){

                if(x.exists()){

                    x.delete();

                }

            }

        }

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
