package net.kokkeli.resources;

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

import net.kokkeli.ISettings;
import net.kokkeli.ValidationUtils;
import net.kokkeli.data.ILogger;
import net.kokkeli.data.LogSeverity;
import net.kokkeli.data.Role;
import net.kokkeli.data.Track;
import net.kokkeli.data.db.NotFoundInDatabaseException;
import net.kokkeli.data.services.ISessionService;
import net.kokkeli.data.services.ITrackService;
import net.kokkeli.data.services.ServiceException;
import net.kokkeli.player.IPlayer;
import net.kokkeli.resources.models.BaseModel;
import net.kokkeli.server.ITemplateService;
import net.kokkeli.server.RenderException;

@Path("/player")
public class PlayerResource extends BaseResource{
    private static final String FORM_ID = "id";
    
    private static final String STATUS_TEMPLATE = "player/status.ftl";
    
    private final ITrackService trackService;
    
    @Inject
    protected PlayerResource(ILogger logger, ITemplateService templateService, IPlayer player, ISessionService sessions, ISettings settings, ITrackService trackService) {
        super(logger, templateService, player, sessions, settings);
        
        this.trackService = trackService;
    }
    
    @GET
    @Produces("text/html; charset=utf-8")
    @Access(Role.ANYNOMOUS)
    @Path("/status")
    public Response playingPage(@Context HttpServletRequest req){
        BaseModel model = buildBaseModel(req);
        try {
            return Response.ok(templates.process(STATUS_TEMPLATE, model)).build();
        } catch (RenderException e) {
            return handleRenderingError(model, e);
        }
    }
    
    @POST
    @Produces("text/html")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Access(value = Role.ADMIN, errorHandling = AuthenticationErrorHandling.RETURN_CODE)
    @Path("/addToQueue")
    public Response addToQueue(@Context HttpServletRequest req, MultivaluedMap<String, String> formParams) throws BadRequestException{
        try {
            long id = Long.parseLong(formParams.getFirst(FORM_ID));
            
            Track track = trackService.get(id);
            
            if (ValidationUtils.isEmpty(track.getLocation())){
                log(String.format("Track #%s did not have a location.", track.getId()), LogSeverity.TRACE);
                return Response.status(Status.INTERNAL_SERVER_ERROR).build();
            }
            
            player.addToQueue(track.getLocation());
            log(String.format("Track #%s added to queue.", track.getId()),LogSeverity.TRACE);
            return Response.ok().build();
        } catch (NumberFormatException e) {
            throw new BadRequestException("Id was not in correct format.", e);
        } catch (ServiceException e) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        } catch (NotFoundInDatabaseException e) {
            return Response.status(Status.NOT_FOUND).build();
        }
    }
    
    @POST
    @Produces("text/html")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Access(value = Role.ADMIN, errorHandling = AuthenticationErrorHandling.RETURN_CODE)
    @Path("/selectPlaylist")
    public Response selectPlaylist(@Context HttpServletRequest req, MultivaluedMap<String, String> formParams) throws BadRequestException{
        try {
            long id = Long.parseLong(formParams.getFirst(FORM_ID));
            player.selectPlaylist(id);
            
            log(String.format("Playlist #%s selected.", id),LogSeverity.TRACE);
            return Response.ok().build();
        } catch (NumberFormatException e) {
            throw new BadRequestException("Id was not in correct format.", e);
        } catch (ServiceException e) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        } catch (NotFoundInDatabaseException e) {
            return Response.status(Status.NOT_FOUND).build();
        }
    }
    
    @POST
    @Produces("text/html")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Access(value = Role.ADMIN, errorHandling = AuthenticationErrorHandling.RETURN_CODE)
    @Path("/play")
    public Response play(@Context HttpServletRequest req){
        try {
            if (player.readyForPlay()){
                player.play();
                log("Playing started.", LogSeverity.TRACE);
            }
            return Response.ok().build();
        } catch (ServiceException e) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @POST
    @Produces("text/html")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Access(value = Role.ADMIN, errorHandling = AuthenticationErrorHandling.RETURN_CODE)
    @Path("/pause")
    public Response pause(@Context HttpServletRequest req){
        player.pause();
        log("Playing paused.", LogSeverity.TRACE);
        return Response.ok().build();
    }
    
    @GET
    @Produces("text/html")
    @Access(value = Role.ANYNOMOUS, errorHandling = AuthenticationErrorHandling.RETURN_CODE)
    @Path("/nowPlaying")
    public Response getPlaying(@Context HttpServletRequest req){
        return Response.ok().entity(player.getTitle()).build();
    }
}
