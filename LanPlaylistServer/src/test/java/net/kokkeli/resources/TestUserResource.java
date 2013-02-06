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
    private static int RESPONSE_OK = 200;
    
    private static final String FORM_USERNAME = "username";
    private static final String FORM_ROLE = "role";
    
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
    
    @Test
    public void testGetDetailsThrowsNotFoundException() throws RenderException, ServiceException{
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
    
    @Test
    public void testGetEditThrowsNotFoundException() throws ServiceException{
        try {
            userResource.userEdit(buildRequest(), NONEXISTING_ID);
            Assert.fail("Not found exception should have been thrown.");
        } catch (NotFoundException e) {
        }
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
