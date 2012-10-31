package net.kokkeli.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import net.kokkeli.server.RenderException;
import net.kokkeli.server.Templates;

/**
 * Class for root resources.
 * 
 * Should only redirect to login or main page.
 * @author Hekku2
 *
 */
@Path("/")
public class RootResource {
    private static final String INDEX_TEMPLATE = "index.ftl";
   
    /**
     * Redirects user to correct page
     * @return HTML-page, either login or main page
     * @throws RenderException Thrown if there is problem with rendering template
     */
    @GET
    @Produces("text/html")
    public String redirect() throws RenderException {
        return Templates.process(INDEX_TEMPLATE);
    }
   
}
