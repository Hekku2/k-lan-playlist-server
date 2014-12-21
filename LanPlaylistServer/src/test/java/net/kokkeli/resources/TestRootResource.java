package net.kokkeli.resources;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import javax.ws.rs.core.Response;

import org.junit.Test;
import net.kokkeli.data.services.ITrackService;
import net.kokkeli.data.services.ServiceException;

public class TestRootResource extends ResourceTestsBase{
    private ITrackService mockTrackService;
    private RootResource resource;
    
    @Override
    public void before() throws Exception {
        mockTrackService = mock(ITrackService.class);
        resource = new RootResource(getLogger(), getTemplateService(), getPlayer(), getSessionService(), getSettings(), mockTrackService);
    }
    
    @Test
    public void testRedirect() throws ServiceException{
        Response r = resource.redirect(buildRequest());
        assertEquals(REDIRECT, r.getStatus());
    }
}
