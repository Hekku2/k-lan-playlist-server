package net.kokkeli.resources;

import java.util.ArrayList;

import javax.ws.rs.core.Response;

import net.kokkeli.data.FetchRequest;
import net.kokkeli.data.services.IFetchRequestService;
import net.kokkeli.data.services.ServiceException;
import net.kokkeli.resources.models.BaseModel;
import net.kokkeli.resources.models.ModelFetchRequests;
import net.kokkeli.server.NotAuthenticatedException;
import net.kokkeli.server.RenderException;

import org.junit.Assert;
import org.junit.Test;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class TestFetchRequestsResource extends ResourceTestsBase {
    private FetchRequestsResource resource;
    private IFetchRequestService mockFetchRequestService;

    @Override
    public void before() throws Exception {
        mockFetchRequestService = mock(IFetchRequestService.class);
        
        resource = new FetchRequestsResource(getLogger(), getTemplateService(), getPlayer(), getSessionService(), getSettings(), mockFetchRequestService, null);
    }
    
    @Test
    public void testIndexRedirectWhenRenderingExceptionIsThrown() throws NotAuthenticatedException, ServiceException, RenderException{
        when(getTemplateService().process(any(String.class), any(BaseModel.class))).thenThrow(new RenderException("Boom says database!"));
        assertRedirectError(resource.index(buildRequest()), "There was a problem with rendering the template.");
    }
    
    @Test
    public void testIndexRedirectWhenServiceExceptionIsThrown() throws NotAuthenticatedException, ServiceException, RenderException{
        when(mockFetchRequestService.get()).thenThrow(new ServiceException("Boom says database!"));
        assertRedirectError(resource.index(buildRequest()), "Something went wrong with service.");
    }
    
    @Test
    public void testIndexReturnsCorrectItems() throws NotAuthenticatedException, ServiceException, RenderException{
        ArrayList<FetchRequest> requests = new ArrayList<FetchRequest>();
        FetchRequest basicRequest = new FetchRequest();
        basicRequest.setDestinationFile("Test destination");
        basicRequest.setHandler("test handler");
        basicRequest.setLocation("Test location");
        
        when(mockFetchRequestService.get()).thenReturn(requests);
        
        ModelAnswer answer = new ModelAnswer();
        when(getTemplateService().process(any(String.class), any(BaseModel.class))).thenAnswer(answer);
        
        Response response = resource.index(buildRequest());
        assertModelResponse(response, answer, null, null);
        
        BaseModel base = answer.getModel();
        Assert.assertTrue(base.getModel() instanceof ModelFetchRequests);
    }
}
