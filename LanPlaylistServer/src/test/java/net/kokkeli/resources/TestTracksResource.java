package net.kokkeli.resources;

import java.util.ArrayList;

import javax.ws.rs.core.Response;

import org.junit.Assert;
import org.junit.Test;

import net.kokkeli.data.Role;
import net.kokkeli.data.Track;
import net.kokkeli.data.User;
import net.kokkeli.data.db.NotFoundInDatabase;
import net.kokkeli.data.services.ITrackService;
import net.kokkeli.data.services.ServiceException;
import net.kokkeli.resources.models.BaseModel;
import net.kokkeli.resources.models.ModelPlaylistItem;
import net.kokkeli.resources.models.ModelTracks;
import net.kokkeli.server.IFileSystem;
import net.kokkeli.server.NotAuthenticatedException;
import net.kokkeli.server.RenderException;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class TestTracksResource extends ResourceTestsBase{
    private TracksResource resource;
    private ITrackService trackService;
    private IFileSystem fileSystem;
    
    public void before() throws NotFoundInDatabase, ServiceException {
        trackService = mock(ITrackService.class);
        fileSystem = mock(IFileSystem.class);
        
        resource = new TracksResource(getLogger(), getTemplateService(), getPlayer(), getSessionService(), getSettings(), trackService, fileSystem);
    }
    
    @Test
    public void testTrackDetailsReturnCorrectViewWhenTrackExists() throws NotFoundInDatabase, ServiceException, NotAuthenticatedException{
        Track existingTrack = new Track();
        existingTrack.setId(23);
        existingTrack.setUploader(new User("anyUser", Role.USER));
        
        when(trackService.get(existingTrack.getId())).thenReturn(existingTrack);
        
        Response r = resource.trackDetails(buildRequest(), existingTrack.getId());
        Assert.assertEquals(RESPONSE_OK, r.getStatus());
    }
    
    @Test
    public void testTrackDetailsReturnsRedirectWhenTrackDoesntExist() throws NotFoundInDatabase, ServiceException, NotAuthenticatedException {
        long notFoundId = 666;
        
        when(trackService.get(notFoundId)).thenThrow(new NotFoundInDatabase("Eijooole"));
        Response r = resource.trackDetails(buildRequest(), notFoundId);
        Assert.assertEquals(REDIRECT, r.getStatus());
    }
    
    @Test
    public void testTrackDetailsHandlesRenderingException() throws NotAuthenticatedException, RenderException, NotFoundInDatabase, ServiceException{
        Track existingTrack = new Track();
        existingTrack.setId(23);
        existingTrack.setUploader(new User("anyUser", Role.USER));
        
        when(trackService.get(existingTrack.getId())).thenReturn(existingTrack);
        
        when(getTemplateService().process(any(String.class), any(BaseModel.class))).thenThrow(new RenderException("Calcutta."));
        
        Response r = resource.trackDetails(buildRequest(), existingTrack.getId());
        Assert.assertEquals(REDIRECT, r.getStatus());
    }
    
    @Test
    public void testTrackDetailsHandlesServiceException() throws NotFoundInDatabase, ServiceException, NotAuthenticatedException{
        long anyId = 312;
        
        when(trackService.get(anyLong())).thenThrow(new ServiceException("Error pärrör"));
        
        Response r = resource.trackDetails(buildRequest(), anyId);
        Assert.assertEquals(REDIRECT, r.getStatus());
    }
    
    @Test
    public void testVerifiedTracksWorks() throws ServiceException, RenderException, NotAuthenticatedException{
        ArrayList<Track> tracks = new ArrayList<Track>();
        for (int i = 0; i < 7; i++) {
            Track track = new Track();
            track.setId(i);
            track.setArtist("Artist " + i);
            track.setExists(i % 2 == 0);
            track.setLocation("Location " + i);
            track.setTrackName("Track name " + i);
            
            if (i % 2 == 0){
                track.setUploader(new User("user " + i, Role.USER));
            }
            tracks.add(track);
        }
        when(fileSystem.fileExists(anyString())).thenReturn(true);
        when(trackService.getAndVerifyTracks()).thenReturn(tracks);
        
        ModelAnswer answer = new ModelAnswer();
        when(getTemplateService().process(any(String.class), any(BaseModel.class))).thenAnswer(answer);
        
        assertModelResponse(resource.verifiedTrack(buildRequest()), answer, null, null);
        
        ModelTracks modelTracks = (ModelTracks)answer.getModel().getModel();
        Assert.assertEquals(7, modelTracks.getItems().size());
        
        ModelPlaylistItem modelTrack = modelTracks.getItems().get(0);
        Assert.assertTrue(modelTrack.getExists());
        Assert.assertEquals("Artist 0", modelTrack.getArtist());
    }
    
    @Test
    public void testVerifiedTracksHandlesServiceException() throws ServiceException, NotAuthenticatedException{
        when(trackService.getAndVerifyTracks()).thenThrow(new ServiceException("Error pärrör"));
        
        Response r = resource.verifiedTrack(buildRequest());
        Assert.assertEquals(REDIRECT, r.getStatus());
    }
    
    @Test
    public void testVerifiedTracksHandlesRenderingException() throws ServiceException, RenderException, NotAuthenticatedException {
        when(trackService.getAndVerifyTracks()).thenReturn(new ArrayList<Track>());
        
        when(getTemplateService().process(any(String.class), any(BaseModel.class))).thenThrow(new RenderException("Calcutta."));
        
        Response r = resource.verifiedTrack(buildRequest());
        Assert.assertEquals(REDIRECT, r.getStatus());
    }
}
