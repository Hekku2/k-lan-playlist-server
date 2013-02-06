package net.kokkeli.resources;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.ws.rs.core.Response;

import junit.framework.Assert;
import net.kokkeli.data.db.NotFoundInDatabase;
import net.kokkeli.data.services.IPlaylistService;
import net.kokkeli.data.services.ServiceException;
import net.kokkeli.player.NotPlaylistPlayingException;
import net.kokkeli.resources.models.BaseModel;
import net.kokkeli.server.NotAuthenticatedException;
import net.kokkeli.server.RenderException;

import org.junit.Test;

import com.sun.jersey.api.NotFoundException;

public class TestIndexResource extends ResourceTestsBase{
    private static int RESPONSE_OK = 200;
    
    private IPlaylistService mockPlaylistService;
    
    
    private IndexResource resource;
    
    public void before() throws NotFoundException, ServiceException, NotFoundInDatabase {
        mockPlaylistService = mock(IPlaylistService.class);
        resource = new IndexResource(getLogger(), getTemplateService(), getPlayer(), mockPlaylistService, getSessionService());
    }
    
    @Test
    public void testIndexthrowsExceptionWhenPlaylistIsNotFound() throws NotFoundInDatabase, ServiceException, NotAuthenticatedException{
        when(mockPlaylistService.getPlaylist(any(Long.class))).thenThrow(new NotFoundInDatabase("Not found."));
        
        try {
            resource.index(buildRequest());
            Assert.fail("Rendering succeeded when there should have been exception.");
        } catch (ServiceException e) {
            Assert.assertEquals("Playing playlist Id did not exist in database.", e.getMessage());
        }
    }
    
    @Test
    public void testIndexIsStillRenderedWhenThereIsNoPlaylistPlaying() throws NotPlaylistPlayingException, ServiceException, RenderException, NotAuthenticatedException{
        final String processedTemplate = "Jeeah";
        when(getTemplateService().process(any(String.class), any(BaseModel.class))).thenReturn(processedTemplate);
        when(getPlayer().getCurrentPlaylistId()).thenThrow(new NotPlaylistPlayingException("No playlist playing."));

        Response r = resource.index(buildRequest());
        Assert.assertEquals(processedTemplate, r.getEntity().toString());
        Assert.assertEquals(RESPONSE_OK, r.getStatus());
        verify(getTemplateService()).process(anyString(), isA(BaseModel.class));
    }
    
    @Test
    public void  testIndexThrowsServiceExceptionWhenPlaylistServiceThrowsServiceExeption() throws ServiceException, NotFoundInDatabase, NotAuthenticatedException{
        final String message = "Explosion";
        
        when(mockPlaylistService.getPlaylist(any(Long.class))).thenThrow(new ServiceException(message));
        
        try {
            resource.index(buildRequest());
            Assert.fail("Exception should have been thrown.");
        } catch (ServiceException e) {
            Assert.assertEquals(message, e.getMessage());
        }
    }
}
