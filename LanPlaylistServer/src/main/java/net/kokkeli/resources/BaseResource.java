package net.kokkeli.resources;

import net.kokkeli.data.ILogger;
import net.kokkeli.server.ITemplateService;

/**
 * Abstract class for basic resource
 * @author Hekku2
 * 
 */
public abstract class BaseResource {
    private final ILogger logger;
    protected final ITemplateService templates;
    
    /**
     * Initializes class.
     * @param logger
     */
    protected BaseResource(final ILogger logger, final ITemplateService templateService){
        this.logger = logger;
        this.templates = templateService;
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
