package net.kokkeli.resources;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import com.google.inject.Inject;

import net.kokkeli.data.Logging;
import net.kokkeli.data.Role;
import net.kokkeli.resources.models.ModelUser;
import net.kokkeli.resources.models.ModelUsers;
import net.kokkeli.server.RenderException;
import net.kokkeli.server.Templates;

/**
 * Users-resource
 * 
 * Resources related to user viewing/editing/creating
 * @author Hekku2
 *
 */
@Path("/users")
public class UsersResource extends BaseResource {
    private static final String USERS_TEMPLATE = "users.ftl";
    
    /**
     * Creates users resource.
     * @param logger
     */
    @Inject
    protected UsersResource(Logging logger) {
        super(logger);
    }
    
    /**
     * Shows list of users
     * @return HTML-page for user list
     * @throws RenderException Thrown if there is problem with rendering template
     */
    @GET
    @Produces("text/html")
    @Access(Role.ADMIN)
    public Response userList(@Context HttpServletRequest req) throws RenderException {
        ArrayList<ModelUser> mockUsers = new ArrayList<ModelUser>();
        
        for (int i = 0; i < 17; i++) {
            mockUsers.add(new ModelUser("RandomName" + i,Role.ADMIN));
        }
        
        ModelUsers modelUsers = new ModelUsers();
        modelUsers.add(mockUsers);
        

        return Response.ok(Templates.process(USERS_TEMPLATE, modelUsers)).build();
    }
}
