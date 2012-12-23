package net.kokkeli.resources;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
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
import net.kokkeli.server.LanServer;
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
    
    private static final String FORM_USERNAME = "username";
    private static final String FORM_ID = "id";
    private static final String FORM_ROLE = "role";
    
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
    public Response userDetails(@Context HttpServletRequest req, @PathParam("id") long id) throws RenderException, NotFoundException, ServiceException{
        User user = userService.get(id);
        ModelUser model = new ModelUser(user.getId(), user.getUserName(), user.getRole());
        
        return Response.ok(Templates.process(USER_DETAILS_TEMPLATE, model)).build();
    }
    
    @GET
    @Produces("text/html")
    @Access(Role.ADMIN)
    @Path("/edit/{id: [0-9]*}")
    public Response userEdit(@Context HttpServletRequest req, @PathParam("id") long id) throws NotFoundException, ServiceException, RenderException{
        User user = userService.get(id);
        ModelUser model = new ModelUser(user.getId(), user.getUserName(), user.getRole());
        
        return Response.ok(Templates.process(USER_EDIT_TEMPLATE, model)).build();
    }
    
    @POST
    @Produces("text/html")
    @Access(Role.ADMIN)
    @Path("/edit/{id: [0-9]*}")
    public Response userEdit(@Context HttpServletRequest req, MultivaluedMap<String, String> formParams) throws NotFoundException, ServiceException, RenderException, BadRequestException{
        containsNeededFields(formParams);
        User editedUser = createUser(formParams);
        
        // Checks that user exists
        User user = userService.get(editedUser.getId());
        
        if (user.getUserName().isEmpty())
            throw new BadRequestException("Username was empty, or contained only whitespace.");
        
        if (user.getUserName().length() > 255)
            throw new BadRequestException("Username was too long.");
        
        userService.update(new User(editedUser.getId(), editedUser.getUserName(), editedUser.getRole()));
        
        return Response.seeOther(LanServer.getURI(String.format("users/%s", user.getId()))).build();
    }
    
    /**
     * Checks that 
     * @param formParams
     * @throws BadRequestException
     */
    private void containsNeededFields(MultivaluedMap<String, String> formParams) throws BadRequestException{
        if (!formParams.containsKey(FORM_ID) || !formParams.containsKey(FORM_USERNAME) || !formParams.containsKey(FORM_ROLE)){
            throw new BadRequestException("User edit post did not contain needed fields.");
        }
    }
    
    /**
     * Creates new ModelUser from formParams.
     * @param formParams Parameters. Contains all needed field for ModelUser
     * @return Created ModelUser
     * @throws BadRequestException Thrown formParams contain illegal input.
     */
    private User createUser(MultivaluedMap<String, String> formParams) throws BadRequestException{
        long id;
        
        try {
            id = Long.parseLong(formParams.getFirst(FORM_ID));
        } catch (NumberFormatException e) {
            throw new BadRequestException("Id was not in correct format.", e);
        }
        
        String username = formParams.getFirst(FORM_USERNAME).trim();
        Role role;
        
        try {
            role = Role.valueOf(formParams.getFirst(FORM_ROLE).toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("There was no such role.", e);
        }
        
        
        return new User(id, username, role);
    }
}
