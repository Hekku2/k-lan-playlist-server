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
import net.kokkeli.player.IPlayer;
import net.kokkeli.resources.models.BaseModel;
import net.kokkeli.server.ITemplateService;
import net.kokkeli.server.NotAuthenticatedException;
import net.kokkeli.server.RenderException;

/**
 * Index-resource
 * 
 * Resources related to fetch request pages.
 * @author Hekku2
 *
 */
@Path("/fetchers")
public class FetchRequestsResource extends BaseResource{

    private static final String INDEX_TEMPLATE = "fetchers/index.ftl";
    
    /**
     * Initializes new resource for fetch request related resources
     * @param logger Logger
     * @param templateService Template service
     * @param player Player
     * @param sessions Sessions
     * @param settings Settings
     */
    @Inject
    protected FetchRequestsResource(ILogger logger,
            ITemplateService templateService, IPlayer player,
            ISessionService sessions, ISettings settings) {
        super(logger, templateService, player, sessions, settings);
    }

    @GET
    @Produces("text/html")
    @Access(Role.USER)
    public Response index(@Context HttpServletRequest req) throws NotAuthenticatedException{
        BaseModel model = buildBaseModel(req);
        try {
            return Response.ok(templates.process(INDEX_TEMPLATE, model)).build();
        } catch (RenderException e) {
            return handleRenderingError(model, e);
        }

    }
}
