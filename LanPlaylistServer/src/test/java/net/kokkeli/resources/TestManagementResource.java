package net.kokkeli.resources;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import net.kokkeli.data.db.NotFoundInDatabase;
import net.kokkeli.data.services.ServiceException;
import net.kokkeli.resources.models.BaseModel;
import net.kokkeli.server.NotAuthenticatedException;
import net.kokkeli.server.RenderException;

import org.junit.Test;

import com.sun.jersey.api.NotFoundException;

public class TestManagementResource extends ResourceTestsBase{
    private ManagementResource resource;
    
    @Override
    public void before() throws NotFoundException, ServiceException, NotFoundInDatabase {
        resource = new ManagementResource(getLogger(), getTemplateService(), getPlayer(), getSessionService(), getSettings());
    }
    
    @Test
    public void testIndexReturnsResponse() throws RenderException, NotAuthenticatedException {
        ModelAnswer model = new ModelAnswer();
        when(getTemplateService().process(any(String.class), any(BaseModel.class))).thenAnswer(model);
        
        assertModelResponse(resource.index(buildRequest()), model, null, null);
    }
    
    @Test
    public void testIndexRedirectsWhenRenderingExceptionsIsThrown() throws RenderException, NotAuthenticatedException{
        when(getTemplateService().process(any(String.class), any(BaseModel.class))).thenThrow(new RenderException("Boom says database!"));
        
        assertRedirectError(resource.index(buildRequest()), "There was a problem with rendering the template.");
    }
}
