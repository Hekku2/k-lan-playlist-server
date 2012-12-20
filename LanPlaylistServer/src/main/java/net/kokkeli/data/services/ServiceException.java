package net.kokkeli.data.services;

/**
 * Exception thrown by services.
 * @author Hekku2
 *
 */
public class ServiceException extends Exception {

    private static final long serialVersionUID = -8809845997832250335L;

    /**
     * Creates new service exception with given message and inner exception
     * @param message Message
     * @param inner Inner exception
     */
    public ServiceException(String message, Exception inner){
        super(message, inner);
    }
    
    /**
     * Creates new service exception with given message
     * @param message Message
     */
    public ServiceException(String message){
        super(message);
    }
}
