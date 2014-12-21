package net.kokkeli.resources;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.google.inject.Inject;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;

import net.kokkeli.ISettings;
import net.kokkeli.ModelBuilder;
import net.kokkeli.ValidationUtils;
import net.kokkeli.data.FetchRequest;
import net.kokkeli.data.FetchStatus;
import net.kokkeli.data.ILogger;
import net.kokkeli.data.LogSeverity;
import net.kokkeli.data.PlayList;
import net.kokkeli.data.Role;
import net.kokkeli.data.Session;
import net.kokkeli.data.Track;
import net.kokkeli.data.db.NotFoundInDatabase;
import net.kokkeli.data.services.IFetchRequestService;
import net.kokkeli.data.services.IPlaylistService;
import net.kokkeli.data.services.ISessionService;
import net.kokkeli.data.services.ServiceException;
import net.kokkeli.player.IPlayer;
import net.kokkeli.resources.models.BaseModel;
import net.kokkeli.resources.models.ModelPlaylist;
import net.kokkeli.resources.models.ModelPlaylistItem;
import net.kokkeli.resources.models.ModelPlaylists;
import net.kokkeli.server.IFileSystem;
import net.kokkeli.server.ITemplateService;
import net.kokkeli.server.NotAuthenticatedException;
import net.kokkeli.server.RenderException;

/**
 * Resources for playlist management
 * 
 * @author Hekku2
 * 
 */
@Path("/playlists")
public class PlaylistsResource extends BaseResource {
    private static final String PLAYLISTS_TEMPLATE = "playlist/playlists.ftl";
    private static final String PLAYLIST_TRACK_ADD_TEMPLATE = "playlist/add.ftl";
    private static final String PLAYLIST_DETAILS_TEMPLATE = "playlist/details.ftl";
    private static final String PLAYLIST_CREATE_TEMPLATE = "playlist/create.ftl";
    private static final String PLAYLIST_TRACK_ADD_VLC_TEMPLATE = "playlist/addVlc.ftl";

    private static final String FORM_NAME = "name";
    private static final String FORM_ID = "id";

    private final IPlaylistService playlistService;
    private final ISettings settings;
    private final IFileSystem filesystem;
    private final IFetchRequestService fetchRequestService;
    private final ModelBuilder<ModelPlaylistItem> modelBuilder;

    /**
     * Creates resource
     * 
     * @param logger
     * @param templateService
     * @param player
     * @param fetchRequestService
     */
    @Inject
    protected PlaylistsResource(ILogger logger, ITemplateService templateService, IPlayer player,
            ISessionService sessions, ISettings settings, IPlaylistService playlistService, IFileSystem filesystem, IFetchRequestService fetchRequestService) {
        super(logger, templateService, player, sessions, settings);

        this.playlistService = playlistService;
        this.settings = settings;
        this.filesystem = filesystem;
        this.fetchRequestService = fetchRequestService;
        modelBuilder = new ModelBuilder<ModelPlaylistItem>(ModelPlaylistItem.class);
    }

    /**
     * Shows list of playlists
     * 
     * @return HTML-page for playlists
     * @throws NotAuthenticatedException
     *             Thrown if there is problem with session.
     */
    @GET
    @Produces("text/html; charset=utf-8")
    @Access(Role.ADMIN)
    public Response playlists(@Context HttpServletRequest req) throws NotAuthenticatedException {
        BaseModel model = buildBaseModel(req);
        try {
            Collection<PlayList> lists = playlistService.getIdNames();

            ModelPlaylists playlists = new ModelPlaylists();

            for (PlayList entry : lists) {
                ModelPlaylist item = new ModelPlaylist(entry.getId());
                item.setName(entry.getName());

                playlists.getItems().add(item);
            }

            model.setModel(playlists);

            return Response.ok(templates.process(PLAYLISTS_TEMPLATE, model)).build();
        } catch (RenderException e) {
            return handleRenderingError(model, e);
        } catch (ServiceException e) {
            return handleServiceException(model, e);
        }
    }

    /**
     * Playlist add upload get.
     * 
     * @param req
     *            Request
     * @param playlistId
     *            Playlist id
     * @return Response
     * @throws NotAuthenticatedException
     *             Thrown if user is not authenticated
     */
    @GET
    @Produces("text/html; charset=utf-8")
    @Access(Role.ANYNOMOUS)
    @Path("/add/upload/{playlistId: [0-9]*}")
    public Response addUpload(@Context HttpServletRequest req, @PathParam("playlistId") long playlistId)
            throws NotAuthenticatedException {
        BaseModel model = buildBaseModel(req);

        ModelPlaylistItem item = new ModelPlaylistItem();
        item.setPlaylistId(playlistId);
        model.setModel(item);

        try {
            return Response.ok(templates.process(PLAYLIST_TRACK_ADD_TEMPLATE, model)).build();
        } catch (RenderException e) {
            return handleRenderingError(model, e);
        }
    }

    /**
     * Playlist vlc add get.
     * 
     * @param req
     *            Request
     * @param playlistId
     *            Playlist id
     * @return Response
     * @throws NotAuthenticatedException
     *             Thrown if user is not authenticated
     */
    @GET
    @Produces("text/html; charset=utf-8")
    @Access(Role.ANYNOMOUS)
    @Path("/add/vlc/{playlistId: [0-9]*}")
    public Response addVlc(@Context HttpServletRequest req, @PathParam("playlistId") long playlistId)
            throws NotAuthenticatedException {
        BaseModel model = buildBaseModel(req);

        ModelPlaylistItem item = new ModelPlaylistItem();
        item.setPlaylistId(playlistId);
        model.setModel(item);

        try {
            return Response.ok(templates.process(PLAYLIST_TRACK_ADD_VLC_TEMPLATE, model)).build();
        } catch (RenderException e) {
            return handleRenderingError(model, e);
        }
    }

    /**
     * POST for add. Adds track to playlist.
     * 
     * @param req
     *            Request
     * @param playlistId
     *            Target playlist
     * @param artist
     *            Artist name
     * @param track
     *            Track name
     * @param uploadedInputStream
     *            Inputstream containing the fie
     * @param fileDetail
     *            Filedetails
     * @return Redirect to playlist detais
     * @throws ServiceException
     *             Thrown if there is problem with service
     * @throws NotFoundInDatabase
     *             Thrown if there is no such playlist.
     * @throws NotAuthenticatedException
     *             Thrown if there is problem with session.
     */
    @POST
    @Produces("text/html; charset=utf-8")
    @Access(Role.ANYNOMOUS)
    @Path("/add/upload/{playlistId: [0-9]*}")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response addUpload(@Context HttpServletRequest req, @PathParam("playlistId") long playlistId,
            @FormDataParam("artist") String artist, @FormDataParam("track") String track,
            @FormDataParam("file") InputStream uploadedInputStream,
            @FormDataParam("file") FormDataContentDisposition fileDetail) throws ServiceException, NotFoundInDatabase,
            NotAuthenticatedException {
        BaseModel model = buildBaseModel(req);
        ModelPlaylistItem createModel = new ModelPlaylistItem();
        createModel.setPlaylistId(playlistId);
        createModel.setArtist(artist);
        createModel.setTrack(track);
        model.setModel(createModel);
        try {
            //TODO Check if this method can use model builder and refactor if possible.
            
            String validationError = getValidationError(createModel);
            if (validationError != null){
                return Response.status(Status.BAD_REQUEST).entity(validationError).build();
            }
            
            if (ValidationUtils.isEmpty(fileDetail.getFileName())) {
                log("User tried to upload with no file.", LogSeverity.TRACE);
                return Response.status(Status.BAD_REQUEST).entity("Select a file to upload.").build();
            }

            String converted = new String(fileDetail.getFileName().getBytes("iso-8859-1"), "UTF-8");
            log("User trying to upload file: " + converted + ", Filetype: " + fileDetail.getType(), LogSeverity.TRACE);
            String filename = settings.getTracksFolder() + "/" + converted;

            if (filesystem.fileExists(filename)) {
                log("User tried to upload file with same name with file already in system. File: " + filename, LogSeverity.TRACE);
                return Response.status(Status.BAD_REQUEST).entity("Similar file already exists. Remove existing file, or upload different.").build();
            }

            filesystem.writeToFile(uploadedInputStream, filename);
            log("Uploading succeeded.", LogSeverity.TRACE);

            // TODO Validate that file is audio
            // TODO Check that disk has space
            // TODO Check that file is not too big.
            
            PlayList playlist = playlistService.getPlaylist(playlistId);
            Track item = new Track();
            item.setArtist(createModel.getArtist());
            item.setTrackName(createModel.getTrack());
            item.setLocation(filename);
            if (model.getCurrentSession() != null)
                item.setUploader(model.getCurrentSession().getUser());

            playlist.getItems().add(item);
            playlistService.update(playlist);
            return Response.ok().entity("Upload successful.").build();
        }  catch (IOException e) {
            log("There was a problem with IO:" + e.getMessage(), LogSeverity.ERROR);
            throw new ServiceException("There was a problem with file uploading.", e);
        }
    }
    
    @POST
    @Produces("text/html; charset=utf-8")
    @Access(Role.ANYNOMOUS)
    @Path("/add/vlc/{playlistid: [0-9]*}")
    public Response addVlc(@Context HttpServletRequest req,  MultivaluedMap<String, String> formParams) throws ServiceException {
        BaseModel model = buildBaseModel(req);

        ModelPlaylistItem item = modelBuilder.createModelFrom(formParams);
        model.setModel(item);
        
        String validationError = getValidationError(item);
        if (validationError != null){
            return Response.status(Status.BAD_REQUEST).entity(validationError).build();
        }

        FetchRequest newRequest = createFetchRequestFromModelPlaylistItem(item, model.getCurrentSession());
        fetchRequestService.add(newRequest);
        
        return Response.ok().entity("Upload successful.").build();
    }

    @GET
    @Produces("text/html; charset=utf-8")
    @Access(Role.ANYNOMOUS)
    @Path("/{playlistId: [0-9]*}")
    public Response details(@Context HttpServletRequest req, @PathParam("playlistId") long playlistId)
            throws ServiceException {
        BaseModel baseModel = buildBaseModel(req);

        try {
            baseModel.setModel(createPlaylistDetailsModel(playlistId));
            return Response.ok(templates.process(PLAYLIST_DETAILS_TEMPLATE, baseModel)).build();
        } catch (RenderException e) {
            return handleRenderingError(baseModel, e);
        } catch (NotFoundInDatabase e) {
            sessions.setError(baseModel.getCurrentSession().getAuthId(), "Playlist not found.");
            return Response.seeOther(settings.getURI("playlists")).build();
        }
    }

    @GET
    @Produces("text/html")
    @Access(Role.ADMIN)
    @Path("/create")
    public Response create(@Context HttpServletRequest req) {
        BaseModel baseModel = buildBaseModel(req);

        try {
            return Response.ok(templates.process(PLAYLIST_CREATE_TEMPLATE, baseModel)).build();
        } catch (RenderException e) {
            return handleRenderingError(baseModel, e);
        }
    }

    @POST
    @Produces("text/html; charset=utf-8")
    @Access(Role.ADMIN)
    @Path("/create")
    public Response create(@Context HttpServletRequest req, MultivaluedMap<String, String> formParams) throws BadRequestException {
        BaseModel baseModel = buildBaseModel(req);

        try {
            ModelPlaylist item = createPlaylist(formParams);
            baseModel.setModel(item);
            
            String validationError = getValidationError(item);
            if (validationError != null){
                baseModel.setError(validationError);
                return Response.ok(templates.process(PLAYLIST_CREATE_TEMPLATE, baseModel)).build();
            }

            PlayList playlist = new PlayList(item.getId());
            playlist.setName(item.getName());

            playlistService.add(playlist);

            return Response.seeOther(settings.getURI(String.format("playlists/%s", playlist.getId()))).build();
        } catch (ServiceException e) {
            return handleServiceException(baseModel, e);
        } catch (RenderException e) {
            return handleRenderingError(baseModel, e);
        }
    }

    @POST
    @Produces("text/html; charset=utf-8")
    @Access(Role.ADMIN)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Path("/removeTrackFromPlaylist/{playlistId: [0-9]*}")
    public Response removeTrackFromPlaylist(@Context HttpServletRequest req, @PathParam("playlistId") long playlistId,
            MultivaluedMap<String, String> formParams) throws BadRequestException {
        try {
            long trackId = Long.parseLong(formParams.getFirst(FORM_ID));

            PlayList list = playlistService.getPlaylist(playlistId);
            for (Track track : list.getItems()) {
                if (track.getId() == trackId) {
                    list.getItems().remove(track);
                    playlistService.update(list);
                    log(String.format("Removed track #%s from playlist #%s.", trackId, playlistId), LogSeverity.TRACE);
                    return Response.ok().build();
                }
            }

            // If track is not found in playlist, return 404
            return Response.status(Status.NOT_FOUND).build();
        } catch (NumberFormatException e) {
            throw new BadRequestException("Id was not in correct format.", e);
        } catch (ServiceException e) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        } catch (NotFoundInDatabase e) {
            return Response.status(Status.NOT_FOUND).build();
        }

    }

    /**
     * Creates details model from playlist. Model contains tracks.
     * 
     * @param playlistId
     *            Id of playlist
     * @return ModelPlaylist
     * @throws ServiceException
     *             Thrown if there is something wrong with the service
     * @throws NotFoundInDatabase
     *             Thrown if item is not found in database
     */
    private ModelPlaylist createPlaylistDetailsModel(long playlistId) throws ServiceException, NotFoundInDatabase {
        PlayList playlist = playlistService.getPlaylist(playlistId);
        ModelPlaylist modelPlayList = new ModelPlaylist(playlist.getId());
        modelPlayList.setName(playlist.getName());

        for (Track playListItem : playlist.getItems()) {
            ModelPlaylistItem model = new ModelPlaylistItem();
            model.setArtist(playListItem.getArtist());
            model.setTrack(playListItem.getTrackName());
            model.setUploader(playListItem.getUploader().getUserName());
            model.setId(playListItem.getId());

            modelPlayList.getItems().add(model);
        }
        return modelPlayList;
    }

    /**
     * Creates new playlist for create from formParams. If values are missing,
     * exception is thrown.
     * 
     * @param formParams
     *            Parameters. Contains all needed field for ModelUser
     * @return Created ModelUser
     * @throws BadRequestException
     *             Thrown formParams contain illegal input.
     */
    private static ModelPlaylist createPlaylist(MultivaluedMap<String, String> formParams) throws BadRequestException {
        String name = formParams.getFirst(FORM_NAME);

        if (name == null) {
            throw new BadRequestException("Form did not contain name.");
        }

        ModelPlaylist playlist = new ModelPlaylist(-1);
        playlist.setName(name.trim());

        return playlist;
    }
    
    /**
     * Returns error message if model doesn't contain valid data, otherwise null is returned.
     * @param model
     * @return Validation error, or null if model is valid.
     */
    private static String getValidationError(ModelPlaylistItem model){
        if (ValidationUtils.isEmpty(model.getTrack()) || ValidationUtils.isEmpty(model.getArtist())) {
            return "Track must have name and artist.";
        }

        if (!ValidationUtils.isValidInput(model.getTrack()) || !ValidationUtils.isValidInput(model.getArtist())) {
            return "Track or artist contained invalid charachters.";
        }
        return null;
    }
    
    private String getValidationError(ModelPlaylist item) throws ServiceException {
        if (ValidationUtils.isEmpty(item.getName())) {
            return "Playlist did not have a name.";
        }

        if (playlistService.nameExists(item.getName())) {
            return String.format("Playlist with name %s already exits.", item.getName());
        }
        
        return null;
    }
    
    /**
     * Creates a new fetch request from model playlist item
     * @param uploader Uploading user
     * @param item model playlist item
     * @return Created fetch request
     */
    private FetchRequest createFetchRequestFromModelPlaylistItem(ModelPlaylistItem item, Session uploader) {
        //TODO Proper generation for destination file and extension.
        String destination = settings.getTracksFolder() + "/" + item.getArtist() + " - " + item.getTrack() + ".ogg";
        
        Track newTrack = new Track();
        newTrack.setArtist(item.getArtist());
        newTrack.setTrackName(item.getTrack());
        if (uploader != null)
            newTrack.setUploader(uploader.getUser());
        newTrack.setLocation(destination);
        
        FetchRequest newRequest = new FetchRequest();
        newRequest.setDestinationFile(destination);
        newRequest.setHandler("vlc");
        newRequest.setLocation(item.getUrl());
        newRequest.setStatus(FetchStatus.WAITING);
        newRequest.setPlaylist(new PlayList(item.getPlaylistId()));
        newRequest.setTrack(newTrack);
        return newRequest;
    }
}
