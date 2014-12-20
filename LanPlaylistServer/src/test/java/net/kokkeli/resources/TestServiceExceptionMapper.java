package net.kokkeli.resources;

import javax.ws.rs.core.Response;

import net.kokkeli.data.services.ServiceException;
import org.junit.Assert;
import org.junit.Test;

public class TestServiceExceptionMapper {
    private ServiceExceptionMapper mapper;
    
    @Test
    public void testToResponseBuildsCorrectMessage(){
        String message = "olo";
        
        mapper = new ServiceExceptionMapper();

        Response r = mapper.toResponse(new ServiceException(message));
        Assert.assertEquals(message + " : Service exception", r.getEntity().toString());
        Assert.assertEquals(500, r.getStatus());
    }

}
