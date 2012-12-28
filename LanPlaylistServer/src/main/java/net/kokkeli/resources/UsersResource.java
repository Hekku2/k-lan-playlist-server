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
import net.kokkeli.server.ITemplateService;
import net.kokkeli.server.LanServer;
import net.kokkeli.server.RenderException;

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
    private static final String USER_CREATE_TEMPLATE = "user_create.ftl";
    
    private static final String FORM_USERNAME = "username";
    private static final String FORM_ID = "id";
    private static final String FORM_ROLE = "role";
    
    private IUserService userService;
    
    /**
     * Creates users resource.
     * @param logger
     */
    @Inject
    protected UsersResource(ILogger logger, ITemplateService templateService, IUserService userservice) {
        super(logger, templateService);
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

        return Response.ok(templates.process(USERS_TEMPLATE, modelUsers)).build();
    }
    
    /**
     * Get for User details
     * @param req Request
     * @param id Requested Id
     * @return Response
     * @throws NotFoundException Thrown if id was not found.
     * @throws ServiceException Thrown if there was problem with services.
     */
    @GET
    @Produces("text/html")
    @Access(Role.ADMIN)
    @Path("{id: [0-9]*}")
    public Response userDetails(@Context HttpServletRequest req, @PathParam("id") long id) throws NotFoundException, ServiceException{
        User user = userService.get(id);
        ModelUser model = new ModelUser(user.getId(), user.getUserName(), user.getRole());
        
        try {
            return Response.ok(templates.process(USER_DETAILS_TEMPLATE, model)).build();
        } catch (RenderException e) {
            throw new ServiceException("There was problem with rendering the template.", e);
        }
    }
    
    /**
     * Get for User edit
     * @param req Request
     * @param id Requested Id.
     * @return Response
     * @throws NotFoundException Thrown if id was not found.
     * @throws ServiceException Thrown if there was problem with services.
     */
    @GET
    @Produces("text/html")
    @Access(Role.ADMIN)
    @Path("/edit/{id: [0-9]*}")
    public Response userEdit(@Context HttpServletRequest req, @PathParam("id") long id) throws NotFoundException, ServiceException{
        User user = userService.get(id);
        ModelUser model = new ModelUser(user.getId(), user.getUserName(), user.getRole());
        
        try {
            return Response.ok(templates.process(USER_EDIT_TEMPLATE, model)).build();
        } catch (RenderException e) {
            throw new ServiceException("There was problem with rendering the template.", e);
        }
    }
    
    /**
     * Post for User edit.
     * @param req Request
     * @param formParams Form parameters
     * @return Response
     * @throws NotFoundException Thrown if id was not found.
     * @throws ServiceException Thrown if there was problem with services.
     * @throws BadRequestException Thrown if request is bad.
     */
    @POST
    @Produces("text/html")
    @Access(Role.ADMIN)
    @Path("/edit/{id: [0-9]*}")
    public Response userEdit(@Context HttpServletRequest req, MultivaluedMap<String, String> formParams) throws NotFoundException, ServiceException, BadRequestException{
        containsNeededFieldsForEdit(formParams);
        User editedUser = createEditUser(formParams);
        
        // Checks that user exists
        User user = userService.get(editedUser.getId());
        
        if (!isUsernameValid(user.getUserName()))
            throw new BadRequestException("Username was invalid.");

        
        
        //TODO Field validation
        userService.update(new User(editedUser.getId(), editedUser.getUserName(), editedUser.getRole()));
        
        return Response.seeOther(LanServer.getURI(String.format("users/%s", user.getId()))).build();
    }

    /**
     * User create get.
     * @param req Request
     * @return Response
     * @throws ServiceException Thrown if rendering fails.
     */
    @GET
    @Produces("text/html")
    @Access(Role.ADMIN)
    @Path("/create/")
    public Response userCreate(@Context HttpServletRequest req) throws ServiceException{
        try {
            return Response.ok(templates.process(USER_CREATE_TEMPLATE)).build();
        } catch (RenderException e) {
            throw new ServiceException("Template processing failed.", e);
        }
    }
    
    /**
     * User create post.
     * @param req Request
     * @param formParams Form parameters
     * @return Response
     * @throws ServiceException Thrown if rendering fails.
     * @throws BadRequestException Thrown if request contained invalid values.
     */
    @POST
    @Produces("text/html")
    @Access(Role.ADMIN)
    @Path("/create/")
    public Response userCreate(@Context HttpServletRequest req, MultivaluedMap<String, String> formParams) throws BadRequestException, ServiceException{
        containsNeededFieldsForCreate(formParams);
        User user = createUser(formParams);
        
        //TODO Validation for user values.
        if (!isUsernameValid(user.getUserName()))
            throw new BadRequestException("Username was invalid.");
        
        userService.add(user);
        
        return Response.seeOther(LanServer.getURI("users")).build();
    }
    
    /**
     * Checks that form contains needed fields for edit. (Username, role and id)
     * @param formParams
     * @throws BadRequestException
     */
    private void containsNeededFieldsForEdit(MultivaluedMap<String, String> formParams) throws BadRequestException{
        if (!formParams.containsKey(FORM_ID)){
            throw new BadRequestException("User edit post did not contain needed fields.");
        }
        
        containsNeededFieldsForCreate(formParams);
    }
    
    /**
     * Checks that form contains needed field for create. (Username and role)
     * @param formParams
     * @throws BadRequestException
     */
    private void containsNeededFieldsForCreate(MultivaluedMap<String, String> formParams) throws BadRequestException{
        if (!formParams.containsKey(FORM_USERNAME) || !formParams.containsKey(FORM_ROLE)){
            throw new BadRequestException("User edit post did not contain needed fields.");
        }
    }
    
    /**
     * Creates new user for edit from formParams. If values are missing, exception is thrown.
     * @param formParams Parameters. Contains all needed field for ModelUser
     * @return Created ModelUser
     * @throws BadRequestException Thrown formParams contain illegal input.
     */
    private User createEditUser(MultivaluedMap<String, String> formParams) throws BadRequestException{
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
    
    /**
     * Creates user from formParams. If username or role is missing, exception is thrown.
     * @param formParams
     * @return
     * @throws BadRequestException
     */
    private User createUser(MultivaluedMap<String, String> formParams) throws BadRequestException{
        String username = formParams.getFirst(FORM_USERNAME).trim();
        
        Role role;
        try {
            role = Role.valueOf(formParams.getFirst(FORM_ROLE).toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("There was no such role.", e);
        }
        
        return new User(username,role);
    }
    
    /**
     * Checks for username validity
     * @param username Username
     * @return True, if username is valid
     */
    private static boolean isUsernameValid(String username){
        //TODO Check for invalid charachters
        return username != null && !username.isEmpty() && username.length() <= 255;
    }
}
