package net.kokkeli.resources;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.junit.Test;
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
    
    /**
     * Mocks MultivaluedMap var user creation posts.
     * @param username
     * @param role
     * @return
     */
    private static MultivaluedMap<String, String> createIdPost(long id){
        @SuppressWarnings("unchecked")
        MultivaluedMap<String, String> map = mock(MultivaluedMap.class);
        
        when(map.containsKey(FORM_ID)).thenReturn(true);
        
        when(map.getFirst(FORM_ID)).thenReturn(id + "");
        return map;
    }
}
