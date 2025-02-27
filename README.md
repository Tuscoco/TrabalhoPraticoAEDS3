# TrabalhoPraticoAEDS3

# TP1 - Sistema de CRUD Sequencial com Ordenação Externa

Este projeto consiste em um sistema implementado em Java que realiza operações de CRUD (Create, Read, Update, Delete) sobre um arquivo binário, utilizando manipulação de arquivo sequencial. O sistema também inclui uma funcionalidade de ordenação externa para otimizar o armazenamento de dados no arquivo.

## Estrutura do Sistema

O sistema possui as seguintes funcionalidades principais:

- **Carga da base de dados**: Importação de dados de um arquivo CSV, rota de API ou outro formato escolhido, para um arquivo binário.
- **Leitura de registros**: O sistema pode ler um registro específico de acordo com o ID.
- **Atualização de registros**: Atualiza um registro no arquivo binário, respeitando as alterações de tamanho.
- **Exclusão de registros**: Marca um registro como deletado, sem removê-lo fisicamente do arquivo, mas com uma marcação de lápide.
- **Ordenação externa**: Implementação de ordenação externa com o uso de múltiplos caminhos e controle de registros em memória, removendo espaços em branco de registros atualizados ou deletados.

## Estrutura do Arquivo Binário

O arquivo binário é estruturado da seguinte forma:

- **Cabeçalho**: Um inteiro que armazena o último ID utilizado.
- **Registros**:
  - **Lápide (Byte)**: Indica se o registro é válido ou foi excluído.
  - **Indicador de Tamanho do Registro (Inteiro)**: Indica o tamanho do vetor de bytes.
  - **Vetor de Bytes**: Contém os dados do objeto em formato binário.

## Funcionalidades do CRUD Sequencial

### 1. Carga da Base de Dados
O sistema permite a importação de dados de arquivos CSV ou de uma rota de API para um arquivo binário. O cabeçalho do arquivo contém o último ID utilizado, e cada registro é armazenado com lápide, indicador de tamanho e os dados binários.

### 2. Leitura de um Registro
Este método recebe um ID como parâmetro e percorre o arquivo binário até encontrar o registro correspondente, retornando os dados desse registro.

### 3. Atualização de Registro
Quando um registro é atualizado:
- Se o tamanho do registro não mudar, os dados são atualizados diretamente no local do arquivo binário.
- Se o registro mudar de tamanho, o registro anterior é marcado como excluído (com lápide) e o novo registro é adicionado ao final do arquivo.

### 4. Deleção de Registro
Este método recebe um ID como parâmetro e marca o registro correspondente como excluído, utilizando a lápide para indicar que o registro foi deletado.

## Ordenação Externa

A ordenação externa tem como objetivo reorganizar os registros no arquivo binário. O processo é realizado em múltiplos caminhos, controlando o número máximo de registros que podem ser mantidos em memória durante cada ordenação.

### Parâmetros:
- **Número de Caminhos**: Define o número de caminhos para a ordenação.
- **Número de Registros Máximos em Memória**: Limita a quantidade de registros que podem ser mantidos em memória durante o processo de ordenação.

Após a ordenação, o arquivo binário será reescrito com os registros atualizados e excluídos removidos.

