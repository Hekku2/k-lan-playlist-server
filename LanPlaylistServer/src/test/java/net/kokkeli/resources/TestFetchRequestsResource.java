package net.kokkeli.resources;

import net.kokkeli.data.services.ServiceException;
import net.kokkeli.resources.models.BaseModel;
import net.kokkeli.server.NotAuthenticatedException;
import net.kokkeli.server.RenderException;

import org.junit.Test;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class TestFetchRequestsResource extends ResourceTestsBase {
    private FetchRequestsResource resource;

    @Override
    public void before() throws Exception {
        resource = new FetchRequestsResource(getLogger(), getTemplateService(), getPlayer(), getSessionService(), getSettings());
    }
    
    @Test
    public void testIndexRedirectWhenRenderingExceptionIsThrown() throws NotAuthenticatedException, ServiceException, RenderException{
        when(getTemplateService().process(any(String.class), any(BaseModel.class))).thenThrow(new RenderException("Boom says database!"));
        
        assertRedirectError(resource.index(buildRequest()), "There was a problem with rendering the template.");
    }
}
