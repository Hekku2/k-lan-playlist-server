package net.kokkeli.resources;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.ws.rs.core.Response;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import net.kokkeli.data.db.NotFoundInDatabaseException;
import net.kokkeli.data.dto.Track;
import net.kokkeli.data.services.ITrackService;
import net.kokkeli.data.services.ServiceException;
import net.kokkeli.player.PlayerStatus;

public class TestPlayerResource extends ResourceTestsBase<PlayerResource> {
    private ITrackService mockTrackService;
    
    @Override
    public void before() throws Exception {
        mockTrackService = mock(ITrackService.class);
        resource = new PlayerResource(getLogger(), getTemplateService(), getPlayer(), getSessionService(), getSettings(), mockTrackService);
    }

    @Test
    public void testPostAddToQueueAddsSongToQueue() throws NotFoundInDatabaseException, ServiceException, BadRequestException{
        long existingId = 23;
        Track track = new Track(existingId);
        track.setLocation("Marsi666//");
        
        when(mockTrackService.get(existingId)).thenReturn(track);
        
        Response r = resource.addToQueue(buildRequest(), createIdPost(existingId));
        assertEquals(RESPONSE_OK, r.getStatus());
        verify(getPlayer()).addToQueue(track.getLocation());
    }
    
    @Test
    public void testPostAddToQueueReturnsNotFoundWhenTrackIsNotFound() throws BadRequestException, NotFoundInDatabaseException, ServiceException {
        long notExistingId = 23;
        when(mockTrackService.get(anyLong())).thenThrow(new NotFoundInDatabaseException("Not found"));
        
        Response r = resource.addToQueue(buildRequest(), createIdPost(notExistingId));
        assertEquals(NOT_FOUND, r.getStatus());
    }
    
    @Test
    public void testPostAddToQueueReturnsServerErrorIfTrackDoesntHaveLocation() throws NotFoundInDatabaseException, ServiceException, BadRequestException{
        long existingId = 23;
        Track track = new Track(existingId);
        track.setLocation(null);
        
        when(mockTrackService.get(existingId)).thenReturn(track);
        Response r = resource.addToQueue(buildRequest(), createIdPost(existingId));
        assertEquals(INTERNAL_SERVER_ERROR, r.getStatus());
    }
    
    @Test
    public void testPostAddToQueueThrowsBadRequestExceptionIfThereisNoId(){
        try {
            resource.addToQueue(buildRequest(), createIdPost(""));
        } catch (BadRequestException e) {
            // This should happen
        }
    }
    
    @Test
    public void testPostAddQueueReturnsInternalServerErrorWhenServiceDoesntWork() throws BadRequestException, NotFoundInDatabaseException, ServiceException{
        long existingId = 23;
        Track track = new Track(existingId);
        track.setLocation("Marsi666//");
        
        when(mockTrackService.get(existingId)).thenThrow(new ServiceException("Lolexception"));
        
        Response r = resource.addToQueue(buildRequest(), createIdPost(existingId));
        assertEquals(INTERNAL_SERVER_ERROR, r.getStatus());
    }
    
    @Test
    public void testSelectPlaylistSelectsPlaylist() throws BadRequestException, NotFoundInDatabaseException, ServiceException{
        long existingId = 23;
        Response r = resource.selectPlaylist(buildRequest(), createIdPost(existingId));
        assertEquals(RESPONSE_OK, r.getStatus());
        verify(getPlayer()).selectPlaylist(existingId);
    }
    
    @Test
    public void testSelectPlaylistReturnsNotFoundWhenPlaylistDoesntExist() throws BadRequestException, NotFoundInDatabaseException, ServiceException{
        long nonExistingId = 23;
        Mockito.doThrow(new NotFoundInDatabaseException("Not found")).when(getPlayer()).selectPlaylist(anyLong());
        
        Response r = resource.selectPlaylist(buildRequest(), createIdPost(nonExistingId));
        assertEquals(NOT_FOUND, r.getStatus());
    }
    
    @Test
    public void testSelectPlaylistReturnsInternalErrorWhenSelectExplodes() throws ServiceException, BadRequestException, NotFoundInDatabaseException{
        long anyId = 23;
        Mockito.doThrow(new ServiceException("Boom!")).when(getPlayer()).selectPlaylist(anyLong());
        
        Response r = resource.selectPlaylist(buildRequest(), createIdPost(anyId));
        assertEquals(INTERNAL_SERVER_ERROR, r.getStatus());
    }
    
    @Test
    public void testSelectPlaylistReturnsBadRequestWhenThereIsNoId(){
        try {
            resource.selectPlaylist(buildRequest(), createIdPost(""));
            Assert.fail("Select playlist shoudl throw BadRequestException if there is no ID in playlist");
        } catch (BadRequestException e) {
            // This should happen
        }
    }
    
    @Test
    public void testPlayCallsPlay() throws ServiceException{
        PlayerStatus status = new PlayerStatus();
        status.setReadyForPlay(true);
        when(getPlayer().status()).thenReturn(status);
        Response r = resource.play(buildRequest());
        assertEquals(RESPONSE_OK, r.getStatus());
        verify(getPlayer()).play();
    }
    
    @Test
    public void testPlayReturnsInternalErrorWhenPlayerThrowsServiceException() throws ServiceException{
        PlayerStatus status = new PlayerStatus();
        status.setReadyForPlay(true);
        when(getPlayer().status()).thenReturn(status);
        Mockito.doThrow(new ServiceException("Boom!")).when(getPlayer()).play();
        Response r = resource.play(buildRequest());
        assertEquals(INTERNAL_SERVER_ERROR, r.getStatus());
    }
}
