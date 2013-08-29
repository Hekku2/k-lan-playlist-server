package net.kokkeli.resources;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.junit.Test;
import org.mockito.Mockito;

import net.kokkeli.data.Track;
import net.kokkeli.data.db.NotFoundInDatabase;
import net.kokkeli.data.services.ITrackService;
import net.kokkeli.data.services.ServiceException;

public class TestRootResource extends ResourceTestsBase{
    private static final String FORM_ID = "id";
    
    private ITrackService mockTrackService;
    private RootResource resource;
    
    @Override
    public void before() throws Exception {
        mockTrackService = mock(ITrackService.class);
        resource = new RootResource(getLogger(), getTemplateService(), getPlayer(), getSessionService(), getSettings(), mockTrackService);
    }

    @Test
    public void testPostAddToQueueAddsSongToQueue() throws NotFoundInDatabase, ServiceException, BadRequestException{
        long existingId = 23;
        Track track = new Track(existingId);
        track.setLocation("Marsi666//");
        
        when(mockTrackService.get(existingId)).thenReturn(track);
        
        Response r = resource.addToQueue(buildRequest(), createIdPost(existingId));
        assertEquals(RESPONSE_OK, r.getStatus());
        verify(getPlayer()).addToQueue(track.getLocation());
    }
    
    @Test
    public void testPostAddToQueueReturnsNotFoundWhenTrackIsNotFound() throws BadRequestException, NotFoundInDatabase, ServiceException {
        long notExistingId = 23;
        when(mockTrackService.get(anyLong())).thenThrow(new NotFoundInDatabase("Not found"));
        
        Response r = resource.addToQueue(buildRequest(), createIdPost(notExistingId));
        assertEquals(NOT_FOUND, r.getStatus());
    }
    
    @Test
    public void testSelectPlaylistSelectsPlaylist() throws BadRequestException, NotFoundInDatabase, ServiceException{
        long existingId = 23;
        Response r = resource.selectPlaylist(buildRequest(), createIdPost(existingId));
        assertEquals(RESPONSE_OK, r.getStatus());
        verify(getPlayer()).selectPlaylist(existingId);
    }
    
    @Test
    public void testSelectPlaylistReturnsNotFoundWhenPlaylistDoesntExist() throws NotFoundInDatabase, ServiceException, BadRequestException{
        long nonExistingId = 23;
        Mockito.doThrow(new NotFoundInDatabase("Not found")).when(getPlayer()).selectPlaylist(anyLong());
        
        Response r = resource.selectPlaylist(buildRequest(), createIdPost(nonExistingId));
        assertEquals(NOT_FOUND, r.getStatus());
    }
    
    @Test
    public void testSelectPlaylistReturnsInternalErrorWhenSelectExplodes() throws NotFoundInDatabase, ServiceException, BadRequestException{
        long anyId = 23;
        Mockito.doThrow(new ServiceException("Boom!")).when(getPlayer()).selectPlaylist(anyLong());
        
        Response r = resource.selectPlaylist(buildRequest(), createIdPost(anyId));
        assertEquals(INTERNAL_SERVER_ERROR, r.getStatus());
    }
    
    @Test
    public void testPlayCallsPlay() throws BadRequestException, ServiceException{
        Response r = resource.play(buildRequest());
        assertEquals(RESPONSE_OK, r.getStatus());
        verify(getPlayer()).play();
    }
    
    @Test
    public void testPlayReturnsInternalErrorWhenPlayerThrowsServiceException() throws ServiceException, BadRequestException{
        Mockito.doThrow(new ServiceException("Boom!")).when(getPlayer()).play();
        Response r = resource.play(buildRequest());
        assertEquals(INTERNAL_SERVER_ERROR, r.getStatus());
    }
    
    /**
     * Mocks MultivaluedMap for id creation posts.
     * @param id
     * @return Multivalued map
     */
    private static MultivaluedMap<String, String> createIdPost(long id){
        @SuppressWarnings("unchecked")
        MultivaluedMap<String, String> map = mock(MultivaluedMap.class);
        
        when(map.containsKey(FORM_ID)).thenReturn(true);
        
        when(map.getFirst(FORM_ID)).thenReturn(id + "");
        return map;
    }
}
