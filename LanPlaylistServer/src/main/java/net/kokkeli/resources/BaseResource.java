package net.kokkeli.resources;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;

import net.kokkeli.ISettings;
import net.kokkeli.data.ILogger;
import net.kokkeli.data.LogSeverity;
import net.kokkeli.data.Role;
import net.kokkeli.data.Session;
import net.kokkeli.data.db.NotFoundInDatabaseException;
import net.kokkeli.data.services.ISessionService;
import net.kokkeli.data.services.ServiceException;
import net.kokkeli.player.IPlayer;
import net.kokkeli.resources.authentication.AuthenticationCookieNotFound;
import net.kokkeli.resources.authentication.AuthenticationUtils;
import net.kokkeli.resources.models.BaseModel;
import net.kokkeli.server.ITemplateService;
import net.kokkeli.server.NotAuthenticatedException;
import net.kokkeli.server.RenderException;

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
    protected final ISettings settings;
    
    /**
     * Initializes class.
     * @param logger
     */
    protected BaseResource(final ILogger logger, final ITemplateService templateService, final IPlayer player, final ISessionService sessions, final ISettings settings){
        this.logger = logger;
        this.templates = templateService;
        this.player = player;
        this.sessions = sessions;
        this.settings = settings;
    }
    
    /**
     * Logs given message.
     * @param message Message
     * @param severity Severity
     */
    protected final void log(String message, LogSeverity severity){
        logger.log(message, severity);
    }
    
    
    /**
     * Build a base model from request. Exception is only thrown when user can't be matched to authentication.
     * This should never happen, because authentication checking should be used before this.
     * @param req Request
     * @return Base model
     * @throws NotAuthenticatedException Thrown is authentication is invalid for some reason
     */
    protected final BaseModel buildBaseModel(HttpServletRequest req) {
        BaseModel model = new BaseModel();
        model.setNowPlaying(player.getTitle());
        
        if (!loadDataFromSession(req, model))
            loadAnonymousData(model);
        
        model.setAnythingPlaying(player.readyForPlay());
        return model;
    }
    
    private static void loadAnonymousData(BaseModel model) {
        model.setUsername(null);
        model.setUserId(0);
        model.setUserRole(Role.ANYNOMOUS.getId());
    }

    private boolean loadDataFromSession(HttpServletRequest req, BaseModel model){
        try {
            Session session = sessions.get(AuthenticationUtils.extractLoginCookie(req.getCookies()).getValue());
            model.setUsername(session.getUser().getUserName());
            model.setUserId(session.getUser().getId());
            model.setUserRole(session.getUser().getRole().getId());
            model.setCurrentSession(session);
            model.setError(session.getError());
            model.setInfo(session.getInfo());
            sessions.clearError(session.getAuthId());
            sessions.clearInfo(session.getAuthId());
            return true;
        } catch (AuthenticationCookieNotFound | NotFoundInDatabaseException e) {
            return false;
        } 
    }
    
    /**
     * Builds a base model with empty username.
     * @return
     */
    protected final BaseModel buildBaseModel() {
        BaseModel model = new BaseModel();
        model.setNowPlaying(player.getTitle());
        model.setUsername("");
        model.setAuthenticationRequired(settings.getRequireAuthentication());
        return model;
    }
    
    /**
     * Creates response for rendering errors. Redirects user to main page.
     * @param model BaseModel
     * @return Response
     */
    protected final Response handleRenderingError(BaseModel model, RenderException e){
        if (model.isAuthenticated())
            sessions.setError(model.getCurrentSession().getAuthId(), "There was a problem with rendering the template.");
        log("There was a problem with rendering: " + e.getMessage(), LogSeverity.ERROR);
        return Response.seeOther(settings.getURI("")).build();
    }
    
    /**
     * Creates response for service exceptions. Redirects user to main page.
     * @param model Basemodel
     * @return Response
     */
    protected final Response handleServiceException(BaseModel model, ServiceException e){
        if (model.isAuthenticated())
            sessions.setError(model.getCurrentSession().getAuthId(), "Something went wrong with service.");
        log("There was a problem with the service: " + e.getMessage(), LogSeverity.ERROR);
        return Response.seeOther(settings.getURI("")).build();
    }
}
