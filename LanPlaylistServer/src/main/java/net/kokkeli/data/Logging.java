package net.kokkeli.data;

import java.util.ArrayList;

/**
 * Logging class.
 * @author Hekku2
 *
 */
public class Logging implements ILogger {
    private static Logging logger = new Logging();
    
    private ArrayList<String> logLines = new ArrayList<String>();
    
    /**
     * Creates new instance of logging.
     */
    public Logging(){
        
    }
    
    /**
     * Writes given message to log.
     * @param message Message to write
     * @param severity Severity of message
     */
    public void log(String message, LogSeverity severity){
        logger.logLines.add(severity + ": " + message);
        System.out.println(severity + ": " + message);
    }
}
