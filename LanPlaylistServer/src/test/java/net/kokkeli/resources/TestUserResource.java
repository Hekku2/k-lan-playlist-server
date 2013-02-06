package net.kokkeli.resources;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import junit.framework.Assert;
import net.kokkeli.data.Role;
import net.kokkeli.data.User;
import net.kokkeli.data.db.NotFoundInDatabase;
import net.kokkeli.data.services.IUserService;
import net.kokkeli.data.services.ServiceException;
import net.kokkeli.resources.models.BaseModel;
import net.kokkeli.server.NotAuthenticatedException;
import net.kokkeli.server.RenderException;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.sun.jersey.api.NotFoundException;

import static org.mockito.Mockito.*;

public class TestUserResource extends ResourceTestsBase{
    private static long EXISTING_USER_ID = 54;
    private static long NONEXISTING_ID = -3;    
    private static final String FORM_USERNAME = "username";
    private static final String FORM_ROLE = "role";
    private static final String FORM_ID = "id";
    
    private IUserService mockUserService;
    
    private User existing;
    
    private UsersResource userResource;

    public void before() throws NotFoundInDatabase, ServiceException {
        mockUserService = mock(IUserService.class);
        
        existing = new User(EXISTING_USER_ID, "user", Role.NONE);
        
        when(mockUserService.get(EXISTING_USER_ID)).thenReturn(existing);
        when(mockUserService.get(NONEXISTING_ID)).thenThrow(new NotFoundInDatabase("User not found"));
        
        userResource = new UsersResource(getLogger(), getTemplateService(), mockUserService, getPlayer(), getSessionService());
    }
    
    // USER DETAILS GET
    @Test
    public void testGetDetailsRedirectsWhenUserIsNotFound() throws RenderException, ServiceException, NotAuthenticatedException{
        assertRedirectAndError(userResource.userDetails(buildRequest(), NONEXISTING_ID), "User not found.");
    }
    
    @Test
    public void testGetDetailsServiceExceptionIsThrownWhenTemplateCantBeProcessed() throws RenderException, NotFoundException, NotAuthenticatedException{
        when(getTemplateService().process(any(String.class), any(BaseModel.class))).thenThrow(new RenderException("Rendering failed"));
        assertRedirectAndError(userResource.userDetails(buildRequest(), EXISTING_USER_ID), "There was problem with rendering the template.");
    }
    
    @Test
    public void testGetDetailsPutsTemplateAndOkInResponse() throws NotFoundException, ServiceException, RenderException, NotAuthenticatedException {
        final String processedTemplate = "Jeeah";
        
        when(getTemplateService().process(any(String.class), any(BaseModel.class))).thenReturn(processedTemplate);
        
        Response r = userResource.userDetails(buildRequest(), EXISTING_USER_ID);
        Assert.assertEquals(processedTemplate, r.getEntity().toString());
        Assert.assertEquals(RESPONSE_OK, r.getStatus());
        verify(getTemplateService()).process(anyString(), isA(BaseModel.class));
    }
    
    //EDIT GET
    @Test
    public void testGetEditThrowsNotFoundException() throws ServiceException, NotAuthenticatedException{
        assertRedirectAndError(userResource.userEdit(buildRequest(), NONEXISTING_ID), "User not found.");
    }
    
    @Test
    public void testGetEditThrowsServiceExceptionWhenTemplateServiceFails() throws RenderException, NotAuthenticatedException{
        when(getTemplateService().process(any(String.class), any(BaseModel.class))).thenThrow(new RenderException("Rendering failed"));
        assertRedirectAndError(userResource.userEdit(buildRequest(), EXISTING_USER_ID), "There was problem with rendering the template.");
    }
    
    @Test
    public void testGetEditPutsTemplateAndOkInResponso() throws RenderException, NotFoundException, ServiceException, NotAuthenticatedException{
        final String processedTemplate = "Jeeah";
        when(getTemplateService().process(any(String.class), any(BaseModel.class))).thenReturn(processedTemplate);
        
        Response r = userResource.userEdit(buildRequest(), EXISTING_USER_ID);
        Assert.assertEquals(processedTemplate, r.getEntity().toString());
        Assert.assertEquals(RESPONSE_OK, r.getStatus());
        verify(getTemplateService()).process(anyString(), isA(BaseModel.class));
    }
    
    //EDIT POST
    @Test
    public void testPostEditUpdatesUser() throws ServiceException, BadRequestException, RenderException, NotAuthenticatedException{
        final String newUsername = "editedUser";
        final Role newRole = Role.ADMIN;
        
        userResource.userEdit(buildRequest(), editUserPost(EXISTING_USER_ID, newUsername, newRole));
        assertSessionInfo("User edited.");
    }
    
    @Test
    public void testPostEditWithInvalidUsernameThrowsBadRequest() throws ServiceException, RenderException, BadRequestException, NotAuthenticatedException{
        final String newUsername = "editedUser<";
        final Role newRole = Role.ADMIN;
        ModelAnswer answer = new ModelAnswer();
        when(getTemplateService().process(any(String.class), any(BaseModel.class))).thenAnswer(answer);
        
        Response r = userResource.userEdit(buildRequest(), editUserPost(EXISTING_USER_ID, newUsername, newRole));
        Assert.assertEquals("Invalid username.", answer.getModel().getError());
        Assert.assertEquals(RESPONSE_OK, r.getStatus());
    }
    
    @Test
    public void testPostEditWithExistingUsernameReturnsError() throws ServiceException, RenderException, BadRequestException, NotAuthenticatedException{
        final String existing = "existing";
        final Role newRole = Role.ADMIN;
        
        ModelAnswer answer = new ModelAnswer();
        when(getTemplateService().process(any(String.class), any(BaseModel.class))).thenAnswer(answer);
        when(mockUserService.exists(any(String.class))).thenReturn(true);
        
        Response r = userResource.userEdit(buildRequest(), editUserPost(EXISTING_USER_ID, existing, newRole));
        Assert.assertEquals("Username already exists.", answer.getModel().getError());
        Assert.assertEquals(RESPONSE_OK, r.getStatus());
    }
    
    //CREATE POST
    @Test
    public void testCreatePostWithWrongUsernameReturnsError() throws RenderException, BadRequestException, ServiceException, NotAuthenticatedException{
        ModelAnswer answer = new ModelAnswer();
        when(getTemplateService().process(any(String.class), any(BaseModel.class))).thenAnswer(answer);
        
        Response r = userResource.userCreate(buildRequest(), createUserPost("", Role.ADMIN));
        Assert.assertEquals("Username was invalid.", answer.getModel().getError());
        Assert.assertEquals(RESPONSE_OK, r.getStatus());
        
        r = userResource.userCreate(buildRequest(), createUserPost("<", Role.ADMIN));
        Assert.assertEquals("Username was invalid.", answer.getModel().getError());
        Assert.assertEquals(RESPONSE_OK, r.getStatus());
    }
    
    @Test
    public void testCreatePostWithCorrectUsernameSucceeds() throws RenderException, BadRequestException, ServiceException, NotAuthenticatedException{
        ModelAnswer answer = new ModelAnswer();
        when(getTemplateService().process(any(String.class), any(BaseModel.class))).thenAnswer(answer);
        
        userResource.userCreate(buildRequest(), createUserPost("fdas", Role.ADMIN));
        Assert.assertNull(answer.getModel().getError());
        Assert.assertEquals("User created.", answer.getModel().getInfo());
    }
    
    /**
     * Mocks MultivaluedMap var user creation posts.
     * @param username
     * @param role
     * @return
     */
    private MultivaluedMap<String, String> createUserPost(String username, Role role){
        @SuppressWarnings("unchecked")
        MultivaluedMap<String, String> map = mock(MultivaluedMap.class);
        
        when(map.containsKey(FORM_USERNAME)).thenReturn(true);
        when(map.containsKey(FORM_ROLE)).thenReturn(true);
        
        when(map.getFirst(FORM_USERNAME)).thenReturn(username);
        when(map.getFirst(FORM_ROLE)).thenReturn(role.name().toUpperCase());
        return map;
    }
    
    /**
     * Mocks MultivaluedMap var user dit posts.
     * @param username
     * @param role
     * @return
     */
    private MultivaluedMap<String, String> editUserPost(long id, String username, Role role){
        MultivaluedMap<String, String> map = createUserPost(username, role);
        
        when(map.containsKey(FORM_ID)).thenReturn(true);
        when(map.getFirst(FORM_ID)).thenReturn(id + "");
        
        return map;
    }
    
    /**
     * Asserts that Response is redirect and error is correct.
     * @param response Response
     * @param error Error
     */
    private void assertRedirectAndError(Response response, String error){
        assertSessionError(error);
        Assert.assertEquals(REDIRECT, response.getStatus());
    }
    
    /**
     * Answer that holds Model
     * @author Hekku2
     *
     */
    private class ModelAnswer implements Answer<String>{
        private BaseModel model;
        
        @Override
        public String answer(InvocationOnMock invocation) throws Throwable {
            model = (BaseModel)invocation.getArguments()[1];
            return "";
        }
        
        public BaseModel getModel(){
            return model;
        }
        
    }
}
