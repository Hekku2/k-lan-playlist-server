package net.kokkeli.resources;

import javax.ws.rs.core.Response;

import net.kokkeli.server.RenderException;

import org.junit.Assert;
import org.junit.Test;

public class TestRenderExceptionMapper {
    private RenderExceptionMapper mapper;
    
    @Test
    public void testToResponseBuildsCorrectMessage(){
        String message = "olo";
        
        mapper = new RenderExceptionMapper();

        Response r = mapper.toResponse(new RenderException(message));
        Assert.assertEquals(message + ": Render exception", r.getEntity().toString());
        Assert.assertEquals(500, r.getStatus());
    }

}
