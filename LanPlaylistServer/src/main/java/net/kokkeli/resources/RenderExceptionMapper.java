package net.kokkeli.resources;

import javax.inject.Singleton;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;



import net.kokkeli.server.RenderException;

@Provider
@Singleton
public class RenderExceptionMapper implements ExceptionMapper<RenderException> { 

    /**
     * Creates response fro render Exception
     * @param ex Exception
     */
    @Override
    public Response toResponse(RenderException ex) {
        return Response.status(500).
            entity(ex.getMessage() + ": Render exception").
            type("text/plain").
            build();
    }
}
