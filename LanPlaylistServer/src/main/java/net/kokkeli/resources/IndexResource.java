package net.kokkeli.resources;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import com.google.inject.Inject;

import net.kokkeli.data.ILogger;
import net.kokkeli.data.Track;
import net.kokkeli.data.Role;
import net.kokkeli.data.db.NotFoundInDatabase;
import net.kokkeli.data.db.PlayList;
import net.kokkeli.data.services.IPlaylistService;
import net.kokkeli.data.services.ISessionService;
import net.kokkeli.data.services.ServiceException;
import net.kokkeli.player.IPlayer;
import net.kokkeli.player.NotPlaylistPlayingException;
import net.kokkeli.resources.models.BaseModel;
import net.kokkeli.resources.models.ModelPlaylist;
import net.kokkeli.resources.models.ModelPlaylistItem;
import net.kokkeli.server.ITemplateService;
import net.kokkeli.server.NotAuthenticatedException;
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
    
    private IPlaylistService playlistService;
    
    /**
     * Creates resource
     * @param logger Logger
     * @param templateService TemplateService
     * @param player Player
     */
    @Inject
    protected IndexResource(ILogger logger, ITemplateService templateService,
            IPlayer player, IPlaylistService playlistService, ISessionService sessions) {
        super(logger, templateService, player, sessions);
        
        this.playlistService = playlistService;
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
    public Response index(@Context HttpServletRequest req) throws NotAuthenticatedException, ServiceException {
        try {
            
            BaseModel base = buildBaseModel(req);
            try {
                long currentPlaylist = player.getCurrentPlaylistId();
                PlayList playlist = playlistService.getPlaylist(currentPlaylist);
                ModelPlaylist modelPlayList = new ModelPlaylist(playlist.getId());
                modelPlayList.setName(playlist.getName());
                
                for (Track playListItem : playlist.getItems()) {
                    ModelPlaylistItem model = new ModelPlaylistItem();
                    model.setArtist(playListItem.getArtist());
                    model.setTrackName(playListItem.getTrackName());
                    
                    modelPlayList.getItems().add(model);
                }
                base.setModel(modelPlayList);
                
            } catch (NotPlaylistPlayingException e) {
                // Suppress. If no playlist is playing, index page is still shown.
            } catch (NotFoundInDatabase e) {
                base.setError("For some reason, currently playing playlist was not found.");
            } catch (ServiceException e) {
                base.setError("For some reason, currently playing playlist can't be shown.");
            }

            return Response.ok(templates.process(INDEX_TEMPLATE, base)).build();
        } catch (RenderException e) {
            // If index page can't be shown, there is nothing to be done.
            throw new ServiceException("There was problem with rendering.", e);
        }
    }
}
