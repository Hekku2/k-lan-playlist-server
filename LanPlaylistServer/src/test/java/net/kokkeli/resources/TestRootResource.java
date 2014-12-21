package net.kokkeli.resources;

import static org.junit.Assert.assertEquals;
import javax.ws.rs.core.Response;

import org.junit.Test;

import net.kokkeli.data.services.ServiceException;

public class TestRootResource extends ResourceTestsBase<RootResource>{    
    @Override
    public void before() throws Exception {
        resource = new RootResource(getLogger(), getTemplateService(), getPlayer(), getSessionService(), getSettings());
    }
    
    @Test
    public void testRedirect() throws ServiceException{
        Response r = resource.redirect(buildRequest());
        assertEquals(REDIRECT, r.getStatus());
    }
}
