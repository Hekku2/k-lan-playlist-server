package net.kokkeli.resources;

import junit.framework.Assert;
import net.kokkeli.data.ILogger;
import net.kokkeli.data.services.IUserService;
import net.kokkeli.data.services.ServiceException;
import net.kokkeli.server.RenderException;

import org.junit.Test;

import com.sun.jersey.api.NotFoundException;

import static org.mockito.Mockito.*;

public class TestUserResource {
    private ILogger mockLogger;
    private IUserService mockUserService;
    
    @Test
    public void testGetDetailsThrowsNotFoundException() throws RenderException, ServiceException{
        final long unexistingId = -3;
        
        mockLogger = mock(ILogger.class);
        mockUserService = mock(IUserService.class);
        when(mockUserService.get(unexistingId)).thenThrow(new NotFoundException("User not found"));
        
        UsersResource res = new UsersResource(mockLogger, mockUserService);
        
        try {
            res.userDetails(null, unexistingId);
            Assert.fail("Getting nonexisting user shoudl throw exception.");
        } catch (NotFoundException e) {
            // This should happen.
        }
    }
}
