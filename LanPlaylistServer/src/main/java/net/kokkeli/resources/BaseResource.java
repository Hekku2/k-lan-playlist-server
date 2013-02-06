package net.kokkeli.resources;

import javax.servlet.http.HttpServletRequest;

import net.kokkeli.data.ILogger;
import net.kokkeli.data.Session;
import net.kokkeli.data.db.NotFoundInDatabase;
import net.kokkeli.data.services.ISessionService;
import net.kokkeli.data.services.ServiceException;
import net.kokkeli.player.IPlayer;
import net.kokkeli.resources.authentication.AuthenticationCookieNotFound;
import net.kokkeli.resources.authentication.AuthenticationUtils;
import net.kokkeli.resources.models.BaseModel;
import net.kokkeli.server.ITemplateService;

/**
 * Abstract class for basic resource
 * @author Hekku2
 * 
 */
public abstract class BaseResource {
    private final ILogger logger;
    protected final IPlayer player;
    protected final ITemplateService templates;
    protected final ISessionService sessions;
    
    /**
     * Initializes class.
     * @param logger
     */
    protected BaseResource(final ILogger logger, final ITemplateService templateService, final IPlayer player, final ISessionService sessions){
        this.logger = logger;
        this.templates = templateService;
        this.player = player;
        this.sessions = sessions;
    }
    
    /**
     * Logs given message.
     * @param message Message
     * @param severity Severity
     */
    protected void log(String message, int severity){
        logger.log(message, severity);
    }
    
    
    /**
     * Build a base model from request. Exception is only thrown when user can't be matched to authentication.
     * This should never happen, because authentication checking should be used before this.
     * @param req Request
     * @return Base model
     * @throws ServiceException Thrown is user can't be extracted from request.
     */
    protected final BaseModel buildBaseModel(HttpServletRequest req) throws ServiceException{
        BaseModel model = new BaseModel();
        model.setNowPlaying(player.getTitle());
        
        try {
            Session session = sessions.get(AuthenticationUtils.extractLoginCookie(req.getCookies()).getValue());
            model.setUsername(session.getUser().getUserName());
            model.setCurrentSession(session);
            model.setError(session.getError());
            model.setInfo(session.getInfo());
            sessions.clearError(session.getAuthId());
            sessions.clearInfo(session.getAuthId());
            
        } catch (AuthenticationCookieNotFound e) {
            throw new ServiceException("There was a problem with authentication.", e);
        } catch (NotFoundInDatabase e) {
            throw new ServiceException("There was a problem with authentication.", e);
        }
        
        return model;
    }
    
    /**
     * Builds a base model with empty username.
     * @return
     */
    protected final BaseModel buildBaseModel() {
        BaseModel model = new BaseModel();
        model.setNowPlaying(player.getTitle());
        model.setUsername("");
        return model;
    }
}
