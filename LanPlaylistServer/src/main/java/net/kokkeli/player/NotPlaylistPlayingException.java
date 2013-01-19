package net.kokkeli.player;

/**
 * Exception thrown when no playlist is playing.
 * @author Hekku2
 *
 */
public class NotPlaylistPlayingException extends Exception {
    private static final long serialVersionUID = -4823687651337284754L;

    /**
     * Creas new exception with given message and inner exception.
     * @param message Message
     * @param inner Inner exception.
     */
    public NotPlaylistPlayingException(String message, Exception inner){
        super(message, inner);
    }
    
    /**
     * Creates new exception with given message.
     * @param message
     */
    public NotPlaylistPlayingException(String message){
        super(message);
    }
}
