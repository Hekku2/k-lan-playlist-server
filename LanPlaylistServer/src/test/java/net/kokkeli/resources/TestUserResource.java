package net.kokkeli.resources;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import junit.framework.Assert;
import net.kokkeli.data.ILogger;
import net.kokkeli.data.Role;
import net.kokkeli.data.User;
import net.kokkeli.data.db.NotFoundInDatabase;
import net.kokkeli.data.services.IUserService;
import net.kokkeli.data.services.ServiceException;
import net.kokkeli.player.IPlayer;
import net.kokkeli.resources.models.BaseModel;
import net.kokkeli.server.ITemplateService;
import net.kokkeli.server.RenderException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.sun.jersey.api.NotFoundException;

import static org.mockito.Mockito.*;

public class TestUserResource {
    private static long EXISTING_USER_ID = 54;
    private static long NONEXISTING_ID = -3;
    private static int RESPONSE_OK = 200;
    
    private static final String FORM_USERNAME = "username";
    private static final String FORM_ROLE = "role";
    
    private ILogger mockLogger;
    private IUserService mockUserService;
    private ITemplateService mockTemplateService;
    private IPlayer mockPlayer;
    
    private User existing;
    
    private UsersResource userResource;
    
    @Before
    public void setup() throws NotFoundInDatabase, ServiceException {
        mockLogger = mock(ILogger.class);
        mockUserService = mock(IUserService.class);
        mockTemplateService = mock(ITemplateService.class);
        mockPlayer = mock(IPlayer.class);
        
        existing = new User(EXISTING_USER_ID, "user", Role.NONE);
        
        when(mockUserService.get(EXISTING_USER_ID)).thenReturn(existing);
        when(mockUserService.get(NONEXISTING_ID)).thenThrow(new NotFoundInDatabase("User not found"));
        
        userResource = new UsersResource(mockLogger, mockTemplateService, mockUserService, mockPlayer);
        
    }
    
    @Test
    public void testGetDetailsThrowsNotFoundException() throws RenderException, ServiceException{
        try {
            userResource.userDetails(null, NONEXISTING_ID);
            Assert.fail("Getting nonexisting user shoudl throw exception.");
        } catch (NotFoundException e) {
            // This should happen.
        }
    }
    
    @Test
    public void testGetDetailsServiceExceptionIsThrownWhenTemplateCantBeProcessed() throws RenderException{
        when(mockTemplateService.process(any(String.class), any(BaseModel.class))).thenThrow(new RenderException("Rendering failed"));
        try {
            userResource.userDetails(null, EXISTING_USER_ID);
            Assert.fail("Service exception should have been thrown.");
        } catch (ServiceException e) {
            // This should happen.
        }
    }
    
    @Test
    public void testGetDetailsPutsTemplateAndOkInResponse() throws NotFoundException, ServiceException, RenderException {
        final String processedTemplate = "Jeeah";
        
        when(mockTemplateService.process(any(String.class), any(BaseModel.class))).thenReturn(processedTemplate);
        
        Response r = userResource.userDetails(null, EXISTING_USER_ID);
        Assert.assertEquals(processedTemplate, r.getEntity().toString());
        Assert.assertEquals(RESPONSE_OK, r.getStatus());
        verify(mockTemplateService).process(anyString(), isA(BaseModel.class));
    }
    
    @Test
    public void testGetEditThrowsNotFoundException() throws ServiceException{
        try {
            userResource.userEdit(null, NONEXISTING_ID);
            Assert.fail("Not found exception should have been thrown.");
        } catch (NotFoundException e) {
        }
    }
    
    @Test
    public void testGetEditThrowsServiceExceptionWhenTemplateServiceFails() throws RenderException{
        when(mockTemplateService.process(any(String.class), any(BaseModel.class))).thenThrow(new RenderException("Rendering failed"));
        try {
            userResource.userEdit(null, EXISTING_USER_ID);
            Assert.fail("Service exception should have been thrown.");
        } catch (ServiceException e) {
            // This should happen.
        }
    }
    
    @Test
    public void testGetEditPutsTemplateAndOkInResponso() throws RenderException, NotFoundException, ServiceException{
        final String processedTemplate = "Jeeah";
        when(mockTemplateService.process(any(String.class), any(BaseModel.class))).thenReturn(processedTemplate);
        
        Response r = userResource.userEdit(null, EXISTING_USER_ID);
        Assert.assertEquals(processedTemplate, r.getEntity().toString());
        Assert.assertEquals(RESPONSE_OK, r.getStatus());
        verify(mockTemplateService).process(anyString(), isA(BaseModel.class));
    }
    
    @Test
    public void testCreatePostWithWrongUsernameReturnsError() throws RenderException, BadRequestException, ServiceException{
        ModelAnswer answer = new ModelAnswer();
        when(mockTemplateService.process(any(String.class), any(BaseModel.class))).thenAnswer(answer);
        
        Response r = userResource.userCreate(null, createUserPost("", Role.ADMIN));
        Assert.assertEquals("Username was invalid.", answer.getModel().getError());
        Assert.assertEquals(RESPONSE_OK, r.getStatus());
        
        r = userResource.userCreate(null, createUserPost("<", Role.ADMIN));
        Assert.assertEquals("Username was invalid.", answer.getModel().getError());
        Assert.assertEquals(RESPONSE_OK, r.getStatus());
    }
    
    @Test
    public void testCreatePostWithCorrectUsernameSucceeds() throws RenderException, BadRequestException, ServiceException{
        ModelAnswer answer = new ModelAnswer();
        when(mockTemplateService.process(any(String.class), any(BaseModel.class))).thenAnswer(answer);
        
        userResource.userCreate(null, createUserPost("fdas", Role.ADMIN));
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
