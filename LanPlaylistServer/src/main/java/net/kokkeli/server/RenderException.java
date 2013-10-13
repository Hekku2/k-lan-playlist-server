package net.kokkeli.server;

/**
 * Render exception, thrown when rendering template fails.
 * @author Hekku2
 *
 */
public class RenderException extends Exception {

    private static final long serialVersionUID = 1220628111666264054L;

    /**
     * Creates new server exception with given message
     * @param message Message
     */
    public RenderException(String message, Exception inner){
        super(message, inner);
    }
    
    /**
     * Creates new server exception with given message
     * @param message Message
     */
    public RenderException(String message){
        super(message);
    }
}
