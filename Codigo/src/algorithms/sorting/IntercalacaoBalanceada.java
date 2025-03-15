package algorithms.sorting;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.*;

import model.Musica;
import model.Registro;
import util.*;

/*
 * Classe que implementa a Intercalação balanceada e a verificação se um arquivo está ordenado
 * Implementado com ajuda de inteligência artificial
 */
public class IntercalacaoBalanceada {
    
    private static String diretorio = "data/temp/";
    private static int ultimoId = 0;

    /*
     * Método que implementa o algoritmo de Intercalação Balanceada
     * 
     * Funcionamento: Recebe os registros não ordenados e divide em blocos com o número de registros infomado no parâmetro.
     * Em seguida, intercala os blocos no número de caminhos informado na chamada da função.
     * Para finalizar, caminha sobre os arquivos temporários e intercala os registros em um arquivo final escolhendo sempre o registro com menor índice.
     */
    public static void ordenar(int numCaminhos) throws FileNotFoundException, IOException{

        String arquivoInicial = "data/database/rock.db";
        String arquivoFinal = "data/database/final.db";
        String arquivoTemp = "data/temp/temp.db";

        try{

            RandomAccessFile excluir = new RandomAccessFile(arquivoFinal, "rw");

            excluir.setLength(0);

            excluir.close();

        }catch(Exception e){

            Logger.log(LogLevel.ERROR, "Falha ao esvaziar arquivo!");
            return;

        }

        //Partimos de um arquivo não ordenado e chamamos a ordenação de forma iterativa para que, a cada iteração, o arquivo vá trazendo os registros para uma organização cada vez mais ordenada. 
        boolean ordenado = false;

        //Optamos por deixar 100 elementos no bloco (fixo) para não acontecer de o usuário colocar poucos elementos e a ordenação rodar por muito tempo
        //Fizemos testes com 4 elementos e a ordenação rodou por mais de 5 minutos
        while(!ordenado){
            
            ordenar(arquivoInicial, numCaminhos, 100, arquivoFinal);
            ordenado = estaOrdenado(arquivoFinal);

            if(!ordenado){

                // Se não estiver ordenado, o arquivo final se torna o inicial para a próxima iteração
                arquivoInicial = arquivoFinal;
                // Cria um novo arquivo temporário para a próxima iteração
                arquivoFinal = arquivoTemp;
                arquivoTemp = "data/temp/temp" + System.currentTimeMillis() + ".db";

            }

        }

        // Se o arquivo final estiver ordenado, renomeia para o arquivo final desejado
        File finalFile = new File(arquivoFinal);
        File desiredFinalFile = new File("data/database/final.db");
        
        if(desiredFinalFile.exists()){

            desiredFinalFile.delete();

        }

        finalFile.renameTo(desiredFinalFile);

        System.out.println("Arquivo ordenado com sucesso!");

        excluirTemporarios();

    }

    public static void ordenar(String arquivoInicial, int numCaminhos, int registros, String arquivoFinal) throws FileNotFoundException, IOException{

        Logger.log(LogLevel.INFO, "Ordenar chamado!");

        try{

            distribuirBlocos(arquivoInicial, numCaminhos, registros);

            intercalar(numCaminhos, arquivoFinal, registros);

        }catch(Exception e){

            System.out.println("Falha ao ordenar arquivo!");

        }

        Logger.log(LogLevel.INFO, "Ordenar encerrado!");

    }

    //Método que separa os registros em blocos e os distribui de acordo com o número de caminhos informado
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

    //Método que intercala os registros dos caminhos em um arquivo final
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
    
    //Métodos auxiliares para manipulação de registros
    @SuppressWarnings("unused")
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

    //Método para verificar se o arquivo está totalmente ordenado, utilizado nas iterações da intercalação
    @SuppressWarnings("unused")
    public static boolean estaOrdenado(String arquivo) throws IOException{

        Logger.log(LogLevel.INFO, "Verificando se o arquivo está ordenado!");

        try(RandomAccessFile file = new RandomAccessFile(arquivo, "r")){

            if(file.length() == 0){

                return true; //Arquivo vazio sempre é ordenado

            }

            int id = file.readInt(); //Lê o último ID que está gravado no inicio do arquivo
            Musica anterior = null;

            while(file.getFilePointer() < file.length()){

                boolean lapide = file.readBoolean();
                int tamanho = file.readInt();
                byte[] array = new byte[tamanho];
                file.readFully(array);

                if(!lapide){

                    Musica atual = new Musica();
                    atual.fromByteArray(array);

                    if(anterior != null && anterior.getIndex() > atual.getIndex()){

                        return false; //Encontrou algum registro fora de ordem

                    }

                    anterior = atual;

                }

            }

        }

        return true; //O arquivo está totalmente ordenado

    }

    //Método que usa o makefile para excluir todos os arquivos temporários criados para a ordenação
    private static void excluirTemporarios(){

        try{

            Process make = new ProcessBuilder("make", "delete").start();
            make.waitFor();

        }catch(IOException | InterruptedException e){

            e.printStackTrace();

        }

    }

}
