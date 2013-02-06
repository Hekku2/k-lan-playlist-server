package net.kokkeli.server;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.ext.ExceptionMapper;

/**
 * Maps not authenticated exception to redirect.
 * @author Hekku2
 *
 */
public class NotAuthenticatedExceptionMapper implements ExceptionMapper<NotAuthenticatedException>{
    /**
     * Creates response from NotAuthenticatedException
     * @param ex Exception
     */
    public Response toResponse(NotAuthenticatedException ex) {
        return Response.seeOther(UriBuilder.fromUri(LanServer.getBaseURI()).path("/authentication").build()).build();
    }
}
