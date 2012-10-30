package net.kokkeli.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import net.kokkeli.server.RenderException;
import net.kokkeli.server.Templates;

@Path("/")
public class RootResource {
    private static final String INDEX_TEMPLATE = "index.ftl";
    
    @GET
    @Produces("text/html")
    public String redirect() throws RenderException {
        return Templates.process(INDEX_TEMPLATE);
    }
}
