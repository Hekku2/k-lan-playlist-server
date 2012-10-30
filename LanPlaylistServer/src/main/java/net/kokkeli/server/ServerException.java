package net.kokkeli.server;

/**
 * Class for exceptions thrown by server.
 * @author Hekku2
 *
 */
public class ServerException extends Exception{
    private static final long serialVersionUID = 5547184743701708781L;
    
    /**
     * Creas new server exception with given message
     * @param message Message
     */
    public ServerException(String message){
        super(message);
    }
}
