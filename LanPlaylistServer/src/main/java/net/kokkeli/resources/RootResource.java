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
    
    /**
     * Creates resource
     * @param logger
     */
    @Inject
    protected RootResource(ILogger logger, ITemplateService templateService) {
        super(logger, templateService);
    }

    private static final String INDEX_TEMPLATE = "index.ftl";
   
    /**
     * Redirects user to correct page
     * @return HTML-page, either login or main page
     * @throws RenderException Thrown if there is problem with rendering template
     */
    @GET
    @Produces("text/html")
    @Access(Role.USER)
    public Response redirect(@Context HttpServletRequest req) throws RenderException {
        ModelPlaylist mockList = new ModelPlaylist();

        return Response.ok(templates.process(INDEX_TEMPLATE, mockList)).build();
    }
   
}
