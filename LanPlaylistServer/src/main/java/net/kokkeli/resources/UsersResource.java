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

import net.kokkeli.ValidationUtils;
import net.kokkeli.data.ILogger;
import net.kokkeli.data.LogSeverity;
import net.kokkeli.data.Role;
import net.kokkeli.data.User;
import net.kokkeli.data.db.NotFoundInDatabase;
import net.kokkeli.data.services.ISessionService;
import net.kokkeli.data.services.IUserService;
import net.kokkeli.data.services.ServiceException;
import net.kokkeli.player.IPlayer;
import net.kokkeli.resources.models.BaseModel;
import net.kokkeli.resources.models.ModelUser;
import net.kokkeli.resources.models.ModelUsers;
import net.kokkeli.server.ITemplateService;
import net.kokkeli.server.LanServer;
import net.kokkeli.server.NotAuthenticatedException;
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
    private static final String USERS_TEMPLATE = "user/users.ftl";
    private static final String USER_DETAILS_TEMPLATE = "user/user.ftl";
    private static final String USER_EDIT_TEMPLATE = "user/user_edit.ftl";
    private static final String USER_CREATE_TEMPLATE = "user/user_create.ftl";
    
    private static final String FORM_USERNAME = "username";
    private static final String FORM_ID = "id";
    private static final String FORM_ROLE = "role";
    
    private IUserService userService;
    
    /**
     * Creates users resource.
     * @param logger
     */
    @Inject
    protected UsersResource(ILogger logger, ITemplateService templateService, IUserService userservice, IPlayer player, ISessionService sessions) {
        super(logger, templateService, player, sessions);
        this.userService = userservice;
    }
    
    /**
     * Shows list of users
     * @return HTML-page for user list
     * @throws ServiceException 
     * @throws NotAuthenticatedException Thrown if there is problem with user session
     * @throws RenderException Thrown if there is problem with rendering template
     */
    @GET
    @Produces("text/html")
    @Access(Role.ADMIN)
    public Response userList(@Context HttpServletRequest req) throws NotAuthenticatedException {
        BaseModel model = buildBaseModel(req);

        try {
            model.setModel(createModelUsers());
            return Response.ok(templates.process(USERS_TEMPLATE, model)).build();
        } catch (RenderException e) {
            return handleRenderingError(model);
        } catch (ServiceException e) {
            return handleServiceException(model);
        }
    }
    
    /**
     * Get for User details
     * @param req Request
     * @param id Requested Id
     * @return Response
     * @throws NotFoundException Thrown if id was not found.
     * @throws ServiceException Thrown if there was problem with services.
     * @throws NotAuthenticatedException Thrown if there is problem with user session
     */
    @GET
    @Produces("text/html")
    @Access(Role.ADMIN)
    @Path("{id: [0-9]*}")
    public Response userDetails(@Context HttpServletRequest req, @PathParam("id") long id) throws NotAuthenticatedException{
        BaseModel model = buildBaseModel(req);
        
        try {
            User user = userService.get(id);
            ModelUser userModel = new ModelUser(user.getId(), user.getUserName(), user.getRole());
            
            model.setModel(userModel);
            
            return Response.ok(templates.process(USER_DETAILS_TEMPLATE, model)).build();
        } catch (RenderException e) {
            return handleRenderingError(model);
        } catch (NotFoundInDatabase e){
            sessions.setError(model.getCurrentSession().getAuthId(), "User not found.");
            return Response.seeOther(LanServer.getURI("users")).build();
        } catch (ServiceException e) {
            return handleServiceException(model);
        }
    }
    
    /**
     * Get for User edit
     * @param req Request
     * @param id Requested Id.
     * @return Response
     * @throws NotFoundException Thrown if id was not found.
     * @throws ServiceException Thrown if there was problem with services.
     * @throws NotAuthenticatedException Thrown is user session is invalid
     */
    @GET
    @Produces("text/html")
    @Access(Role.ADMIN)
    @Path("/edit/{id: [0-9]*}")
    public Response userEdit(@Context HttpServletRequest req, @PathParam("id") long id) throws NotAuthenticatedException{
        BaseModel model = buildBaseModel(req);
        
        try {
            User user = userService.get(id);
            ModelUser modelUser = new ModelUser(user.getId(), user.getUserName(), user.getRole());
            model.setModel(modelUser);
            
            return Response.ok(templates.process(USER_EDIT_TEMPLATE, model)).build();
        } catch (RenderException e) {
            return handleRenderingError(model);
        } catch (NotFoundInDatabase e) {
            sessions.setError(model.getCurrentSession().getAuthId(), "User not found.");
            return Response.seeOther(LanServer.getURI("users")).build();
        } catch (ServiceException e) {
            return handleServiceException(model);
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
     * @throws RenderException 
     * @throws NotAuthenticatedException Thrown is user session is not valid.
     */
    @POST
    @Produces("text/html")
    @Access(Role.ADMIN)
    @Path("/edit/{id: [0-9]*}")
    public Response userEdit(@Context HttpServletRequest req, MultivaluedMap<String, String> formParams) throws BadRequestException, RenderException, NotAuthenticatedException{
        BaseModel model = buildBaseModel(req);
        
        containsNeededFieldsForEdit(formParams);
        ModelUser editedUser = createEditUser(formParams);
        try {
            // Checks that user exists
            User user = userService.get(editedUser.getId());
            
            if (ValidationUtils.isEmpty(editedUser.getUsername())){
                return editPostErrorResponse(model, editedUser, "Username is required.");
            }
            
            if (!ValidationUtils.isValidUsername(editedUser.getUsername())){
                editedUser.setUsername(user.getUserName());
                return editPostErrorResponse(model, editedUser, "Invalid username.");
            }
            
            if (userService.exists(editedUser.getUsername())){
                return editPostErrorResponse(model, editedUser, "Username already exists.");
            }
            
            //TODO Field validation
            userService.update(new User(editedUser.getId(), editedUser.getUsername(), editedUser.getRoleEnum()));
            sessions.setInfo(model.getCurrentSession().getAuthId(), "User edited.");
            return Response.seeOther(LanServer.getURI(String.format("users/%s", user.getId()))).build();
        } catch (NotFoundInDatabase e) {
            sessions.setError(model.getCurrentSession().getAuthId(), "User not found.");
            return Response.seeOther(LanServer.getURI("users")).build();
        } catch (ServiceException e) {
            return handleServiceException(model);
        }
    }

    /**
     * User create get.
     * @param req Request
     * @return Response
     * @throws ServiceException Thrown if rendering fails.
     * @throws NotAuthenticatedException 
     */
    @GET
    @Produces("text/html")
    @Access(Role.ADMIN)
    @Path("/create/")
    public Response userCreate(@Context HttpServletRequest req) throws NotAuthenticatedException{
        BaseModel model = buildBaseModel(req);
        
        try {
            return Response.ok(templates.process(USER_CREATE_TEMPLATE, model)).build();
        } catch (RenderException e) {
            return handleRenderingError(model);
        }
    }
    
    /**
     * User create post.
     * @param req Request
     * @param formParams Form parameters
     * @return Response
     * @throws ServiceException Thrown if rendering fails.
     * @throws BadRequestException Thrown if request contained invalid values.
     * @throws NotAuthenticatedException 
     */
    @POST
    @Produces("text/html")
    @Access(Role.ADMIN)
    @Path("/create/")
    public Response userCreate(@Context HttpServletRequest req, MultivaluedMap<String, String> formParams) throws BadRequestException, NotAuthenticatedException{
        BaseModel model = buildBaseModel(req);
        
        try {
            containsNeededFieldsForCreate(formParams);
            ModelUser user = createUser(formParams);

            //TODO Validation for user values.
            if (ValidationUtils.isEmpty(user.getUsername())){
                model.setError("Username is required.");
                return Response.ok(templates.process(USER_CREATE_TEMPLATE, model)).build();
            }
            
            if (!ValidationUtils.isValidUsername(user.getUsername())){
                model.setError("Username was invalid.");
                return Response.ok(templates.process(USER_CREATE_TEMPLATE, model)).build();
            }
            
            long id = userService.add(new User(user.getUsername(),user.getRoleEnum())).getId();
            log("User created.", LogSeverity.TRACE);
            
            sessions.setInfo(model.getCurrentSession().getAuthId(), "User created.");
            return Response.seeOther(LanServer.getURI(String.format("users/%s", id))).build();
        } catch (RenderException e) {
            return handleRenderingError(model);
        } catch (ServiceException e){
            return handleServiceException(model);
        }

    }
    
    /**
     * Creates model with all users.
     * @return ModelUsers
     * @throws ServiceException thrown if there is problem with service.
     */
    private ModelUsers createModelUsers() throws ServiceException{
        ModelUsers modelUsers = new ModelUsers();
        
        Collection<User> users = userService.get();
        for (User user : users) {
            modelUsers.add(new ModelUser(user.getId(), user.getUserName(), user.getRole()));
        }
        return modelUsers;
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
    private ModelUser createEditUser(MultivaluedMap<String, String> formParams) throws BadRequestException{
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
        
        return new ModelUser(id, username, role);
    }
    
    /**
     * Creates user from formParams. If username or role is missing, exception is thrown.
     * @param formParams
     * @return
     * @throws BadRequestException
     */
    private ModelUser createUser(MultivaluedMap<String, String> formParams) throws BadRequestException{
        String username = formParams.getFirst(FORM_USERNAME).trim();
        
        Role role;
        try {
            role = Role.valueOf(formParams.getFirst(FORM_ROLE).toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("There was no such role.", e);
        }
        
        return new ModelUser(0, username,role);
    }
    
    /**
     * Creates edit post error response. Creates model user and fills fields from user.
     * @param base Basemodel, which is used to render.
     * @param user User
     * @param error Error message that is shown
     * @return Response
     * @throws RenderException Thrown is there is problem with rendering.
     */
    private Response editPostErrorResponse(BaseModel base, ModelUser user, String error) throws RenderException{
        ModelUser modelUser = new ModelUser(user.getId(), user.getUsername(), user.getRoleEnum());
        base.setModel(modelUser);
        base.setError(error);
        return Response.ok(templates.process(USER_EDIT_TEMPLATE, base)).build();
    }
}
