package net.kokkeli.resources;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import com.google.inject.Inject;

import net.kokkeli.ISettings;
import net.kokkeli.ModelBuilder;
import net.kokkeli.ValidationUtils;
import net.kokkeli.data.ILogger;
import net.kokkeli.data.Role;
import net.kokkeli.data.Track;
import net.kokkeli.data.db.NotFoundInDatabase;
import net.kokkeli.data.services.ISessionService;
import net.kokkeli.data.services.ITrackService;
import net.kokkeli.data.services.ServiceException;
import net.kokkeli.player.IPlayer;
import net.kokkeli.resources.models.BaseModel;
import net.kokkeli.resources.models.ModelPlaylistItem;
import net.kokkeli.resources.models.ModelTrack;
import net.kokkeli.resources.models.ModelTracks;
import net.kokkeli.server.IFileSystem;
import net.kokkeli.server.ITemplateService;
import net.kokkeli.server.NotAuthenticatedException;
import net.kokkeli.server.RenderException;

@Path("/tracks")
public class TracksResource extends BaseResource {
    private static final String INDEX_TEMPLATE = "tracks/index.ftl";
    private static final String TRACK_DETAILS_TEMPLATE = "tracks/track.ftl";
    private static final String TRACK_EDIT_TEMPLATE = "tracks/edit.ftl";
    
    private static final String FORM_TRACK = "track";
    private static final String FORM_ARTIST = "artist";
    private static final String FORM_LOCATION = "location";
    
    private final ITrackService trackService;
    private final IFileSystem fileSystem;
    private final ModelBuilder<ModelTrack> modelBuilder;
    
    /**
     * Creates tracks resource.
     * @param logger
     */
    @Inject
    protected TracksResource(ILogger logger, ITemplateService templateService,
            IPlayer player, ISessionService sessions, ISettings settings, ITrackService trackService, IFileSystem fileSystem) {
        super(logger, templateService, player, sessions, settings);
        
        this.trackService = trackService;
        this.fileSystem = fileSystem;
        modelBuilder = new ModelBuilder<ModelTrack>(ModelTrack.class);
    }

    @GET
    @Produces("text/html")
    @Access(Role.ADMIN)
    @Path("{id: [0-9]*}")
    public Response trackDetails(@Context HttpServletRequest req, @PathParam("id") long id) throws NotAuthenticatedException{
        BaseModel model = buildBaseModel(req);
        
        try {
            Track track = trackService.get(id);
            
            ModelTrack trackModel = new ModelTrack();
            trackModel.setId(track.getId());
            trackModel.setArtist(track.getArtist());
            trackModel.setLocation(track.getLocation());
            trackModel.setTrack(track.getTrackName());
            trackModel.setExists(track.getExists());
            trackModel.setUploader(track.getUploader().getUserName());
            
            model.setModel(trackModel);
            
            return Response.ok(templates.process(TRACK_DETAILS_TEMPLATE, model)).build();
        } catch (RenderException e) {
            return handleRenderingError(model, e);
        } catch (NotFoundInDatabase e){
            sessions.setError(model.getCurrentSession().getAuthId(), "Track not found.");
            return Response.seeOther(settings.getURI("tracks")).build();
        } catch (ServiceException e) {
            return handleServiceException(model, e);
        }
    }
    
    @GET
    @Produces("text/html")
    @Access(Role.ADMIN)
    public Response verifiedTrack(@Context HttpServletRequest req) throws NotAuthenticatedException{
        BaseModel baseModel = buildBaseModel(req);

        try {
            Collection<Track> tracks = trackService.getAndVerifyTracks();
            
            ModelTracks model = new ModelTracks();
            for (Track track : tracks) {
                ModelPlaylistItem item = new ModelPlaylistItem();
                item.setArtist(track.getArtist());
                item.setTrackName(track.getTrackName());
                if (track.getUploader() != null){
                    item.setUploader(track.getUploader().toString());
                }
                item.setExists(fileSystem.fileExists(track.getLocation()));
                item.setId(track.getId());
                
                model.getItems().add(item);
            }
            
            baseModel.setModel(model);
            
            return Response.ok(templates.process(INDEX_TEMPLATE, baseModel)).build();
        } catch (RenderException e) {
            return handleRenderingError(baseModel, e);
        } catch (ServiceException e) {
            return handleServiceException(baseModel, e);
        }
    }
    
    @GET
    @Path("edit/{id: [0-9]*}")
    @Produces("text/html")
    @Access(Role.ADMIN)
    public Response editTrack(@Context HttpServletRequest req, @PathParam("id") long id) throws NotAuthenticatedException{
        BaseModel baseModel = buildBaseModel(req);
        try {
            Track track = trackService.get(id);
            
            ModelTrack trackModel = new ModelTrack();
            trackModel.setId(track.getId());
            trackModel.setArtist(track.getArtist());
            trackModel.setLocation(track.getLocation());
            trackModel.setTrack(track.getTrackName());
            trackModel.setExists(track.getExists());
            trackModel.setUploader(track.getUploader().getUserName());
            
            baseModel.setModel(trackModel);
            
            
            return Response.ok(templates.process(TRACK_EDIT_TEMPLATE, baseModel)).build();
        } catch (RenderException e) {
            return handleRenderingError(baseModel, e);
        } catch (NotFoundInDatabase e) {
            sessions.setError(baseModel.getCurrentSession().getAuthId(), "Track not found.");
            return Response.seeOther(settings.getURI("tracks")).build();
        } catch (ServiceException e) {
            return handleServiceException(baseModel, e);
        }
    }
    
    @POST
    @Path("edit/{id: [0-9]*}")
    @Produces("text/html")
    @Access(Role.ADMIN)
    public Response editTrack(@Context HttpServletRequest req, @PathParam("id") long id, MultivaluedMap<String, String> formParams) throws NotAuthenticatedException, BadRequestException{
        BaseModel model = buildBaseModel(req);
        
        validateEditForm(formParams);
        
        try {
            Track track = trackService.get(id);
            
            ModelTrack trackModel = modelBuilder.createModelFrom(formParams);
            model.setModel(trackModel);
            trackModel.setUploader(track.getUploader().getUserName());
            
            if (ValidationUtils.isEmpty(trackModel.getArtist()) ||
                ValidationUtils.isEmpty(trackModel.getLocation()) ||
                ValidationUtils.isEmpty(trackModel.getTrack())){
                model.setError("Artist, track name and location are required.");
                return Response.ok(templates.process(TRACK_EDIT_TEMPLATE, model)).build();
            }
            
            
            track.setArtist(trackModel.getArtist());
            track.setLocation(trackModel.getLocation());
            track.setTrackName(trackModel.getTrack());
            
            trackService.update(track);
            
            sessions.setInfo(model.getCurrentSession().getAuthId(), "Track edited.");
            return Response.seeOther(settings.getURI(String.format("tracks/%s", id))).build();
        } catch (RenderException e) {
            return handleRenderingError(model, e);
        } catch (NotFoundInDatabase e) {
            sessions.setError(model.getCurrentSession().getAuthId(), "Track not found.");
            return Response.seeOther(settings.getURI("tracks")).build();
        } catch (ServiceException e) {
            return handleServiceException(model, e);
        }
    }

    /**
     * Checks that form contains correct values
     * @param formParams 
     * @throws BadRequestException
     */
    private static void validateEditForm(MultivaluedMap<String, String> formParams) throws BadRequestException{
        if (!formParams.containsKey(FORM_LOCATION) ||
            !formParams.containsKey(FORM_ARTIST) ||
            !formParams.containsKey(FORM_TRACK)){
            throw new BadRequestException("Form doesnt have needed values.");
        }
    }
}
