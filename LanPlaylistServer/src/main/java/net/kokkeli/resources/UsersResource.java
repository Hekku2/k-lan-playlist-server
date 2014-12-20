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

import net.kokkeli.ISettings;
import net.kokkeli.ModelBuilder;
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
    private ModelBuilder<ModelUser> modelBuilder;
    
    /**
     * Creates users resource.
     * @param logger
     */
    @Inject
    protected UsersResource(ILogger logger, ITemplateService templateService, IUserService userservice, IPlayer player, ISessionService sessions, ISettings settings) {
        super(logger, templateService, player, sessions, settings);
        this.userService = userservice;
        
        modelBuilder = new ModelBuilder<ModelUser>(ModelUser.class);
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
            return handleRenderingError(model, e);
        } catch (ServiceException e) {
            return handleServiceException(model, e);
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
            return handleRenderingError(model, e);
        } catch (NotFoundInDatabase e){
            sessions.setError(model.getCurrentSession().getAuthId(), "User not found.");
            return Response.seeOther(settings.getURI("users")).build();
        } catch (ServiceException e) {
            return handleServiceException(model, e);
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
            return handleRenderingError(model, e);
        } catch (NotFoundInDatabase e) {
            sessions.setError(model.getCurrentSession().getAuthId(), "User not found.");
            return Response.seeOther(settings.getURI("users")).build();
        } catch (ServiceException e) {
            return handleServiceException(model, e);
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
        try {
            ModelUser editedUser = modelBuilder.createModelFrom(formParams);
            String validationError = getValidationErrorForUserEditing(editedUser);
            if (validationError != null){
                return editPostErrorResponse(model, editedUser, validationError);
            }
            
            //Change to new password.
            if (!ValidationUtils.isEmpty(editedUser.getNewPassword())){
                userService.changePassword(editedUser.getId(), editedUser.getNewPassword());
            }
            
            userService.update(new User(editedUser.getId(), editedUser.getUsername(), editedUser.getRoleEnum()));
            sessions.setInfo(model.getCurrentSession().getAuthId(), "User edited.");
            return Response.seeOther(settings.getURI(String.format("users/%s", editedUser.getId()))).build();
        } catch (NotFoundInDatabase e) {
            sessions.setError(model.getCurrentSession().getAuthId(), "User not found.");
            return Response.seeOther(settings.getURI("users")).build();
        } catch (ServiceException e) {
            return handleServiceException(model, e);
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
            return handleRenderingError(model, e);
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
        BaseModel baseModel = buildBaseModel(req);
        
        try {
            containsNeededFieldsForCreate(formParams);
            ModelUser model = modelBuilder.createModelFrom(formParams);

            String validationError = getValidationErrorForUserCreation(model);
            if (validationError != null){
                baseModel.setError(validationError);
                return Response.ok(templates.process(USER_CREATE_TEMPLATE, baseModel)).build();
            }
            
            User user = new User(model.getUsername(), model.getRoleEnum());
            user.setPasswordHash(userService.calculateHash(model.getNewPassword()));
            
            long id = userService.add(user).getId();
            log("User created.", LogSeverity.INFO);
            
            sessions.setInfo(baseModel.getCurrentSession().getAuthId(), "User created.");
            return Response.seeOther(settings.getURI(String.format("users/%s", id))).build();
        } catch (RenderException e) {
            return handleRenderingError(baseModel, e);
        } catch (ServiceException e){
            return handleServiceException(baseModel, e);
        }

    }
    
    /**
     * Checks that model is valid, and if model is not valid, validation error is returned.
     * If model is valid, null is returned
     * @param model Model
     * @return Null if model is valid, otherwise validation error string
     * @throws ServiceException Thrown if there is a problem with the service
     */
    private String getValidationErrorForUserCreation(ModelUser model) throws ServiceException{
        //TODO Validation for user values.
        String usernameValidationError = getUsernameValidationError(model.getUsername());
        if (usernameValidationError != null){
            return usernameValidationError;
        }
        
        if (ValidationUtils.isEmpty(model.getNewPassword())){
            return "Password is required for normal user.";
        }
        
        if (!model.getNewPassword().equals(model.getConfirmPassword())){
            return "Confirm password did not match new password.";
        }
        
        if (userService.exists(model.getUsername())){
            return "Username already in use!";
        }
        
        if (model.getRoleEnum() == null || model.getRoleEnum() == Role.NONE){
            return "User must have role.";
        }
        
        return null;
    }
    
    /**
     * Returns validation error if edited user contains invalid values.
     * @param editedUser Editer user
     * @return Validation error if there is one.
     * @throws ServiceException Thrown if there is a problem with the service
     * @throws NotFoundInDatabase Thrown if there is no user to be edited.
     */
    private String getValidationErrorForUserEditing(ModelUser editedUser) throws ServiceException, NotFoundInDatabase{
        User user = userService.get(editedUser.getId());
        
        String usernameValidationError = getUsernameValidationError(editedUser.getUsername());
        if (usernameValidationError != null){
            return usernameValidationError;
        }

        
        if ((!ValidationUtils.isEmpty(editedUser.getConfirmPassword()) || !ValidationUtils.isEmpty(editedUser.getNewPassword()) )
                && !editedUser.getConfirmPassword().equals(editedUser.getNewPassword())){
            return "Passwords did not match.";
        }
        
        if (!editedUser.getUsername().equals(user.getUserName()) && userService.exists(editedUser.getUsername())){
            return "Username already exists.";
        }
        
        if (editedUser.getRoleEnum() == null || editedUser.getRoleEnum() == Role.NONE){
            return "User must have role.";
        }
        
        return null;
    }
    
    /**
     * Check if username is valid and returns error, if there is one. Otherwise null is returned.
     * @param username Username
     * @return Null, if username is valid
     */
    private static String getUsernameValidationError(String username){
        if (ValidationUtils.isEmpty(username)){
            return "Username is required.";
        }
        
        if (!ValidationUtils.isValidUsername(username)){
            return "Username was invalid.";
        }
        
        return null;
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
    private static void containsNeededFieldsForEdit(MultivaluedMap<String, String> formParams) throws BadRequestException{
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
    private static void containsNeededFieldsForCreate(MultivaluedMap<String, String> formParams) throws BadRequestException{
        if (!formParams.containsKey(FORM_USERNAME) || !formParams.containsKey(FORM_ROLE)){
            throw new BadRequestException("User edit post did not contain needed fields.");
        }
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
