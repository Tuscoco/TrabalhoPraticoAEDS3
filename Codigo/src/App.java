import java.io.FileNotFoundException;
import java.io.IOException;

import util.ConsoleHelper;
import util.LogLevel;
import util.Logger;

public class App {

    /*
     * Método Principal
     * 
     * Funcionamento:
     * 
     * Inicializa o arquivo de log e o ConsoleHelper, iniciando a interação com o usuario
     * chamando o método ConsoleHelper.run
     */
    public static void main(String[] args) throws FileNotFoundException, IOException {

        Logger.reiniciarLog();

        Logger.log(LogLevel.INFO, "Programa executado!");
    
        ConsoleHelper console = new ConsoleHelper();

        console.run();

        Logger.log(LogLevel.INFO, "Programa encerrado!");
    
    }
    
}