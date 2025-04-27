package algorithms.indexing.btree;

import model.Registro;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.Comparator;

/*
 * Classe que implementa o algoritmo da Arvore B em memória secundária
 */
@SuppressWarnings("unused")
public class BTree {

    /*
     * Classe que implementa a estrutura da Página da Arvore B
     */
    public class Pagina{

        public int numElementos;
        public Registro[] chaves = new Registro[ordem - 1];
        public long[] filhos = new long[ordem];
        public boolean folha = true;

        public Pagina(int ordem){

            criarPagina(ordem);

        }

        /*
         * Método que preenche a página com valores "nulos" (-1), para ids e endereços
         */
        public void criarPagina(int ordem){

            numElementos = 0;

            for(int i = 0;i < (ordem - 1);i++){chaves[i] = new Registro();}
            for(int i = 0;i < ordem;i++){filhos[i] = -1;}

            folha = true;

        }

        /*
         * Método que transforma o objeto Pagina em um vetor de bytes para ser inserido no arquivo
         */
        public byte[] toByteArray(){

            try{

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                DataOutputStream dos = new DataOutputStream(baos);

                dos.writeInt(numElementos);

                for(int i = 0;i < ordem;i++){

                    dos.writeLong(filhos[i]);

                }

                for(int i = 0;i < ordem - 1;i++){

                    dos.writeInt(chaves[i].id);
                    dos.writeLong(chaves[i].end);

                }

                dos.writeBoolean(folha);

                return baos.toByteArray();

            }catch(Exception e){

                e.printStackTrace();

            }

            return null;

        }

        /*
         * Método que transforma um vetor de bytes em um objeto Pagina, para que seja manipulado
         */
        public void fromByteArray(byte[] array){

            try{

                ByteArrayInputStream bais = new ByteArrayInputStream(array);
                DataInputStream dis = new DataInputStream(bais);

                this.numElementos = dis.readInt();

                for(int i = 0;i < ordem;i++){

                    this.filhos[i] = dis.readLong();

                }

                for(int i = 0;i < ordem - 1;i++){

                    if(chaves[i] == null){

                        chaves[i] = new Registro();

                    }

                    this.chaves[i].id = dis.readInt();
                    this.chaves[i].end = dis.readLong();

                }

                this.folha = dis.readBoolean();

            }catch(Exception e){

                e.printStackTrace();

            }

        }

        /*
         * Método que ordena os registros da página, para que as operações de 
         * busca, inserção e remoção sejam feitas corretamente
         */
        public void ordenarChaves(){

            Registro[] validos = new Registro[numElementos];
            int cont = 0;

            for(int i = 0;i < chaves.length;i++){

                if(chaves[i] != null && chaves[i].id >= 0){

                    validos[cont] = chaves[i];
                    cont++;

                }

            }

            Arrays.sort(validos, 0, cont, Comparator.comparingInt(r -> r.id));

            for(int i = 0;i < chaves.length;i++){

                if(i < cont){

                    chaves[i] = validos[i];

                }else{

                    chaves[i] = new Registro();

                }

            }

        }

    }

    /*
     * Classe para auxiliar o split de uma página da Arvore B
     */
    private class SplitResult{

        public Registro registro;
        public long enderecoPaginaNova;

        public SplitResult(Registro subir, long enderecoPaginaNova){

            this.registro = subir;
            this.enderecoPaginaNova = enderecoPaginaNova;
            
        }

    }

    private int ordem;
    private long raiz;
    private static final String arquivo = "data/indexes/BTree.db";
    public static RandomAccessFile file;

    private Registro registroAux;
    private long paginaAux;

    /*
     * Construtor usado quando a árvore já está construída
     * 
     * Funcionamento:
     * - Abre o arquivo de índice e lê um long, que é a raiz da árvore, e guarda no atributo.
     * - Após isso lê um inteiro, que é a ordem da árvore, e guarda no atributo.
     */
    public BTree(){

        try{

            file = new RandomAccessFile(arquivo, "rw");

            file.seek(0);
            this.raiz = file.readLong();
            this.ordem = file.readInt();

        }catch(Exception e){

            e.printStackTrace();

        }

    }

    /*
     * Construtor usado quando a árvore será construída
     * 
     * Funcionamento:
     * - Abre o arquivo de índice e o esvazia completamente.
     * - Após isso, escreve um long, a raiz, e um inteiro, a ordem.
     */
    public BTree(int ordem){

        this.ordem = ordem;
        this.raiz = -1;

        try{

            file = new RandomAccessFile(arquivo, "rw");

            file.setLength(0);
            file.seek(0);
            file.writeLong(raiz);
            file.writeInt(ordem);

        }catch(Exception e){

            e.printStackTrace();

        }

    }

    /*
     * Método que insere a página no arquivo, no endereço informado
     */
    private void inserirPagina(Pagina pagina, long endereco){

        try{

            file.seek(endereco);

            byte[] array = pagina.toByteArray();

            if(array != null){

                file.writeInt(array.length);
                file.write(array);

            }else{

                throw new Exception("Erro ao transformar em array de bytes!");

            }

        }catch(Exception e){

            e.printStackTrace();
            System.err.println(e.getMessage());

        }

    }

    /*
     * Método que vai até o endereço informado, lê uma página inteira, cria o objeto e o retorna
     */
    private Pagina lerPagina(long endereco){

        try{

            file.seek(endereco);

            int tam = file.readInt();
            byte[] array = new byte[tam];
            file.readFully(array);

            Pagina nova = new Pagina(this.ordem);
            nova.fromByteArray(array);

            return nova;

        }catch(Exception e){

            e.printStackTrace();

        }

        return null;

    }

/////////////////////////////////////////////////INSERIR//////////////////////////////////////////////////////

    /*
     * Método que insere um registro quando a árvore está vazia
     * 
     * Funcionamento:
     * - Verifica se a raiz é -1 (árvore vazia)
     * - Se for -1, vai para o final do arquivo, cria a página, insere o registro e insere a página no arquivo
     * - Se não for -1, chama o outro método de inserir, que possui os outros casos
     */
    public void inserir(Registro registro){

        try{

            if(raiz == -1){

                long endereco = file.length();
                file.seek(endereco);

                Pagina pagina = new Pagina(this.ordem);
                pagina.chaves[0] = registro;
                pagina.numElementos = 1;

                inserirPagina(pagina, endereco);

                file.seek(0);
                file.writeLong(endereco);
                this.raiz = endereco;
    
            }else{

                inserir(registro, raiz, null, -1);

            }

        }catch(Exception e){

            e.printStackTrace();

        }

    }

    /*
     * Método que insere um registro
     * 
     * Funcionamento:
     * - Lê a página no endereço informado e verifica se é uma folha
     * - Se não for, procura o endereço da página em que ele se encaixa
     * - Se for, verifica a quantidade de elementos
     * - Se for possível inserir, insere o registro e insere a página no arquivo
     * - Se não for possível inserir, faz split na página
     */
    private void inserir(Registro registro, long endereco, Pagina pai, long enderecoPai){

        try{

            Pagina pagina = lerPagina(endereco);

            if(pagina.folha){

                if(pagina.numElementos < (ordem - 1)){

                    pagina.chaves[pagina.numElementos] = registro;
                    pagina.numElementos++;
                    pagina.ordenarChaves();

                    inserirPagina(pagina, endereco);
    
                }else{

                    SplitResult splitado = splitPagina(pagina, registro, endereco);
                    
                    if(splitado != null){

                        inserirEndereco(pai, enderecoPai, splitado.enderecoPaginaNova, splitado.registro);

                    }

                }

            }else{

                long e = procurarEndereco(registro, pagina);

                inserir(registro, e, pagina, endereco);

            }

        }catch(Exception e){

            e.printStackTrace();

        }

    }

    /* 
     * Método auxiliar para o split, que recebe o elemento que irá subir e o coloca na página pai
    */
    private SplitResult inserirEndereco(Pagina pai, long enderecoPai, long endereco, Registro aSubir){

        try{

            if(pai.numElementos < (ordem - 1)){

                int pos = 0;
                while(pos < pai.numElementos && pai.chaves[pos].id < aSubir.id){ pos++; }
    
                for(int j = pai.numElementos;j > pos;j--){

                    pai.chaves[j] = pai.chaves[j - 1];

                }
                pai.chaves[pos] = aSubir;

                for(int j = pai.numElementos + 1;j > pos;j--){

                    pai.filhos[j] = pai.filhos[j - 1];

                }
                pai.filhos[pos + 1] = endereco;

                pai.numElementos++;
                inserirPagina(pai, enderecoPai);

                return null;

            }else{

                return splitPagina(pai, aSubir, enderecoPai);

            }

        }catch(Exception e){

            e.printStackTrace();

        }

        return null;

    }

    /*
     * Método que procura o endereço da próxima página para inserir o registro
     */
    private long procurarEndereco(Registro registro, Pagina pagina){

        for(int i = 0;i < pagina.numElementos;i++){

            if(registro.id < pagina.chaves[i].id){

                return pagina.filhos[i];

            }

        }

        return pagina.filhos[pagina.numElementos];

    }

    /*
     * Método split
     * 
     * Funcionamento:
     * - Recebe a página cheia e o registro a ser inserido, e insere todos em um array
     * - Ordena o array para saber qual elemento irá subir
     * - Cria duas páginas novas, insere os elementos da esquerda e da direita do elemento que irá subir nas páginas criadas
     * - Insere os endereços dos filhos da página cheia nas páginas criadas
     * - Insere as páginas no arquivo de índice
     */
    private SplitResult splitPagina(Pagina cheia, Registro novo, long enderecoPaginaCheia){

        try{

            Registro[] registros = new Registro[ordem];

            for(int i = 0;i < (ordem - 1);i++){
    
                registros[i] = cheia.chaves[i];
    
            }
    
            registros[ordem - 1] = novo;
    
            Arrays.sort(registros, Comparator.comparingInt(r -> r.id));
    
            Pagina novaEsq = new Pagina(this.ordem);
            Pagina novaDir = new Pagina(this.ordem);
    
            for(int i = 0;i < (ordem / 2);i++){ novaEsq.chaves[i] = registros[i]; }
            Registro vaiSubir = registros[ordem / 2];
            for(int i = ((ordem / 2) + 1), j = 0;i < registros.length;i++,j++){ novaDir.chaves[j] = registros[i]; }

            novaEsq.numElementos = (ordem / 2);
            novaDir.numElementos = (ordem / 2);

            novaEsq.ordenarChaves();
            novaDir.ordenarChaves();

            for(int i = 0;i <= ordem / 2;i++){

                novaEsq.filhos[i] = cheia.filhos[i];

            }

            for(int i = (ordem / 2) + 1,j = 0;i < ordem;i++,j++){

                novaDir.filhos[j] = cheia.filhos[i];

            }
    
            inserirPagina(novaEsq, enderecoPaginaCheia);

            long enderecoNovo = file.length();
            inserirPagina(novaDir, enderecoNovo);

            if(enderecoPaginaCheia == raiz){
                
                Pagina raizNova = new Pagina(this.ordem);
                raizNova.chaves[0] = vaiSubir;
                raizNova.filhos[0] = enderecoPaginaCheia;
                raizNova.filhos[1] = enderecoNovo;
                raizNova.numElementos = 1;

                long enderecoRaizNova = file.length();
                inserirPagina(raizNova, enderecoRaizNova);
                file.seek(0);
                file.writeLong(enderecoRaizNova);
                this.raiz = enderecoRaizNova;

            }else{

                return new SplitResult(vaiSubir, enderecoNovo);

            }

        }catch(Exception e){

            e.printStackTrace();

        }

        return null;

    }

/////////////////////////////////////////////////PROCURAR//////////////////////////////////////////////////////

    /*
     * Método de procura
     * 
     * Funcionamento:
     * - Se a árvore for vazia, retorna nulo
     * - Se não, chama o método de procura
     */
    public Registro procurar(int id){

        if(raiz == -1){

            return null;

        }

        return procurar(id, raiz);

    }

    /*
     * Método de procura
     * 
     * Funcionamento:
     * - Lê a página no endereço informado
     * - Verifica se o id está presente na página
     * - Se sim, retorna o registro
     * - Se não, caminha para a próxima página
     */
    private Registro procurar(int id, long endereco){

        Pagina pagina = lerPagina(endereco);

        for(int i = 0;i < pagina.numElementos;i++){

            if(id == pagina.chaves[i].id){

                return pagina.chaves[i];

            }else if(id < pagina.chaves[i].id){

                if(pagina.filhos[i] == -1){

                    return null;

                }

                return procurar(id, pagina.filhos[i]);

            }

        }

        if(pagina.filhos[pagina.numElementos] == -1){

            return null;

        }

        return procurar(id, pagina.filhos[pagina.numElementos]);

    }

/////////////////////////////////////////////////ATUALIZAR//////////////////////////////////////////////////////

    /*
     * Método de atualização
     * 
     * Funcionamento:
     * - Se a árvore for vazia, retorna falso
     * - Se não, chama o método de atualização
     */
    public boolean atualizar(int id, long endNovo){

        if(raiz == -1){

            return false;

        }

        return atualizar(id, raiz, endNovo);

    }

    /*
     * Método de atualização
     * 
     * Funcionamento:
     * - Lê a página no endereço informado
     * - Verifica se o id está presente na página
     * - Se sim, atualiza o registro e insere no arquivo
     * - Se não, caminha para a próxima página
     */
    private boolean atualizar(int id, long endereco, long endNovo){

        Pagina pagina = lerPagina(endereco);

        for(int i = 0;i < pagina.numElementos;i++){

            if(id == pagina.chaves[i].id){

                Registro novo = new Registro(id, endNovo);

                pagina.chaves[i] = novo;

                inserirPagina(pagina, endereco);

            }else if(id < pagina.chaves[i].id){

                if(pagina.filhos[i] == -1){

                    return false;

                }

                return atualizar(id, pagina.filhos[i], endNovo);

            }

        }

        if(pagina.filhos[pagina.numElementos] == -1){

            return false;

        }

        return atualizar(id, pagina.filhos[pagina.numElementos], endNovo);

    }

/////////////////////////////////////////////////DELETAR//////////////////////////////////////////////////////

    /*
     * Método de deletar
     * 
     * Funcionamento:
     * - Se a árvore for vazia, retorna falso
     * - Se não, chama o método de deletar
     */
    public boolean deletar(int id){

        if(raiz == -1){

            return false;

        }

        return deletar(id, raiz);

    }

    /*
     * Método de deletar
     * 
     * Funcionamento:
     * - Lê a página no endereço informado
     * - Verifica se o id está presente na página
     * - Se sim, deleta o registro e insere a página no arquivo
     * - Se não, caminha para a próxima página
     */
    private boolean deletar(int id, long endereco){

        Pagina pagina = lerPagina(endereco);

        for(int i = 0;i < pagina.numElementos;i++){

            if(id == pagina.chaves[i].id){

                Registro novo = new Registro();

                pagina.chaves[i] = novo;

                inserirPagina(pagina, endereco);

            }else if(id < pagina.chaves[i].id){

                if(pagina.filhos[i] == -1){

                    return false;

                }

                return deletar(id, pagina.filhos[i]);

            }

        }

        if(pagina.filhos[pagina.numElementos] == -1){

            return false;

        }

        return deletar(id, pagina.filhos[pagina.numElementos]);

    }
    
}
