package net.kokkeli.resources;

import javax.inject.Singleton;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
@Singleton
public class BadRequestExceptionMapper implements ExceptionMapper<BadRequestException> { 

    /**
     * Creates response fro render Exception
     * @param ex Exception
     */
    @Override
    public Response toResponse(BadRequestException ex) {
        return Response.status(500).
            entity(ex.getMessage()).
            type("text/plain").
            build();
    }
}

