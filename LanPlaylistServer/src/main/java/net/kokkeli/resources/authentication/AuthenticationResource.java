package net.kokkeli.resources.authentication;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;

import com.google.inject.Inject;

import net.kokkeli.data.Logging;
import net.kokkeli.data.Role;
import net.kokkeli.resources.Access;
import net.kokkeli.resources.BaseResource;
import net.kokkeli.server.LanServer;
import net.kokkeli.server.RenderException;
import net.kokkeli.server.Templates;

/**
 * Authentication resource. This class doesn't need access control.
 * @author Hekku2
 *
 */
@Path("/authentication")
public class AuthenticationResource extends BaseResource {
    
    @Inject
    protected AuthenticationResource(Logging logger) {
        super(logger);
    }

    private static final String AUTHENTICATE_TEMPLATE = "authenticate.ftl";
    
    @GET
    @Produces("text/html")
    @Access(Role.NONE)
    public String authenticate() throws RenderException{
        return Templates.process(AUTHENTICATE_TEMPLATE);
    }
    
    @POST
    @Consumes("application/x-www-form-urlencoded")
    @Produces("text/html")
    @Access(Role.NONE)
    public Response authenticate(@FormParam("user") String username, @FormParam("pwd") String password) throws RenderException{
        log("User " + username + " trying to authenticate.", 1);
        
        //TODO Add authentication here
        
        return Response.seeOther(LanServer.getBaseURI())
                .cookie(new NewCookie("auth", "I am the authentication cookie!"))
                .build();
    }
}
