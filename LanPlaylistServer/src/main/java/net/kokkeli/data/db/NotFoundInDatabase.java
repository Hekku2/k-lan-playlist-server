package net.kokkeli.data.db;

/**
 * Class for exceptions thrown by server.
 * @author Hekku2
 *
 */
public class NotFoundInDatabase extends Exception{
    private static final long serialVersionUID = -4823687651337284754L;

    /**
     * Creas new not found in database exception with given message and inner exception.
     * @param message Message
     * @param inner Inner exception.
     */
    public NotFoundInDatabase(String message, Exception inner){
        super(message, inner);
    }
    
    /**
     * Creates new not found in database exception with given message.
     * @param message
     */
    public NotFoundInDatabase(String message){
        super(message);
    }
}