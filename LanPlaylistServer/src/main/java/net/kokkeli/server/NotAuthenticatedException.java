package net.kokkeli.server;

/**
 * Not authenticated exception
 * @author Hekku2
 *
 */
public class NotAuthenticatedException extends Exception {
    private static final long serialVersionUID = -4965541199856654411L;

    /**
     * Creates new NotAuthenticatedException with given message and inner exception
     * @param message Message
     * @param inner Inner exception
     */
    public NotAuthenticatedException(String message, Exception inner){
        super(message, inner);
    }
    
    /**
     * Creates new NotAuthenticatedException.
     * @param message Message
     */
    public NotAuthenticatedException(String message){
        super(message);
    }
}
