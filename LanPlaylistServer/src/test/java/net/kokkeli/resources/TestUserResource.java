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
    public void testGetDetailsRedirectsWhenUserIsNotFound() throws RenderException, ServiceException{
        userResource.userDetails(buildRequest(), NONEXISTING_ID);
        verify(getSessionService(), times(1)).setError(null, "User not found.");
    }
    
    @Test
    public void testGetDetailsServiceExceptionIsThrownWhenTemplateCantBeProcessed() throws RenderException{
        when(getTemplateService().process(any(String.class), any(BaseModel.class))).thenThrow(new RenderException("Rendering failed"));
        try {
            userResource.userDetails(buildRequest(), EXISTING_USER_ID);
            Assert.fail("Service exception should have been thrown.");
        } catch (ServiceException e) {
            // This should happen.
        }
    }
    
    @Test
    public void testGetDetailsPutsTemplateAndOkInResponse() throws NotFoundException, ServiceException, RenderException {
        final String processedTemplate = "Jeeah";
        
        when(getTemplateService().process(any(String.class), any(BaseModel.class))).thenReturn(processedTemplate);
        
        Response r = userResource.userDetails(buildRequest(), EXISTING_USER_ID);
        Assert.assertEquals(processedTemplate, r.getEntity().toString());
        Assert.assertEquals(RESPONSE_OK, r.getStatus());
        verify(getTemplateService()).process(anyString(), isA(BaseModel.class));
    }
    
    //EDIT GET
    @Test
    public void testGetEditThrowsNotFoundException() throws ServiceException{
        Response r = userResource.userEdit(buildRequest(), NONEXISTING_ID);
        Assert.assertEquals(REDIRECT, r.getStatus());
        assertSessionError("User not found.");
    }
    
    @Test
    public void testGetEditThrowsServiceExceptionWhenTemplateServiceFails() throws RenderException{
        when(getTemplateService().process(any(String.class), any(BaseModel.class))).thenThrow(new RenderException("Rendering failed"));
        try {
            userResource.userEdit(buildRequest(), EXISTING_USER_ID);
            Assert.fail("Service exception should have been thrown.");
        } catch (ServiceException e) {
            // This should happen.
        }
    }
    
    @Test
    public void testGetEditPutsTemplateAndOkInResponso() throws RenderException, NotFoundException, ServiceException{
        final String processedTemplate = "Jeeah";
        when(getTemplateService().process(any(String.class), any(BaseModel.class))).thenReturn(processedTemplate);
        
        Response r = userResource.userEdit(buildRequest(), EXISTING_USER_ID);
        Assert.assertEquals(processedTemplate, r.getEntity().toString());
        Assert.assertEquals(RESPONSE_OK, r.getStatus());
        verify(getTemplateService()).process(anyString(), isA(BaseModel.class));
    }
    
    //EDIT POST
    @Test
    public void testPostEditUpdatesUser() throws ServiceException, BadRequestException, RenderException{
        final String newUsername = "editedUser";
        final Role newRole = Role.ADMIN;
        
        userResource.userEdit(buildRequest(), editUserPost(EXISTING_USER_ID, newUsername, newRole));
        assertSessionInfo("User edited.");
    }
    
    @Test
    public void testPostEditWithInvalidUsernameThrowsBadRequest() throws ServiceException, RenderException{
        final String newUsername = "editedUser<";
        final Role newRole = Role.ADMIN;
        
        try {
            userResource.userEdit(buildRequest(), editUserPost(EXISTING_USER_ID, newUsername, newRole));
            Assert.fail("There should have been error.");
        } catch (BadRequestException e) {
            Assert.assertEquals("Username was invalid.", e.getMessage());
        }
    }
    
    @Test
    public void testPostEditWithExistingUsernameReturnsError() throws ServiceException, RenderException, BadRequestException{
        final String existing = "existing";
        final Role newRole = Role.ADMIN;
        
        ModelAnswer answer = new ModelAnswer();
        when(getTemplateService().process(any(String.class), any(BaseModel.class))).thenAnswer(answer);
        when(mockUserService.exists(any(String.class))).thenReturn(true);
        
        userResource.userEdit(buildRequest(), editUserPost(EXISTING_USER_ID, existing, newRole));
        Assert.assertEquals("Username already exists.", answer.getModel().getError());
    }
    
    //CREATE POST
    @Test
    public void testCreatePostWithWrongUsernameReturnsError() throws RenderException, BadRequestException, ServiceException{
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
    public void testCreatePostWithCorrectUsernameSucceeds() throws RenderException, BadRequestException, ServiceException{
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
