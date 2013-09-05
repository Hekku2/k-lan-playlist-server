package net.kokkeli.resources;

import javax.ws.rs.core.Response;

import org.junit.Assert;
import org.junit.Test;

import net.kokkeli.data.Role;
import net.kokkeli.data.Track;
import net.kokkeli.data.User;
import net.kokkeli.data.db.NotFoundInDatabase;
import net.kokkeli.data.services.ITrackService;
import net.kokkeli.data.services.ServiceException;
import net.kokkeli.server.IFileSystem;
import net.kokkeli.server.NotAuthenticatedException;
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
    public void trackDetailsReturnCorrectViewWhenTrackExists() throws NotFoundInDatabase, ServiceException, NotAuthenticatedException{
        Track existingTrack = new Track();
        existingTrack.setId(23);
        existingTrack.setUploader(new User("anyUser", Role.USER));
        
        when(trackService.get(existingTrack.getId())).thenReturn(existingTrack);
        
        Response r = resource.trackDetails(buildRequest(), existingTrack.getId());
        Assert.assertEquals(RESPONSE_OK, r.getStatus());
    }
    
    @Test
    public void trackDetailsReturnsRedirectWhenUserDoesntExist() throws NotFoundInDatabase, ServiceException, NotAuthenticatedException {
        long notFoundId = 666;
        
        when(trackService.get(notFoundId)).thenThrow(new NotFoundInDatabase("Eijooole"));
        Response r = resource.trackDetails(buildRequest(), notFoundId);
        Assert.assertEquals(REDIRECT, r.getStatus());
    }
}
