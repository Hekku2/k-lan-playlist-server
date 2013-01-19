package net.kokkeli.resources;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.ws.rs.core.Response;

import junit.framework.Assert;
import net.kokkeli.data.ILogger;
import net.kokkeli.data.db.NotFoundInDatabase;
import net.kokkeli.data.services.IPlaylistService;
import net.kokkeli.data.services.ServiceException;
import net.kokkeli.player.IPlayer;
import net.kokkeli.player.NotPlaylistPlayingException;
import net.kokkeli.resources.models.BaseModel;
import net.kokkeli.server.ITemplateService;
import net.kokkeli.server.RenderException;

import org.junit.Before;
import org.junit.Test;

import com.sun.jersey.api.NotFoundException;

public class TestIndexResource {
    private static int RESPONSE_OK = 200;
    
    private ILogger mockLogger;
    private ITemplateService mockTemplateService;
    private IPlayer mockPlayer;
    private IPlaylistService mockPlaylistService;
    
    private IndexResource resource;
    
    
    @Before
    public void setup() throws NotFoundException, ServiceException {
        mockLogger = mock(ILogger.class);
        mockTemplateService = mock(ITemplateService.class);
        mockPlayer = mock(IPlayer.class);
        mockPlaylistService = mock(IPlaylistService.class);
        
        resource = new IndexResource(mockLogger, mockTemplateService, mockPlayer, mockPlaylistService);
    }
    
    @Test
    public void testIndexthrowsExceptionWhenPlaylistIsNotFound() throws NotFoundInDatabase, ServiceException{
        when(mockPlaylistService.getPlaylist(any(Long.class))).thenThrow(new NotFoundInDatabase("Not found."));
        
        try {
            resource.index(null);
            Assert.fail("Rendering succeeded when there should have been exception.");
        } catch (ServiceException e) {
            Assert.assertEquals("Playing playlist Id did not exist in database.", e.getMessage());
        }
    }
    
    @Test
    public void testIndexIsStillRenderedWhenThereIsNoPlaylistPlaying() throws NotPlaylistPlayingException, ServiceException, RenderException{
        final String processedTemplate = "Jeeah";
        when(mockTemplateService.process(any(String.class), any(BaseModel.class))).thenReturn(processedTemplate);
        when(mockPlayer.getCurrentPlaylistId()).thenThrow(new NotPlaylistPlayingException("No playlist playing."));

        Response r = resource.index(null);
        Assert.assertEquals(processedTemplate, r.getEntity().toString());
        Assert.assertEquals(RESPONSE_OK, r.getStatus());
        verify(mockTemplateService).process(anyString(), isA(BaseModel.class));
    }
    
    @Test
    public void  testIndexThrowsServiceExceptionWhenPlaylistServiceThrowsServiceExeption() throws ServiceException, NotFoundInDatabase{
        final String message = "Explosion";
        
        when(mockPlaylistService.getPlaylist(any(Long.class))).thenThrow(new ServiceException(message));
        
        try {
            resource.index(null);
            Assert.fail("Exception should have been thrown.");
        } catch (ServiceException e) {
            Assert.assertEquals(message, e.getMessage());
        }
    }
}
