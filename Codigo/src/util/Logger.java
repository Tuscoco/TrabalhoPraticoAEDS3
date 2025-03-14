package util;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/* Classe de log
 * 
 * Classe com o propósito de criar e preencher um arquivo de log
 * para facilitar o debug e desenvolvimento da aplicação
 */
public class Logger {
    
    private static final String file = "debug/logs/debug.log";

    //Imprime no arquivo de log as informações passadas
    private static void logToFile(LogLevel level, String message) throws IOException{

        try(FileWriter writer = new FileWriter(file, true)){

            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));

            writer.write("[" + timestamp + "] [" + level + "] " + message + "\n");

        }catch(IOException e){

            e.getMessage();

        }

    }

    //Recebe as informações e chama o método de logToFile
    public static void log(LogLevel level, String message) throws IOException{

        logToFile(level, message);

    }

    //Reinicia o arquivo de log
    public static void reiniciarLog() throws IOException{

        try(FileWriter writer = new FileWriter(file, false)){

        }catch(IOException e){

            e.getMessage();

        }

    }

}
