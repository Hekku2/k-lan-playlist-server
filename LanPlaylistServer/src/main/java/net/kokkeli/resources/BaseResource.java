package net.kokkeli.resources;

import net.kokkeli.data.ILogger;
import net.kokkeli.player.IPlayer;
import net.kokkeli.resources.models.BaseModel;
import net.kokkeli.server.ITemplateService;

/**
 * Abstract class for basic resource
 * @author Hekku2
 * 
 */
public abstract class BaseResource {
    private final ILogger logger;
    private final IPlayer player;
    protected final ITemplateService templates;
    
    /**
     * Initializes class.
     * @param logger
     */
    protected BaseResource(final ILogger logger, final ITemplateService templateService, final IPlayer player){
        this.logger = logger;
        this.templates = templateService;
        this.player = player;
    }
    
    /**
     * Logs given message.
     * @param message Message
     * @param severity Severity
     */
    protected void log(String message, int severity){
        logger.log(message, severity);
    }
    
    protected BaseModel buildBaseModel(){
        BaseModel model = new BaseModel();
        
        model.setNowPlaying(player.getTitle());
        
        return model;
    }
}
