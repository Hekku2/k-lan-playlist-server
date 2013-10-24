package net.kokkeli.resources;

import javax.ws.rs.core.Response;

import net.kokkeli.server.RenderException;

import org.junit.Assert;
import org.junit.Test;

public class TestRenderExceptionMapper {

    @Test
    public void testToResponseBuildsCorrectMessage(){
        String message = "olo";
        
        RenderExceptionMapper m = new RenderExceptionMapper();

        Response r = m.toResponse(new RenderException(message));
        Assert.assertEquals(message, r.getEntity().toString());
        Assert.assertEquals(500, r.getStatus());
    }

}
