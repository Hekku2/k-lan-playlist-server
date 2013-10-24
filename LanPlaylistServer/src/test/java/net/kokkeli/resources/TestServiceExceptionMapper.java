package net.kokkeli.resources;

import javax.ws.rs.core.Response;

import net.kokkeli.data.services.ServiceException;
import org.junit.Assert;
import org.junit.Test;

public class TestServiceExceptionMapper {

    @Test
    public void testToResponseBuildsCorrectMessage(){
        String message = "olo";
        
        ServiceExceptionMapper m = new ServiceExceptionMapper();

        Response r = m.toResponse(new ServiceException(message));
        Assert.assertEquals(message, r.getEntity().toString());
        Assert.assertEquals(500, r.getStatus());
    }

}
