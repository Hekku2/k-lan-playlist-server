package net.kokkeli.resources;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import com.google.inject.Inject;

import net.kokkeli.data.ILogger;
import net.kokkeli.data.Role;
import net.kokkeli.data.services.ServiceException;
import net.kokkeli.resources.models.ModelPlaylist;
import net.kokkeli.server.ITemplateService;
import net.kokkeli.server.RenderException;

/**
 * Class for root resources.
 * 
 * Should only redirect to login or main page.
 * @author Hekku2
 *
 */
@Path("/")
public class RootResource extends BaseResource {
    private static final String INDEX_TEMPLATE = "index.ftl";
    
    /**
     * Creates resource
     * @param logger
     */
    @Inject
    protected RootResource(ILogger logger, ITemplateService templateService) {
        super(logger, templateService);
    }
   
    /**
     * Redirects user to correct page
     * @return HTML-page, either login or main page
     * @throws ServiceException Thrown when there is problem with rendering.
     */
    @GET
    @Produces("text/html")
    @Access(Role.USER)
    public Response redirect(@Context HttpServletRequest req) throws ServiceException {
        ModelPlaylist mockList = new ModelPlaylist();

        try {
            return Response.ok(templates.process(INDEX_TEMPLATE, mockList)).build();
        } catch (RenderException e) {
            throw new ServiceException("There was problem with rendering.", e);
        }
    }
}
