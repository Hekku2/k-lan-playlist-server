package net.kokkeli.resources;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.ws.rs.core.Response;

import org.junit.Assert;
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
    private IPlaylistService mockPlaylistService;
    private IndexResource resource;
    
    public void before() throws NotFoundException, ServiceException, NotFoundInDatabase {
        mockPlaylistService = mock(IPlaylistService.class);
        resource = new IndexResource(getLogger(), getTemplateService(), getPlayer(), mockPlaylistService, getSessionService(), getSettings());
    }
    
    @Test
    public void testIndexthrowsExceptionWhenPlaylistIsNotFound() throws RenderException, ServiceException, NotFoundInDatabase, NotAuthenticatedException {
        ModelAnswer model = new ModelAnswer();
        when(getTemplateService().process(any(String.class), any(BaseModel.class))).thenAnswer(model);
        
        when(mockPlaylistService.getPlaylist(any(Long.class))).thenThrow(new NotFoundInDatabase("Not found."));
        assertModelResponse(resource.index(buildRequest()), model, "For some reason, currently playing playlist was not found.", null);
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
    public void testIndexThrowsServiceExceptionWhenPlaylistServiceThrowsServiceExeption() throws RenderException, ServiceException, NotFoundInDatabase, NotAuthenticatedException {
        ModelAnswer model = new ModelAnswer();
        when(getTemplateService().process(any(String.class), any(BaseModel.class))).thenAnswer(model);
        
        when(mockPlaylistService.getPlaylist(any(Long.class))).thenThrow(new ServiceException("Any message."));
        
        assertModelResponse(resource.index(buildRequest()), model, "For some reason, currently playing playlist can't be shown.", null);
    }
}
