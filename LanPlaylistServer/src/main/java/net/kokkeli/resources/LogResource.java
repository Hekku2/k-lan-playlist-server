package net.kokkeli.resources;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.inject.Inject;

import net.kokkeli.data.dto.ILogger;
import net.kokkeli.data.dto.LogRow;
import net.kokkeli.data.dto.Role;
import net.kokkeli.data.services.ILogService;
import net.kokkeli.data.services.ISessionService;
import net.kokkeli.data.services.ServiceException;
import net.kokkeli.player.IPlayer;
import net.kokkeli.resources.models.BaseModel;
import net.kokkeli.server.ITemplateService;
import net.kokkeli.server.NotAuthenticatedException;
import net.kokkeli.server.RenderException;
import net.kokkeli.settings.ISettings;

/**
 * Resource for log viewing related operations.
 * @author Hekku2
 */
@Path("/log")
public class LogResource extends BaseResource {
    private static final String INDEX_TEMPLATE = "log/index.ftl";
    
    private final ILogService logService;
    
    /**
     * Creates a new instace of LogResource
     * @param logger Logger
     * @param templateService Template service
     * @param player Player
     * @param sessions Sessions
     * @param settings Settings
     * @param logService Log service
     */
    @Inject
    protected LogResource(ILogger logger, ITemplateService templateService, IPlayer player, ISessionService sessions,
            ISettings settings, ILogService logService) {
        super(logger, templateService, player, sessions, settings);
        
        this.logService = logService;
    }

    /**
     * Shows index page for admin
     * @return HTML-page, main page
     * @throws NotAuthenticatedException Thrown if there is a problem with session.
     */
    @GET
    @Produces("text/html")
    @Access(Role.ADMIN)
    public Response index(@Context HttpServletRequest req) throws NotAuthenticatedException {
        BaseModel base = buildBaseModel(req);
        try {
            return Response.ok(templates.process(INDEX_TEMPLATE, base)).build();
        } catch (RenderException e) {
            return handleRenderingError(base, e);
        }
    }
    
    /**
     * Returns json containing log files
     * @param req Request
     * @return Json containing log files
     * @throws ServiceException Thrown if there is a problem in the service
     * @throws NotAuthenticatedException Thrown if there is a problem with session.
     */
    @GET
    @Path("/logs")
    @Produces(MediaType.APPLICATION_JSON)
    @Access(Role.ADMIN)
    public Collection<LogRow> logRows(@Context HttpServletRequest req) throws ServiceException, NotAuthenticatedException {
        //TODO Time selection
        return logService.get();
    }
}
