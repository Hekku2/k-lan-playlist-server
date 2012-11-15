package net.kokkeli.resources;

import net.kokkeli.data.Logging;

/**
 * Abstract class for basic resource
 * @author Hekku2
 * 
 */
public abstract class BaseResource {
    private final Logging logger;
    
    /**
     * Initializes class.
     * @param logger
     */
    protected BaseResource(final Logging logger){
        this.logger = logger;
    }
    
    /**
     * Logs given message.
     * @param message Message
     * @param severity Severity
     */
    protected void log(String message, int severity){
        logger.log(message, severity);
    }
}
