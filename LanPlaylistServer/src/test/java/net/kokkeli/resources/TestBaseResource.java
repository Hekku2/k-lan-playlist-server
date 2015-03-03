package net.kokkeli.resources;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;

import net.kokkeli.data.db.NotFoundInDatabaseException;
import net.kokkeli.data.dto.LogSeverity;
import net.kokkeli.data.services.ServiceException;
import net.kokkeli.player.PlayerStatus;
import net.kokkeli.resources.models.BaseModel;
import net.kokkeli.server.RenderException;

import org.junit.Assert;
import org.junit.Test;

import com.sun.jersey.api.NotFoundException;

import static org.mockito.Mockito.*;

public class TestBaseResource extends ResourceTestsBase<BaseResource>{
    
    @Override
    public void before() throws NotFoundException, ServiceException, NotFoundInDatabaseException {
        resource = new BaseResource(getLogger(), getTemplateService(), getPlayer(), getSessionService(), getSettings()){};
    }
    
    @Test
    public void testBaseModelDoesntThrowUnauthenticatedExceptionWhenAuthenticationCookieIsNotFound() {
        HttpServletRequest req = mock(HttpServletRequest.class);
        resource.buildBaseModel(req);
    }
    
    @Test
    public void testBaseModelDoesntThrowsUnauthenticatedExceptionWhenSessionIsNotFound() throws NotFoundInDatabaseException{
        when(getSessionService().get(any(String.class))).thenThrow(new NotFoundInDatabaseException("Not Found."));
        
        resource.buildBaseModel(buildRequest());
    }
    
    @Test
    public void testLogCallsLoggerService(){
        String testMessage = "Log Message";
        resource.log(testMessage, LogSeverity.DEBUG);
        verify(getLogger()).log(testMessage, LogSeverity.DEBUG);
        
        resource.log(testMessage, LogSeverity.ERROR);
        verify(getLogger()).log(testMessage, LogSeverity.ERROR);
        
        resource.log(testMessage, LogSeverity.TRACE);
        verify(getLogger()).log(testMessage, LogSeverity.TRACE);
    }
    
    @Test
    public void testBaseModelWithoutArguments() throws ServiceException{
        String title = "Title";
        PlayerStatus status = new PlayerStatus();
        status.setTitle(title);
        when(getPlayer().status()).thenReturn(status);
        
        BaseModel model = resource.buildBaseModel();
        Assert.assertEquals(title, model.getNowPlaying());
        Assert.assertEquals("", model.getUsername());
        Assert.assertNull(model.getError());
        Assert.assertNull(model.getInfo());
        Assert.assertNull(model.getCurrentSession());
        Assert.assertNull(model.getModel());
    }
    
    @Test
    public void testHandlingRenderErrorRedirectsToIndex(){
        String errorMessage = "There was a problem with rendering: Exception";
        BaseModel model = resource.buildBaseModel(buildRequest());
        RenderException ex = new RenderException("Exception");
        
        Response response = resource.handleRenderingError(model, ex);
        verify(getLogger()).log(errorMessage, LogSeverity.ERROR);
        
        assertSessionError("There was a problem with rendering the template.");
        Assert.assertEquals(REDIRECT, response.getStatus());
    }
    
    @Test
    public void testHandlingServiceExceptionRedirectsToIndex(){
        String errorMessage = "There was a problem with the service: Exception";
        BaseModel model = resource.buildBaseModel(buildRequest());
        ServiceException ex = new ServiceException("Exception");
        
        Response response = resource.handleServiceException(model, ex);
        verify(getLogger()).log(errorMessage, LogSeverity.ERROR);
        
        assertSessionError("Something went wrong with service.");
        Assert.assertEquals(REDIRECT, response.getStatus());
    }
}
