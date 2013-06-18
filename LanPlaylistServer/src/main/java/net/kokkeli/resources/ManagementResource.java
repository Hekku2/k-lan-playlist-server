package net.kokkeli.resources;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import com.google.inject.Inject;

import net.kokkeli.ISettings;
import net.kokkeli.data.ILogger;
import net.kokkeli.data.Role;
import net.kokkeli.data.services.ISessionService;
import net.kokkeli.data.services.ServiceException;
import net.kokkeli.player.IPlayer;
import net.kokkeli.resources.models.BaseModel;
import net.kokkeli.server.ITemplateService;
import net.kokkeli.server.NotAuthenticatedException;
import net.kokkeli.server.RenderException;

/**
 * Resources for Management
 * 
 * @author Hekku2
 * 
 */
@Path("/management")
public class ManagementResource extends BaseResource {
    private static final String INDEX_TEMPLATE = "management/index.ftl";
    
    /**
     * Creates resource
     * @param logger Logger
     * @param templateService TemplateService
     * @param player Player
     */
    @Inject
    protected ManagementResource(ILogger logger,
            ITemplateService templateService, IPlayer player,
            ISessionService sessions, ISettings settings) {
        super(logger, templateService, player, sessions, settings);
    }

    /**
     * Shows index page for user
     * @return HTML-page, main page
     * @throws ServiceException Thrown when there is problem with rendering.
     * @throws NotAuthenticatedException Thrown if there is problem with session.
     */
    @GET
    @Produces("text/html")
    @Access(Role.USER)
    public Response index(@Context HttpServletRequest req) throws NotAuthenticatedException {
        BaseModel base = buildBaseModel(req);
        try {
            return Response.ok(templates.process(INDEX_TEMPLATE, base)).build();
        } catch (RenderException e) {
            return handleRenderingError(base, e);
        }
    }
}
