package util;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {
    
    private static final String file = "debug/logs/debug.log";

    private static void logToFile(LogLevel level, String message) throws IOException{

        try(FileWriter writer = new FileWriter(file, true)){

            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));

            writer.write("[" + timestamp + "] [" + level + "] " + message + "\n");

        }catch(IOException e){

            e.getMessage();

        }

    }

    public static void log(LogLevel level, String message) throws IOException{

        logToFile(level, message);

    }

    public static void reiniciarLog() throws IOException{

        try(FileWriter writer = new FileWriter(file, false)){

        }catch(IOException e){

            e.getMessage();

        }

    }

}
