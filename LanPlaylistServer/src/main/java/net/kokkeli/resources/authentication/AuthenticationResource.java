package net.kokkeli.resources.authentication;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;

import com.google.inject.Inject;

import net.kokkeli.data.ILogger;
import net.kokkeli.data.Role;
import net.kokkeli.data.Session;
import net.kokkeli.data.User;
import net.kokkeli.data.db.NotFoundInDatabase;
import net.kokkeli.data.services.ISessionService;
import net.kokkeli.data.services.IUserService;
import net.kokkeli.data.services.ServiceException;
import net.kokkeli.player.IPlayer;
import net.kokkeli.resources.Access;
import net.kokkeli.resources.BaseResource;
import net.kokkeli.resources.models.BaseModel;
import net.kokkeli.server.ITemplateService;
import net.kokkeli.server.LanServer;
import net.kokkeli.server.RenderException;

/**
 * Authentication resource. This class doesn't need access control.
 * 
 * @author Hekku2
 * 
 */
@Path("/authentication")
public class AuthenticationResource extends BaseResource {
    private static final String AUTHENTICATE_TEMPLATE = "authenticate.ftl";
    
    private final IUserService users;

    @Inject
    protected AuthenticationResource(ILogger logger, ITemplateService templateService, IPlayer player, ISessionService sessions, IUserService users) {
        super(logger, templateService, player, sessions);
        this.users = users;
    }

    /**
     * Shows Authenticaiton page
     * @return Authentication page response
     * @throws ServiceException Thrown if authentication page can't be shown.
     */
    @GET
    @Produces("text/html")
    @Access(Role.NONE)
    public Response authenticate() throws ServiceException {
        BaseModel model = buildBaseModel();
        model.setUsername("");
        
        try {
            return Response.ok(templates.process(AUTHENTICATE_TEMPLATE, model)).build();
        } catch (RenderException e) {
            //in this case, service exception can be thrown, because there is nothing to be done if authentication page can't be shown.
            throw new ServiceException("There was problem with rendering.");
        }
    }

    /**
     * Handles authentication post.
     * @param req Request
     * @param username Username
     * @param password Password
     * @return Response
     */
    @POST
    @Consumes("application/x-www-form-urlencoded")
    @Produces("text/html")
    @Access(Role.NONE)
    public Response authenticate(@Context HttpServletRequest req,
            @FormParam("user") String username,
            @FormParam("pwd") String password) {
        log("User " + username + " trying to authenticate.", 1);
        BaseModel model = buildBaseModel();
        
        User user;
        try {
            user = users.get(username, password);
        } catch (NotFoundInDatabase exception) {
            return handleWrongUsernameOrPassword(model);
        } catch (ServiceException e){
            return handleServiceException(model);
        }
        
        Session session = sessions.createSession(user); 
        try {
            Cookie cook = AuthenticationUtils.extractLoginCookie(req.getCookies());
            log("Old cookie found, modifying...", 0);
            NewCookie modified = new NewCookie(cook.getName(), session.getAuthId(), "/",
                    cook.getDomain(), cook.getComment(), cook.getMaxAge(),
                    cook.getSecure());

            return Response.seeOther(LanServer.getBaseURI()).cookie(modified)
                    .build();
            
        } catch (AuthenticationCookieNotFound e) {
            log("No old cookie found, creating new.", 0);
            NewCookie auth = new NewCookie("auth", session.getAuthId());
            return Response.seeOther(LanServer.getBaseURI()).cookie(auth)
                    .build();
        }

    }

    /**
     * Logs user out.
     * @param req Request
     * @return Response
     */
    @GET
    @Path("/logout")
    public Response logout(@Context HttpServletRequest req) {
        log("User logget out.", 1);

        Cookie cook = null;
        try {
            cook = AuthenticationUtils.extractLoginCookie(req.getCookies());
        } catch (AuthenticationCookieNotFound e) {
            log("No authentication found.", 1);
            return Response.seeOther(LanServer.getBaseURI()).build();
        }

        NewCookie modified = new NewCookie(cook.getName(), "", "/",
                cook.getDomain(), cook.getComment(), cook.getMaxAge(),
                cook.getSecure());

        return Response.seeOther(LanServer.getBaseURI()).cookie(modified)
                .build();
    }
    
    /**
     * Handles case for wrong username or password. Returns authentication page response with error.
     * @param model Basemodel
     * @return Response with error
     */
    private Response handleWrongUsernameOrPassword(BaseModel model){
        try {
            //Shows error if username or password is wrong.
            model.setUsername("");
            model.setError("Wrong username or password.");
            return Response.ok(templates.process(AUTHENTICATE_TEMPLATE, model)).build();
        } catch (RenderException e) {
            return handleRenderingError(model);
        }
    }
}
