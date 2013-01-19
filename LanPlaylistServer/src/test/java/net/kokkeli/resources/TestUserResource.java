package net.kokkeli.resources;

import javax.ws.rs.core.Response;

import junit.framework.Assert;
import net.kokkeli.data.ILogger;
import net.kokkeli.data.Role;
import net.kokkeli.data.User;
import net.kokkeli.data.services.IUserService;
import net.kokkeli.data.services.ServiceException;
import net.kokkeli.player.IPlayer;
import net.kokkeli.resources.models.BaseModel;
import net.kokkeli.server.ITemplateService;
import net.kokkeli.server.RenderException;

import org.junit.Before;
import org.junit.Test;

import com.sun.jersey.api.NotFoundException;

import static org.mockito.Mockito.*;

public class TestUserResource {
    private static long EXISTING_USER_ID = 54;
    private static long NONEXISTING_ID = -3;
    private static int RESPONSE_OK = 200;
    
    private ILogger mockLogger;
    private IUserService mockUserService;
    private ITemplateService mockTemplateService;
    private IPlayer mockPlayer;
    
    private User existing;
    
    private UsersResource userResource;
    
    @Before
    public void setup() throws NotFoundException, ServiceException {
        mockLogger = mock(ILogger.class);
        mockUserService = mock(IUserService.class);
        mockTemplateService = mock(ITemplateService.class);
        mockPlayer = mock(IPlayer.class);
        
        existing = new User(EXISTING_USER_ID, "user", Role.NONE);
        
        when(mockUserService.get(EXISTING_USER_ID)).thenReturn(existing);
        when(mockUserService.get(NONEXISTING_ID)).thenThrow(new NotFoundException("User not found"));
        
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
}
