package algorithms.encryption.caesar;

import java.io.RandomAccessFile;

import repository.CRUDI;

/**
 * Classe que implementa a cifra de César para criptografia e descriptografia de arquivos.
 */
public class CaesarCipher {

    private String diretorio;
    private int chave;

    public CaesarCipher(String diretorio, int chave){

        this.diretorio = diretorio;
        this.chave = chave;

    }

    /*
     * Criptografa o arquivo utilizando a chave fornecida pelo usuário no terminal e salva o resultado em um novo arquivo.
     */
    public void criptografar(){

        try{

            RandomAccessFile file = new RandomAccessFile("data/encrypted/arquivoCriptografadoCaesarCipher" + System.currentTimeMillis() + ".db", "rw");

            String str = CRUDI.lerTudoComoTexto(diretorio);
            int tam = str.length();

            for(int i = 0;i < tam;i++){

                int caractere = str.charAt(i);
                String cripto = "";

                if(caractere + chave > 126){

                    if(caractere >= 32 && caractere <= 126){

                        int temp = caractere + chave;
                        temp -= 94;
                        cripto += (char) temp;

                    }else{

                        cripto += caractere;

                    }

                }else if(caractere >= 32 && caractere <=126){

                    cripto += (char) (caractere + chave);

                }else{

                    cripto += caractere;

                }

                file.writeBytes(cripto);

            }

            file.close();

        }catch(Exception e){

            e.printStackTrace();

        }

    }

    /*
     * Descriptografa o arquivo criptografado utilizando a chave original e salva o resultado em um novo arquivo.
     */
    public void descriptografar(){

        try{

            RandomAccessFile file = new RandomAccessFile(diretorio, "r");
            byte[] bytes = new byte[(int) file.length()];
            file.readFully(bytes);
            String mensagem = new String(bytes);
            String descriptografada = "";

            file.close();

            for(int i = 0;i < mensagem.length();i++){

                int caractere = mensagem.charAt(i);

                if(caractere - chave < 32){

                    if(caractere >= 32 && caractere <= 126){

                        int temp = caractere - chave;
                        temp += 94;
                        descriptografada += (char) temp;

                    }

                }else if(caractere >= 32 && caractere <=126){

                    descriptografada += (char) (caractere - chave);

                }else{

                    descriptografada += caractere;

                }

            }

            RandomAccessFile fileD = new RandomAccessFile("data/decrypted/arquivoDescriptografadoCaesarCipher" + System.currentTimeMillis() + ".db", "rw");
            fileD.write(descriptografada.replace(",+", "\n").getBytes());
            fileD.close();

        }catch(Exception e){

            e.printStackTrace();

        }

    }
    
}