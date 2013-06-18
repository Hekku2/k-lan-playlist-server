package net.kokkeli.resources;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.servlet.http.HttpServletRequest;

import net.kokkeli.data.db.NotFoundInDatabase;
import net.kokkeli.data.services.ServiceException;
import net.kokkeli.server.NotAuthenticatedException;

import org.junit.Assert;
import org.junit.Test;
import com.sun.jersey.api.NotFoundException;

public class TestBaseResource extends ResourceTestsBase{
    private BaseResource resource;
    
    public void before() throws NotFoundException, ServiceException, NotFoundInDatabase {
        resource = new BaseResource(getLogger(), getTemplateService(), getPlayer(), getSessionService(), getSettings()){
            
        };
    }
    
    @Test
    public void testBaseModelThrowsUnauthenticatedExceptionWhenAuthenticationCookieIsNotFound() {
        HttpServletRequest req = mock(HttpServletRequest.class);
        
        try {
            resource.buildBaseModel(req);
            Assert.fail("Building base model without authentication cookies should have thrown UnauthenticatedException");
        } catch (NotAuthenticatedException e) {
            Assert.assertEquals("There was a problem with authentication.", e.getMessage());
        }
    }
    
    @Test
    public void testBaseModelThrowsUnauthenticatedExceptionWhenSessionIsNotFound() throws NotFoundInDatabase{
        when(getSessionService().get(any(String.class))).thenThrow(new NotFoundInDatabase("Not Found."));
        
        try {
            resource.buildBaseModel(buildRequest());
            Assert.fail("Building base model without authentication cookies should have thrown UnauthenticatedException");
        } catch (NotAuthenticatedException e) {
            Assert.assertEquals("There was a problem with authentication.", e.getMessage());
        }
    }
}
