package net.kokkeli.resources.authentication;

/**
 * Exception for authentication errors
 * @author Hekku2
 *
 */
public class AuthenticationException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     * Creates new server exception with given message
     * @param message Message
     */
    public AuthenticationException(String message){
        super(message);
    }
}
