package net.kokkeli.server;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import net.kokkeli.ISettings;

import com.google.inject.Inject;

/**
 * Maps not authenticated exception to redirect.
 * @author Hekku2
 *
 */
@Provider
public class NotAuthenticatedExceptionMapper implements ExceptionMapper<NotAuthenticatedException>{
    
    @Inject
    protected ISettings settings;
    
    /**
     * Creates response from NotAuthenticatedException
     * @param ex Exception
     */
    @Override
    public Response toResponse(NotAuthenticatedException ex) {
        return Response.seeOther(UriBuilder.fromUri(settings.getBaseURI()).path("/authentication").build()).build();
    }
}
