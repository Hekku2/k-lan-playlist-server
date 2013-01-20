package net.kokkeli.resources;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import com.google.inject.Inject;

import net.kokkeli.data.ILogger;
import net.kokkeli.data.Role;
import net.kokkeli.data.User;
import net.kokkeli.data.services.IPlaylistService;
import net.kokkeli.player.IPlayer;
import net.kokkeli.resources.authentication.AuthenticationUtils;
import net.kokkeli.resources.models.BaseModel;
import net.kokkeli.resources.models.ModelUser;
import net.kokkeli.resources.models.ModelUsers;
import net.kokkeli.server.ITemplateService;
import net.kokkeli.server.RenderException;

/**
 * Resources for playlist management
 * @author Hekku2
 *
 */
@Path("/playlists")
public class PlaylistsResource extends BaseResource {
    private static final String PLAYLISTS_TEMPLATE = "playlists.ftl";
    
    private final IPlaylistService playlistService;
    
    /**
     * Creates resource
     * @param logger
     * @param templateService
     * @param player
     */
    @Inject
    protected PlaylistsResource(ILogger logger, ITemplateService templateService, IPlayer player, IPlaylistService playlistService) {
        super(logger, templateService, player);
        
        this.playlistService = playlistService;
    }

    /**
     * Shows list of users
     * @return HTML-page for user list
     * @throws RenderException Thrown if there is problem with rendering template
     */
    @GET
    @Produces("text/html")
    @Access(Role.ADMIN)
    public Response playlists(@Context HttpServletRequest req) throws RenderException {
        BaseModel model = buildBaseModel();
        model.setUsername(AuthenticationUtils.extractUsername(req));
        
        return Response.ok(templates.process(PLAYLISTS_TEMPLATE, model)).build();
    }
}
