package net.kokkeli.resources;

/**
 * Exception thrown when request is bad.
 * @author Hekku2
 *
 */
public class BadRequestException extends Exception {
    private static final long serialVersionUID = -4965541199856654411L;

    /**
     * Creates new bad request exception with given message and inner exception
     * @param message Message
     * @param inner Inner exception
     */
    public BadRequestException(String message, Exception inner){
        super(message, inner);
    }
    
    /**
     * Creates new bad request exception with given message
     * @param message Message
     */
    public BadRequestException(String message){
        super(message);
    }
}
