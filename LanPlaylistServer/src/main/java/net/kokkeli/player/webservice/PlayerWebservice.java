package net.kokkeli.player.webservice;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.google.inject.Inject;

import net.kokkeli.data.db.NotFoundInDatabaseException;
import net.kokkeli.data.services.ServiceException;
import net.kokkeli.player.IPlayer;
import net.kokkeli.player.PlayerStatus;
import net.kokkeli.resources.BadRequestException;

/**
 * Purpose of this class is to act as a webservice to offer process-to-process commmunication between lanserver and player.
 * @author Hekku2
 *
 */
@Path("/player")
public class PlayerWebservice {
    private static final String FORM_ID = "id";
    private static final String FORM_LOCATION = "location";
    
    private final IPlayer player;
    
    @Inject
    public PlayerWebservice(IPlayer player){
        this.player = player;
    }
    
    @POST
    @Produces("text/html")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Path("/selectPlaylist")
    public Response selectPlaylist(@Context HttpServletRequest req, MultivaluedMap<String, String> formParams) throws BadRequestException{
        try {
            long id = Long.parseLong(formParams.getFirst(FORM_ID));           
            player.selectPlaylist(id);
            return Response.ok().build();
        } catch (NumberFormatException e) {
            throw new BadRequestException("Id was not in correct format.", e);
        } catch (NotFoundInDatabaseException e) {
            return Response.status(Status.NOT_FOUND).build();
        } catch (ServiceException e) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @POST
    @Produces("text/html")
    @Path("/play")
    public Response play(@Context HttpServletRequest req){
        try {
            player.play();
            return Response.ok().build();
        } catch (ServiceException e) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @POST
    @Produces("text/html")
    @Path("/pause")
    public Response pause(@Context HttpServletRequest req){
        try {
            player.pause();
            return Response.ok().build();
        } catch (ServiceException e) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @POST
    @Produces("text/html")
    @Path("/addToQueue")
    public Response addToQueue(@Context HttpServletRequest req, MultivaluedMap<String, String> formParams) throws BadRequestException{
        try {
            String location = formParams.getFirst(FORM_LOCATION);
            if (location == null)
                throw new BadRequestException("Location was not in correct format.");
            player.addToQueue(location);
            return Response.ok().build();
        } catch (ServiceException e) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GET
    @Path("/playlistPlaying")
    @Produces(MediaType.APPLICATION_JSON)
    public PlayerStatus playlistPlaying(@Context HttpServletRequest req) throws ServiceException{
        PlayerStatus playerStatus = new PlayerStatus();
        playerStatus.setReadyForPlay(player.status().getReadyForPlay());
        playerStatus.setPlaying(player.status().getPlaying());
        if (playerStatus.getPlaying())
            playerStatus.setSelectedPlaylist(player.status().getSelectedPlaylist());
        return playerStatus;
    }
}
