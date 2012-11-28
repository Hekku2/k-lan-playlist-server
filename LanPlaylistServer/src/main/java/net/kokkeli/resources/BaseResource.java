package net.kokkeli.resources;

import net.kokkeli.data.ILogger;

/**
 * Abstract class for basic resource
 * @author Hekku2
 * 
 */
public abstract class BaseResource {
    private final ILogger logger;
    
    /**
     * Initializes class.
     * @param logger
     */
    protected BaseResource(final ILogger logger){
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
