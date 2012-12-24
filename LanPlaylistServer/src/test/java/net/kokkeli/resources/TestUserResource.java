package net.kokkeli.resources;

import junit.framework.Assert;
import net.kokkeli.data.ILogger;
import net.kokkeli.data.Role;
import net.kokkeli.data.User;
import net.kokkeli.data.services.IUserService;
import net.kokkeli.data.services.ServiceException;
import net.kokkeli.resources.models.ViewModel;
import net.kokkeli.server.ITemplateService;
import net.kokkeli.server.RenderException;

import org.junit.Before;
import org.junit.Test;

import com.sun.jersey.api.NotFoundException;

import static org.mockito.Mockito.*;

public class TestUserResource {
    private static long EXISTING_USER_ID;
    
    private ILogger mockLogger;
    private IUserService mockUserService;
    private ITemplateService mockTemplateService;
    private User existing;
    
    private UsersResource userResource;
    
    @Before
    public void setup() throws NotFoundException, ServiceException, RenderException{
        mockLogger = mock(ILogger.class);
        mockUserService = mock(IUserService.class);
        mockTemplateService = mock(ITemplateService.class);
        
        existing = new User(EXISTING_USER_ID, "", Role.NONE);
        
        when(mockUserService.get(EXISTING_USER_ID)).thenReturn(existing);
        when(mockTemplateService.process(any(String.class), any(ViewModel.class))).thenReturn("");
        
        userResource = new UsersResource(mockLogger, mockTemplateService, mockUserService);
        
    }
    
    @Test
    public void testGetDetailsThrowsNotFoundException() throws RenderException, ServiceException{
        final long unexistingId = -3;

        when(mockUserService.get(unexistingId)).thenThrow(new NotFoundException("User not found"));
        
        try {
            userResource.userDetails(null, unexistingId);
            Assert.fail("Getting nonexisting user shoudl throw exception.");
        } catch (NotFoundException e) {
            // This should happen.
        }
    }
    
    @Test
    public void testServiceExceptionIsThrownWhenTemplateCantBeProcessed() throws RenderException{
        when(mockTemplateService.process(any(String.class), any(ViewModel.class))).thenThrow(new RenderException("Rendergin failed"));
        
        try {
            userResource.userDetails(null, EXISTING_USER_ID);
            Assert.fail("Service exception should have been thrown.");
        } catch (ServiceException e) {
            // This should happen.
        }
    }
}
