# Instruções de Compilação e Execução

Este projeto foi implementado em Java. A seguir, estão as instruções para compilar e executar o sistema.

## Pré-requisitos

1. **Java Development Kit (JDK)**:
   - **Versão:** JDK 21 ou superior.
   - **Instalação no Windows:**
     - [Download e instalação do JDK](https://adoptium.net/)
     - Certifique-se de adicionar o `JAVA_HOME` nas variáveis de ambiente e que o `java` e `javac` estejam acessíveis no terminal.
   - **Instalação no Linux:**
     - Para distribuições baseadas em Debian/Ubuntu:
       ```bash
       sudo apt update
       sudo apt install openjdk-11-jdk
       java -version
       ```
     - Para distribuições baseadas em Fedora:
       ```bash
       sudo dnf update
       sudo dnf install java-11-openjdk-devel
       java -version
       ```

2. **Git**:
   - **Instalação no Windows:**
     - [Download do Git](https://git-scm.com/)
     - Verifique a instalação com:
       ```bash
       git --version
       ```
   - **Instalação no Linux:**
     - Para distribuições baseadas em Debian/Ubuntu:
       ```bash
       sudo apt update
       sudo apt install git
       git --version
       ```
     - Para distribuições baseadas em Fedora:
       ```bash
       sudo dnf update
       sudo dnf install git
       git --version
       ```

## Execução:
1. **Clone o repositório**:
   ```bash
   git clone https://github.com/Tuscoco/TrabalhoPraticoAEDS3.git
   ```
2. **Entre no diretório do projeto na pasta Codigo**:
   ```bash
   cd seu_diretorio/TrabalhoPraticoAEDS3/Codigo
   ```
3. **Compile e execute o projeto(há um arquivo makefile pronto para isso)**:
   ```bash
   make run
   ```   
