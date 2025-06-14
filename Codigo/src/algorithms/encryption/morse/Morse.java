package algorithms.encryption.morse;

import java.io.RandomAccessFile;

import repository.CRUDI;

public class Morse {
    
    private Dicionario dicionario;
    private String diretorio;

    public Morse(String diretorio){

        dicionario = new Dicionario();
        this.diretorio = diretorio;

    }

    public void criptografar(){

        try{

            RandomAccessFile file = new RandomAccessFile("data/encrypted/arquivoCriptografadoMORSE" + System.currentTimeMillis() + ".db", "rw");

            String str = CRUDI.lerTudoComoTexto(diretorio);
            int tam = str.length();

            for(int i = 0; i < tam; i++){

                int caractere = (int) str.charAt(i);
                String codigoAscii = String.valueOf(caractere);
                String nova = "";

                for(int j = 0; j < codigoAscii.length(); j++){

                    int digito = codigoAscii.charAt(j) - '0';
                    nova += dicionario.procurar(digito);
                    if (j < codigoAscii.length() - 1) nova += ",";

                }

                nova += "/";
                file.writeBytes(nova);

            }

            file.close();

        }catch(Exception e){

            e.printStackTrace();

        }

    }

    public void descriptografar(){

        try{
            
            RandomAccessFile file = new RandomAccessFile(diretorio, "r");
            String[] mensagem = file.readLine().split("/");
            String descriptografada = "";

            file.close();

            for(int i = 0;i < mensagem.length;i++){

                String codigoASCII = "";

                String[] partes = mensagem[i].split(",");

                for(int j = 0;j < partes.length;j++){

                    if(partes[j] != null){

                        codigoASCII += String.valueOf(dicionario.procurarReverso(partes[j]));

                    }

                }

                char caractere = (char) Integer.parseInt(codigoASCII);

                descriptografada += caractere;

            }

            RandomAccessFile fileD = new RandomAccessFile("data/decrypted/arquivoDescriptografadoMORSE" + System.currentTimeMillis() + ".db", "rw");
            fileD.write(descriptografada.getBytes());
            fileD.close();

        }catch(Exception e){

            e.printStackTrace();

        }

    }

}
