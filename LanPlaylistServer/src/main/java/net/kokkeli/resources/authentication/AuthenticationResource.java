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
import net.kokkeli.data.services.ISessionService;
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

    @Inject
    protected AuthenticationResource(ILogger logger, ITemplateService templateService, IPlayer player, ISessionService sessions) {
        super(logger, templateService, player, sessions);
    }

    @GET
    @Produces("text/html")
    @Access(Role.NONE)
    public String authenticate() throws ServiceException {
        BaseModel model = buildBaseModel();
        model.setUsername("");
        
        try {
            return templates.process(AUTHENTICATE_TEMPLATE, model);
        } catch (RenderException e) {
            throw new ServiceException("There was problem with rendering.");
        }
    }

    @POST
    @Consumes("application/x-www-form-urlencoded")
    @Produces("text/html")
    @Access(Role.NONE)
    public Response authenticate(@Context HttpServletRequest req,
            @FormParam("user") String username,
            @FormParam("pwd") String password) {
        log("User " + username + " trying to authenticate.", 1);

        // TODO Add authentication here

        Cookie cook = null;
        try {
            cook = AuthenticationUtils.extractLoginCookie(req.getCookies());
        } catch (AuthenticationCookieNotFound e) {
            // TODO Add authentication here
            log("No old cookie found, creating new.", 0);
            NewCookie auth = new NewCookie("auth", "Ok");
            return Response.seeOther(LanServer.getBaseURI()).cookie(auth)
                    .build();
        }
        log("Old cookie found, modifying...", 0);
        NewCookie modified = new NewCookie(cook.getName(), "Ok", "/",
                cook.getDomain(), cook.getComment(), cook.getMaxAge(),
                cook.getSecure());

        return Response.seeOther(LanServer.getBaseURI()).cookie(modified)
                .build();
    }

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
}
