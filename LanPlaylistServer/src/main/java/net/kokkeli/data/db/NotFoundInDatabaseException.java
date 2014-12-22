package net.kokkeli.data.db;

/**
 * Class for exceptions thrown by server.
 * @author Hekku2
 *
 */
public class NotFoundInDatabaseException extends Exception{
    private static final long serialVersionUID = -4823687651337284754L;

    /**
     * Creas new not found in database exception with given message and inner exception.
     * @param message Message
     * @param inner Inner exception.
     */
    public NotFoundInDatabaseException(String message, Exception inner){
        super(message, inner);
    }
    
    /**
     * Creates new not found in database exception with given message.
     * @param message
     */
    public NotFoundInDatabaseException(String message){
        super(message);
    }
}