package net.kokkeli.resources;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import com.google.inject.Inject;

import net.kokkeli.data.dto.ILogger;
import net.kokkeli.data.dto.Role;
import net.kokkeli.data.services.ISessionService;
import net.kokkeli.data.services.ServiceException;
import net.kokkeli.player.IPlayer;
import net.kokkeli.server.ITemplateService;
import net.kokkeli.settings.ISettings;

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
    protected RootResource(ILogger logger, ITemplateService templateService, IPlayer player, ISessionService sessions, ISettings settings) {
        super(logger, templateService, player, sessions, settings);
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
        return Response.seeOther(settings.getURI("index")).build();
    }
}
