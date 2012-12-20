package net.kokkeli.resources;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import com.google.inject.Inject;
import com.sun.jersey.api.NotFoundException;

import net.kokkeli.data.ILogger;
import net.kokkeli.data.Role;
import net.kokkeli.data.User;
import net.kokkeli.data.services.IUserService;
import net.kokkeli.data.services.ServiceException;
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
    private static final String USER_DETAILS_TEMPLATE = "user.ftl";
    private static final String USER_EDIT_TEMPLATE = "user_edit.ftl";
    
    private IUserService userService;
    
    /**
     * Creates users resource.
     * @param logger
     */
    @Inject
    protected UsersResource(ILogger logger, IUserService userservice) {
        super(logger);
        this.userService = userservice;
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
        ModelUsers modelUsers = new ModelUsers();
        
        Collection<User> users = userService.get();
        for (User user : users) {
            modelUsers.add(new ModelUser(user.getId(), user.getUserName(), user.getRole()));
        }      

        return Response.ok(Templates.process(USERS_TEMPLATE, modelUsers)).build();
    }
    
    @GET
    @Produces("text/html")
    @Access(Role.ADMIN)
    @Path("{id: [0-9]*}")
    public Response userDetails(@Context HttpServletRequest req, @PathParam("id") int id) throws RenderException, NotFoundException, ServiceException{
        User user = userService.get(id);
        ModelUser model = new ModelUser(user.getId(), user.getUserName(), Role.ADMIN);
        
        return Response.ok(Templates.process(USER_DETAILS_TEMPLATE, model)).build();
    }
    
    @GET
    @Produces("text/html")
    @Access(Role.ADMIN)
    @Path("/edit/{id: [0-9]*}")
    public Response userEdit(@Context HttpServletRequest req, @PathParam("id") int id) throws NotFoundException, ServiceException, RenderException{
        User user = userService.get(id);
        ModelUser model = new ModelUser(user.getId(), user.getUserName(), user.getRole());
        
        return Response.ok(Templates.process(USER_EDIT_TEMPLATE, model)).build();
    }
}
