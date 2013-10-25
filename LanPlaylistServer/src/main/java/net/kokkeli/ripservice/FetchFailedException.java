package net.kokkeli.ripservice;

/**
 * Exception representing that should be thrown when fetching item fails.
 * @author Hekku2
 */
public class FetchFailedException extends Exception {
    private static final long serialVersionUID = 894445639727484004L;
    
    /**
     * Creates fetch failed exception
     * @param message Message
     */
    public FetchFailedException(String message){
        super(message);
    }
}
