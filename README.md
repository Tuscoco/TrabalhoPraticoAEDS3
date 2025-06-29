# Trabalho Prático de AEDs III

## Visão Geral

Este projeto é o Trabalho Prático (TP) da disciplina de Algoritmos e Estruturas de Dados III (AEDs III). Seu objetivo principal é aprofundar a compreensão e a aplicação prática de conceitos de armazenamento e manipulação de dados em memória secundária, incluindo acesso sequencial e indexado, compressão de dados e criptografia.

O projeto será desenvolvido em etapas, construindo uma aplicação robusta para gerenciar registros de uma base de dados escolhida.
Entidade Base: **Musica**

A entidade principal do nosso sistema é a Musica, que representa um registro de informações sobre uma faixa musical. Sua estrutura é crucial para a forma como os dados serão armazenados e manipulados em disco. A classe Musica possui os seguintes campos, categorizados por seu tipo de tamanho:

    index (int): Identificador único da música. É um campo de Tamanho Fixo (4 bytes).

    name (String): O nome da música. Este é um campo de Tamanho Variável, pois seu comprimento pode mudar para cada registro.

    artist (String): O artista principal da música. Este campo é tratado como Tamanho Fixo (15 caracteres), sendo preenchido com espaços ou truncado para garantir essa padronização.

    date (long): A data de lançamento da música, armazenada como um timestamp. É um campo de Tamanho Fixo (8 bytes).

    length (double): A duração da música. É um campo de Tamanho Fixo (8 bytes).

    fArtists (String[]): Um array de strings representando artistas participantes (featured artists). Este é um campo de Lista de Valores com Separador/Formato a Definir. Na serialização, o número de artistas é gravado, seguido pelo comprimento e os bytes de cada string de artista, permitindo a reconstrução da lista.

A classe Musica inclui métodos essenciais (toByteArray() e fromByteArray()) para serialização e desserialização de objetos para/de arrays de bytes, permitindo o armazenamento eficiente em arquivos.

## Etapas do Trabalho Prático

O projeto está dividido em quatro etapas progressivas, cada uma adicionando funcionalidades e aprofundando os conceitos de AEDs III.

### Etapa 1: Criação da Base de Dados, Manipulação de Arquivo Sequencial, Ordenação Externa

Nesta primeira etapa, o foco é estabelecer a base do sistema de gerenciamento de arquivos.

#### 1.1. Criação da Base de Dados

    Objetivo: Preparar um conjunto de dados para a entidade Musica.

    Implementação: Serão criados e populados registros da entidade Musica a partir de uma fonte de dados (e.g., CSV, JSON ou gerados aleatoriamente). Esses registros serão a base sobre a qual todas as operações subsequentes serão realizadas.

#### 1.2. Manipulação de Arquivo Sequencial

    Objetivo: Desenvolver funções para armazenar e acessar registros sequencialmente em um arquivo binário.

    Implementação:

        Inclusão (CRUD - Create): Adicionar novos registros ao final do arquivo.

        Leitura (CRUD - Read): Percorrer o arquivo do início ao fim para ler todos os registros ou buscar um registro específico por varredura.

        Atualização (CRUD - Update): Localizar um registro existente e modificar seu conteúdo. Isso geralmente envolve marcar o registro antigo como "inválido" e adicionar a versão atualizada ao final do arquivo.

        Exclusão (CRUD - Delete): Marcar um registro como excluído logicamente (e.g., alterando um byte de flag no início do registro). A exclusão física (reorganização do arquivo para remover espaços vazios) pode ser implementada posteriormente ou em um processo de "coleta de lixo" (garbage collection).

    Formato do Arquivo: O arquivo será binário, com cada registro Musica serializado para byte[] usando os métodos da classe Musica.

#### 1.3. Ordenação Externa

    Objetivo: Implementar um algoritmo de ordenação que possa lidar com arquivos de dados que não cabem completamente na memória RAM.

    Implementação: Será escolhido e implementado um algoritmo de ordenação externa (e.g., Merge Sort Multi-Caminhos). Este algoritmo envolverá a divisão do arquivo original em blocos menores que podem ser ordenados na memória, a gravação desses blocos temporários de volta ao disco, e então a fusão desses blocos ordenados para produzir o arquivo final completamente ordenado.

    Relevância: Essencial para otimizar buscas sequenciais e preparar o terreno para a construção de índices.

### Etapa 2: Manipulação de Arquivo Indexado com Árvore B+, Hash e Lista Invertida

Esta etapa foca na melhoria da performance de busca através da implementação de estruturas de indexação.

#### 2.1. Índice com Árvore B+

    Objetivo: Implementar uma Árvore B+ para permitir buscas eficientes baseadas em um campo chave (e.g., index ou name).

    Implementação:

        Estrutura: A Árvore B+ será uma estrutura de dados de árvore balanceada, otimizada para armazenamento em disco. Os nós folha formarão uma lista ligada para permitir varreduras de intervalo eficientes.

        Operações: Inclusão, busca e exclusão de chaves. Cada nó da árvore armazenará chaves e ponteiros para páginas de dados no arquivo principal ou para outros nós da árvore.

    Vantagens: Excelente para buscas por intervalo e para manter os dados ordenados.

#### 2.2. Índice com Hashing Extensível

    Objetivo: Implementar uma estrutura de Hash para permitir buscas de acesso direto extremamente rápidas.

    Implementação:

        Função de Hash: Será desenvolvida uma função de hash que mapeia o campo chave (e.g., name) para um endereço no arquivo de índice ou diretamente no arquivo de dados.

        Tratamento de Colisões: Será implementado um método para resolver colisões (e.g., endereçamento aberto ou encadeamento separado).

        Tipos de Hashing: Pode-se optar por Hashing Extensível (que se expande e contrai dinamicamente) ou Hashing Linear (que se expande gradualmente).

    Vantagens: Acesso muito rápido a registros individuais quando a chave é conhecida.

#### 2.3. Lista Invertida

    Objetivo: Implementar uma Lista Invertida para permitir buscas baseadas em palavras-chave em campos de texto (e.g., name ou fArtists).

    Implementação:

        Estrutura: A Lista Invertida mapeará termos (palavras) a uma lista de identificadores de documentos (ou registros) onde esses termos aparecem.

        Processamento: O campo de texto será tokenizado (dividido em palavras) e as palavras serão indexadas.

        Busca: Permitirá buscar registros que contenham uma ou mais palavras-chave.

    Vantagens: Essencial para buscas textuais e sistemas de recuperação de informação.

### Etapa 3: Compactação com Huffman e LZW e Casamento de Padrões

Esta etapa aborda a otimização do espaço de armazenamento e a busca por padrões dentro dos dados.

#### 3.1. Compactação com Huffman

    Objetivo: Reduzir o tamanho dos arquivos de dados usando o algoritmo de Huffman.

    Implementação:

        Algoritmo: O algoritmo de Huffman será aplicado aos dados, gerando um código de prefixo variável para cada caractere ou byte, onde caracteres/bytes mais frequentes recebem códigos mais curtos.

        Operações: Funções de compactação e descompactação de arquivos.

        Tabela de Frequência: A tabela de frequências (ou a árvore de Huffman) deve ser armazenada junto com os dados compactados para permitir a descompactação.

    Vantagens: Compressão sem perdas, eficiente para dados com distribuição de frequência não uniforme.

#### 3.2. Compactação com LZW

    Objetivo: Reduzir o tamanho dos arquivos de dados usando o algoritmo LZW (Lempel-Ziv-Welch).

    Implementação:

        Algoritmo: O LZW construirá um dicionário de sequências de dados (padrões) conforme a compactação progride. Em vez de compactar caracteres individuais, ele substitui sequências repetidas por códigos menores.

        Operações: Funções de compactação e descompactação.

    Vantagens: Compressão sem perdas, geralmente eficiente para dados com muitas repetições de padrões longos.

#### 3.3. Casamento de Padrões (Pattern Matching)

    Objetivo: Implementar algoritmos eficientes para encontrar ocorrências de um padrão de texto dentro de um texto maior.

    Implementação:

        Será implementado um algoritmo de casamento de padrões (e.g., Knuth-Morris-Pratt (KMP), Boyer-Moore, ou Rabin-Karp).

        Aplicação: Utilizado para buscar substrings dentro de campos de texto da entidade Musica ou para validação de dados.

    Relevância: Fundamental para buscas avançadas e processamento de texto.

### Etapa 4: Criptografia

A etapa final concentra-se na segurança dos dados, implementando técnicas de criptografia.

#### 4.1. Criptografia

    Objetivo: Proteger a confidencialidade e a integridade dos dados armazenados, tornando-os ilegíveis para usuários não autorizados.

    Implementação:

        Serão implementados diversos algoritmos de criptografia, incluindo:

            RSA: Um algoritmo de criptografia assimétrica amplamente utilizado para segurança de dados, incluindo troca de chaves e assinaturas digitais.

            Cifra de César: Um algoritmo de criptografia simétrica de substituição simples, útil para fins didáticos de introdução à criptografia.

            Código Morse: Embora não seja um algoritmo de criptografia no sentido tradicional (é um esquema de codificação), pode ser implementado para demonstrar codificação e decodificação de mensagens.

        Gerenciamento de Chaves: Considerações sobre como as chaves para cada algoritmo serão geradas, armazenadas e gerenciadas.

    Vantagens: Segurança dos dados em repouso e em trânsito (dependendo da aplicação).

    Aplicação: Poderá ser aplicada a campos específicos da entidade Musica (e.g., nome da música, ou o próprio arquivo completo), garantindo que apenas usuários com a chave correta possam acessar o conteúdo.
