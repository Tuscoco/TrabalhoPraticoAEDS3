package algorithms.compression.lzw;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.util.ArrayList;

import repository.CRUDI;

@SuppressWarnings("unused")
public class LZW {
    
    private static final int BITS_POR_INDICE = 12;
    private String diretorio;

    public LZW(String diretorio){

        this.diretorio = diretorio;

    }

    public void comprimir(){

        try{

            String str = CRUDI.lerTudoComoTexto(diretorio);
            byte[] msgBytes = str.getBytes();
    
            ArrayList<ArrayList<Byte>> dicionario = new ArrayList<>();
            ArrayList<Byte> vetorBytes;
            int i, j;
            byte b;
    
            for(j = -128; j < 128; j++){
    
                b = (byte) j;
                vetorBytes = new ArrayList<>();
                vetorBytes.add(b);
                dicionario.add(vetorBytes);
    
            }
    
            ArrayList<Integer> saida = new ArrayList<>();
    
            i = 0;
            int indice;
            int ultimoIndice;
            while (i < msgBytes.length) {
    
                vetorBytes = new ArrayList<>();
                b = msgBytes[i];
                vetorBytes.add(b);
                indice = dicionario.indexOf(vetorBytes);
                ultimoIndice = indice;
    
                while (indice != -1 && i < msgBytes.length - 1) {
    
                    i++;
                    b = msgBytes[i];
                    vetorBytes.add(b);
                    indice = dicionario.indexOf(vetorBytes);
    
                    if (indice != -1)
                        ultimoIndice = indice;
                }
    
                saida.add(ultimoIndice);
    
                if(dicionario.size() < (Math.pow(2, BITS_POR_INDICE) - 1)){
    
                    dicionario.add(vetorBytes);
    
                }
    
                if (indice != -1 && i == msgBytes.length - 1){
    
                    break;
    
                }
    
            }
    
            VetorDeBits bits = new VetorDeBits(saida.size()*BITS_POR_INDICE);
            int l = saida.size()*BITS_POR_INDICE-1;
    
            for(i=saida.size()-1; i>=0; i--){
    
                int n = saida.get(i);
                for(int m=0; m<BITS_POR_INDICE; m++){
    
                    if(n%2==0){
    
                        bits.clear(l);
    
                    }else{
    
                        bits.set(l);
    
                    }
    
                    l--;
                    n /= 2;
    
                }
    
            }
    
            RandomAccessFile arquivoDestino = new RandomAccessFile("data/compressed/arquivoComprimidoLZW" + System.currentTimeMillis() + ".db", "rw");
    
            arquivoDestino.write(bits.toByteArray());
    
            arquivoDestino.close();

        }catch(Exception e){

            e.printStackTrace();

        }

    }

    @SuppressWarnings("unchecked")
    public void descomprimir(){

        try{

            RandomAccessFile arquivoOrigem = new RandomAccessFile(diretorio, "r");
            byte[] msgCodificada = new byte[(int) arquivoOrigem.length()];

            for(int i = 0;i < arquivoOrigem.length();i++){

                msgCodificada[i] = arquivoOrigem.readByte();

            }

            arquivoOrigem.close();

            VetorDeBits bits = new VetorDeBits(msgCodificada);
    
            int i, j, k;
            ArrayList<Integer> indices = new ArrayList<>();
            k=0;
            for(i=0; i < bits.length()/BITS_POR_INDICE; i++){
    
                int n = 0;
    
                for(j=0; j<BITS_POR_INDICE; j++){
    
                    n = n*2 + (bits.get(k++)?1:0);
    
                }
    
                indices.add(n);
    
            }
    
            ArrayList<Byte> vetorBytes;
    
            ArrayList<Byte> msgBytes = new ArrayList<>();
    
            ArrayList<ArrayList<Byte>> dicionario = new ArrayList<>();
            byte b;
            for(j = -128, i = 0; j < 128; j++, i++){
    
                b = (byte) j;
                vetorBytes = new ArrayList<>();
                vetorBytes.add(b);
                dicionario.add(vetorBytes);
    
            }
    
            ArrayList<Byte> proximoVetorBytes;
    
            i = 0;
            while(i < indices.size()){
    
                vetorBytes = (ArrayList<Byte>) (dicionario.get(indices.get(i))).clone();
    
                for(j = 0; j < vetorBytes.size(); j++){
    
                    msgBytes.add(vetorBytes.get(j));
    
                }
    
                if(dicionario.size() < (Math.pow(2, BITS_POR_INDICE) - 1)){
    
                    dicionario.add(vetorBytes);
    
                }
    
                i++;
                if(i < indices.size()){
    
                    proximoVetorBytes = (ArrayList<Byte>) dicionario.get(indices.get(i));
                    vetorBytes.add(proximoVetorBytes.get(0));
    
                }
    
            }
    
            byte[] msgVetorBytes = new byte[msgBytes.size()];
    
            for(i = 0; i < msgBytes.size(); i++){
    
                msgVetorBytes[i] = msgBytes.get(i);
    
            }

            RandomAccessFile arquivoDestino = new RandomAccessFile("data/database/arquivoDescomprimidoLZW" + System.currentTimeMillis() + ".db", "rw");

            arquivoDestino.write(msgVetorBytes);

            arquivoDestino.close();

        }catch(Exception e){

            e.printStackTrace();
    
        }
        
    }

}
