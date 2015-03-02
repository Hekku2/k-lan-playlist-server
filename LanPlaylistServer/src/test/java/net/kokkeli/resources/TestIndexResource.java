package net.kokkeli.resources;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.ws.rs.core.Response;

import org.junit.Assert;

import net.kokkeli.data.PlayList;
import net.kokkeli.data.Role;
import net.kokkeli.data.Track;
import net.kokkeli.data.User;
import net.kokkeli.data.db.NotFoundInDatabaseException;
import net.kokkeli.data.services.IPlaylistService;
import net.kokkeli.data.services.ServiceException;
import net.kokkeli.player.NotPlaylistPlayingException;
import net.kokkeli.resources.models.BaseModel;
import net.kokkeli.resources.models.ModelPlaylist;
import net.kokkeli.server.NotAuthenticatedException;
import net.kokkeli.server.RenderException;

import org.junit.Test;

import com.sun.jersey.api.NotFoundException;

public class TestIndexResource extends ResourceTestsBase<IndexResource>{
    private IPlaylistService mockPlaylistService;
    
    @Override
    public void before() throws NotFoundException, ServiceException, NotFoundInDatabaseException {
        mockPlaylistService = mock(IPlaylistService.class);
        resource = new IndexResource(getLogger(), getTemplateService(), getPlayer(), mockPlaylistService, getSessionService(), getSettings());
    }
    
    @Test
    public void testIndexReturnsCorrectModel() throws NotAuthenticatedException, ServiceException, RenderException, NotFoundInDatabaseException, NotPlaylistPlayingException {
        long currentlyPlayingId = 434;
        
        PlayList playlist = new PlayList(currentlyPlayingId);
        
        for (int i = 0; i < 6; i++) {
            Track t = new Track(i);
            t.setArtist("Artist " + i);
            t.setExists(i % 2 == 0);
            t.setLocation("Location " + i);
            t.setTrackName("Track name " + i);
            t.setUploader(new User("User " + i, Role.USER));
            playlist.getItems().add(t);
        }
        
        ModelAnswer model = new ModelAnswer();
        when(getTemplateService().process(any(String.class), any(BaseModel.class))).thenAnswer(model);
        when(getPlayer().playlistPlaying()).thenReturn(true);
        when(getPlayer().getCurrentPlaylistId()).thenReturn(currentlyPlayingId);
        when(mockPlaylistService.getPlaylist(currentlyPlayingId)).thenReturn(playlist);
        
        assertModelResponse(resource.index(buildRequest()), model, null, null);
        ModelPlaylist modelList = (ModelPlaylist)model.getModel().getModel();
        
        Assert.assertEquals(6, modelList.getItems().size());
    }
    
    @Test
    public void testIndexDoesntThrowExceptionWhenPlaylistIsNotSelected() throws RenderException, ServiceException, NotAuthenticatedException {
        ModelAnswer model = new ModelAnswer();
        when(getTemplateService().process(any(String.class), any(BaseModel.class))).thenAnswer(model);
        
        when(getPlayer().playlistPlaying()).thenReturn(false);
        assertModelResponse(resource.index(buildRequest()), model, null, null);
    }
    
    @Test
    public void testIndexIsStillRenderedWhenThereIsNoPlaylistPlaying() throws ServiceException, RenderException, NotAuthenticatedException, NotPlaylistPlayingException{
        final String processedTemplate = "Jeeah";
        when(getTemplateService().process(any(String.class), any(BaseModel.class))).thenReturn(processedTemplate);
        when(getPlayer().getCurrentPlaylistId()).thenThrow(new NotPlaylistPlayingException("No playlist playing."));
        when(getPlayer().playlistPlaying()).thenReturn(true);
        
        Response r = resource.index(buildRequest());
        Assert.assertEquals(processedTemplate, r.getEntity().toString());
        Assert.assertEquals(RESPONSE_OK, r.getStatus());
        verify(getTemplateService()).process(anyString(), isA(BaseModel.class));
    }
    
    @Test
    public void testIndexShowsErrorIfPlayingPlaylistIsNotFound() throws RenderException, ServiceException, NotFoundInDatabaseException, NotAuthenticatedException, NotPlaylistPlayingException{
        long currentlyPlayingNotFoundPlaylistId = 434;
        ModelAnswer model = new ModelAnswer();
        when(getTemplateService().process(any(String.class), any(BaseModel.class))).thenAnswer(model);
        when(getPlayer().playlistPlaying()).thenReturn(true);
        when(getPlayer().getCurrentPlaylistId()).thenReturn(currentlyPlayingNotFoundPlaylistId);
        when(mockPlaylistService.getPlaylist(currentlyPlayingNotFoundPlaylistId)).thenThrow(new NotFoundInDatabaseException("Lol not found"));
        
        assertModelResponse(resource.index(buildRequest()), model, "For some reason, currently playing playlist was not found.", null);
    }
    
    @Test
    public void testIndexThrowsServiceExceptionWhenPlaylistServiceThrowsServiceExeption() throws RenderException, ServiceException, NotFoundInDatabaseException, NotAuthenticatedException {
        ModelAnswer model = new ModelAnswer();
        when(getTemplateService().process(any(String.class), any(BaseModel.class))).thenAnswer(model);
        
        when(getPlayer().playlistPlaying()).thenReturn(true);
        when(mockPlaylistService.getPlaylist(any(Long.class))).thenThrow(new ServiceException("Any message."));
        
        assertModelResponse(resource.index(buildRequest()), model, "For some reason, currently playing playlist can't be shown.", null);
    }
    
    @Test
    public void testIndexThrowsServiceExceptionThrowsServiceExceptionIfRenderingFails() throws RenderException, NotAuthenticatedException{
        when(getTemplateService().process(any(String.class), any(BaseModel.class))).thenThrow(new RenderException("Error perror"));
        
        try {
            resource.index(buildRequest());
            Assert.fail("Index should throw error.");
        } catch (ServiceException e) {
            // This should happen
        }
    }
}
