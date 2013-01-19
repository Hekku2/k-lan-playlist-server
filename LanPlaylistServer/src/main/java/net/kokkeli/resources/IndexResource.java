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
import net.kokkeli.player.IPlayer;
import net.kokkeli.resources.authentication.AuthenticationUtils;
import net.kokkeli.resources.models.BaseModel;
import net.kokkeli.resources.models.ModelPlaylist;
import net.kokkeli.server.ITemplateService;
import net.kokkeli.server.RenderException;

/**
 * Index-resource
 * 
 * Resources related to index page.
 * @author Hekku2
 *
 */
@Path("/index")
public class IndexResource extends BaseResource {
    private static final String INDEX_TEMPLATE = "index.ftl";
    
    /**
     * Creates resource
     * @param logger Logger
     * @param templateService TemplateService
     * @param player Player
     */
    @Inject
    protected IndexResource(ILogger logger, ITemplateService templateService,
            IPlayer player) {
        super(logger, templateService, player);
    }

    /**
     * Shows index page for user
     * @return HTML-page, main page
     * @throws ServiceException Thrown when there is problem with rendering.
     */
    @GET
    @Produces("text/html")
    @Access(Role.USER)
    public Response index(@Context HttpServletRequest req) throws ServiceException {
        ModelPlaylist mockList = new ModelPlaylist();
        
        BaseModel base = buildBaseModel();
        base.setUsername(AuthenticationUtils.extractUsername(req));
        
        base.setModel(mockList);
        try {
            return Response.ok(templates.process(INDEX_TEMPLATE, base)).build();
        } catch (RenderException e) {
            throw new ServiceException("There was problem with rendering.", e);
        }
    }
}
