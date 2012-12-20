package net.kokkeli.data.db;

/**
 * Class for exceptions thrown by server.
 * @author Hekku2
 *
 */
public class DatabaseException extends Exception{
    private static final long serialVersionUID = -4823687651337284754L;

    /**
     * Creas new database exception with given message and inner exception.
     * @param message Message
     * @param inner Inner exception.
     */
    public DatabaseException(String message, Exception inner){
        super(message, inner);
    }
    
    /**
     * Creates new database exception with given message.
     * @param message
     */
    public DatabaseException(String message){
        super(message);
    }
}