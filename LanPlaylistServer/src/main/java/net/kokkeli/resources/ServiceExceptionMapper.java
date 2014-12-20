package net.kokkeli.resources;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import net.kokkeli.data.services.ServiceException;

/**
 * Maps ServiceException to proper response
 * @author Hekku2
 *
 */
@Provider
public class ServiceExceptionMapper implements ExceptionMapper<ServiceException> { 

    /**
     * Creates response fro render Exception
     * @param ex Exception
     */
    @Override
    public Response toResponse(ServiceException ex) {
        return Response.status(500).
            entity(ex.getMessage() + " : Service exception").
            type("text/plain").
            build();
    }
}