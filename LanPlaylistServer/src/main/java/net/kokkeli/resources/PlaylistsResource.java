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

import com.google.inject.Inject;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;

import net.kokkeli.ISettings;
import net.kokkeli.ValidationUtils;
import net.kokkeli.data.ILogger;
import net.kokkeli.data.LogSeverity;
import net.kokkeli.data.PlayList;
import net.kokkeli.data.Role;
import net.kokkeli.data.Track;
import net.kokkeli.data.db.NotFoundInDatabase;
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
 * @author Hekku2
 *
 */
@Path("/playlists")
public class PlaylistsResource extends BaseResource {
    private static final String PLAYLISTS_TEMPLATE = "playlist/playlists.ftl";
    private static final String PLAYLIST_TRACK_ADD_TEMPLATE ="playlist/add.ftl";
    private static final String PLAYLIST_DETAILS_TEMPLATE = "playlist/details.ftl";
    private static final String PLAYLIST_CREATE_TEMPLATE = "playlist/create.ftl";
    
    private static final String FORM_NAME = "name";
    
    private final IPlaylistService playlistService;
    private final ISettings settings;
    private final IFileSystem filesystem;
    /**
     * Creates resource
     * @param logger
     * @param templateService
     * @param player
     */
    @Inject
    protected PlaylistsResource(ILogger logger,
            ITemplateService templateService,
            IPlayer player,
            ISessionService sessions,
            ISettings settings,
            IPlaylistService playlistService,
            IFileSystem filesystem) {
        super(logger, templateService, player, sessions, settings);
        
        this.playlistService = playlistService;
        this.settings = settings;
        this.filesystem = filesystem;
    }

    /**
     * Shows list of users
     * @return HTML-page for user list
     * @throws ServiceException Thrown if there was a problem with service.
     * @throws NotAuthenticatedException Thrown if there is problem with session.
     * @throws RenderException Thrown if there is problem with rendering template
     */
    @GET
    @Produces("text/html")
    @Access(Role.ADMIN)
    public Response playlists(@Context HttpServletRequest req) throws ServiceException, NotAuthenticatedException {
        BaseModel model = buildBaseModel(req);

        Collection<PlayList> lists = playlistService.getIdNames();
        
        ModelPlaylists playlists = new ModelPlaylists();
        
        for (PlayList entry : lists) {
            ModelPlaylist item = new ModelPlaylist(entry.getId());
            item.setName(entry.getName());
            
            playlists.getItems().add(item);
        }
        
        model.setModel(playlists);
        
        try {
            return Response.ok(templates.process(PLAYLISTS_TEMPLATE, model)).build();
        } catch (RenderException e) {
            throw new ServiceException("There was a problem with rendering.");
        }
    }
    
    @GET
    @Produces("text/html")
    @Access(Role.USER)
    @Path("/add/{playlistId: [0-9]*}")
    public Response add(@Context HttpServletRequest req, @PathParam("playlistId") long playlistId) throws ServiceException, NotAuthenticatedException {
        BaseModel model = buildBaseModel(req);
        
        try {
            return Response.ok(templates.process(PLAYLIST_TRACK_ADD_TEMPLATE, model)).build();
        } catch (RenderException e) {
            log("There was a problem with rendering:" + e.getMessage(), LogSeverity.ERROR);
            throw new ServiceException("There was a problem with rendering.");
        }
    }
    
    /**
     * POST for add. Adds track to playlist.
     * @param req Request
     * @param playlistId Target playlist
     * @param artist Artist name
     * @param track Track name
     * @param uploadedInputStream Inputstream containing the fie
     * @param fileDetail Filedetails
     * @return Redirect to playlist detais
     * @throws ServiceException Thrown if there is  problem with service
     * @throws NotFoundInDatabase Thrown if there is no such playlist.
     * @throws NotAuthenticatedException Thrown if there is problem with session.
     */
    @POST
    @Produces("text/html")
    @Access(Role.USER)
    @Path("/add/{playlistId: [0-9]*}")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response add(@Context HttpServletRequest req,
            @PathParam("playlistId") long playlistId,
            @FormDataParam("artist") String artist,
            @FormDataParam("track") String track,
            @FormDataParam("file") InputStream uploadedInputStream,
            @FormDataParam("file") FormDataContentDisposition fileDetail) throws ServiceException, NotFoundInDatabase, NotAuthenticatedException {
        BaseModel model = buildBaseModel(req);
        
        try {
            if (ValidationUtils.isEmpty(track) || ValidationUtils.isEmpty(artist)){
                model.setError("Track must have name and artist.");
                return Response.ok(templates.process(PLAYLIST_TRACK_ADD_TEMPLATE, model)).build();
            }
            
            if (!ValidationUtils.containsOnlyNumbersAndLettersAndWhiteSpace(track)
             || !ValidationUtils.containsOnlyNumbersAndLettersAndWhiteSpace(artist)){
                model.setError("Track or artist contained invalid charachters.");
                return Response.ok(templates.process(PLAYLIST_TRACK_ADD_TEMPLATE, model)).build();
            }
            
            if (ValidationUtils.isEmpty(fileDetail.getFileName())){
                log("User tried to upload with no file.", LogSeverity.TRACE);
                model.setError("Select a file to upload.");
                return Response.ok(templates.process(PLAYLIST_TRACK_ADD_TEMPLATE, model)).build();
            }
            
            log("User trying to upload file: " + fileDetail.getFileName() + ", Filetype: " + fileDetail.getType(), LogSeverity.TRACE);
            String filename = settings.getTracksFolder() + "/" + fileDetail.getFileName();
            
            if (filesystem.fileExists(filename)){
                model.setError("Similar file already exists. Remove existing file, or upload different.");
                log("User tried to upload file with same name with file already in system. File: " + filename, LogSeverity.TRACE);
                return Response.ok(templates.process(PLAYLIST_TRACK_ADD_TEMPLATE, model)).build();
            }
            
            filesystem.writeToFile(uploadedInputStream, filename);
            log("Uploading succeeded.", LogSeverity.TRACE);

            //TODO Validate that file is audio
            //TODO Check that disk has space

            PlayList playlist = playlistService.getPlaylist(playlistId);
            Track item = new Track();
            
            item.setArtist(artist);
            item.setTrackName(track);
            item.setLocation(filename);
            item.setUploader(model.getCurrentSession().getUser());
            
            playlist.getItems().add(item);
            playlistService.update(playlist);
            
            return Response.ok(templates.process(PLAYLIST_TRACK_ADD_TEMPLATE, model)).build();
        } catch (RenderException e) {
            log("There was a problem with rendering:" + e.getMessage(), LogSeverity.ERROR);
            throw new ServiceException("There was a problem with rendering.", e);
        } catch (IOException e) {
            log("There was a problem with IO:" + e.getMessage(), LogSeverity.ERROR);
            throw new ServiceException("There was a problem with file uploading.", e);
        }
    }
    
    @GET
    @Produces("text/html")
    @Access(Role.USER)
    @Path("/{playlistId: [0-9]*}")
    public Response details(@Context HttpServletRequest req, @PathParam("playlistId") long playlistId) throws ServiceException, NotAuthenticatedException {
        BaseModel baseModel = buildBaseModel(req);

        try {
            PlayList playlist = playlistService.getPlaylist(playlistId);
            ModelPlaylist modelPlayList = new ModelPlaylist(playlist.getId());
            modelPlayList.setName(playlist.getName());
            
            for (Track playListItem : playlist.getItems()) {
                ModelPlaylistItem model = new ModelPlaylistItem();
                model.setArtist(playListItem.getArtist());
                model.setTrackName(playListItem.getTrackName());
                model.setUploader(playListItem.getUploader().getUserName());
                model.setId(playListItem.getId());
                
                modelPlayList.getItems().add(model);
            }
            baseModel.setModel(modelPlayList);
            return Response.ok(templates.process(PLAYLIST_DETAILS_TEMPLATE, baseModel)).build();
        } catch (RenderException e) {
            log("There was a problem with rendering:" + e.getMessage(), LogSeverity.ERROR);
            throw new ServiceException("There was a problem with rendering.");
        } catch (NotFoundInDatabase e) {
            sessions.setError(baseModel.getCurrentSession().getAuthId(), "Playlist not found.");
            return Response.seeOther(settings.getURI("playlists")).build();
        }
    }

    @GET
    @Produces("text/html")
    @Access(Role.USER)
    @Path("/create")
    public Response create(@Context HttpServletRequest req) throws NotAuthenticatedException, ServiceException{
    	BaseModel baseModel = buildBaseModel(req);
    	
    	try {
			return Response.ok(templates.process(PLAYLIST_CREATE_TEMPLATE, baseModel)).build();
		} catch (RenderException e) {
			log("There was a problem with rendering:" + e.getMessage(), LogSeverity.ERROR);
			throw new ServiceException("There was a problem with rendering.");
		}
    }
    
    @POST
    @Produces("text/html")
    @Access(Role.USER)
    @Path("/create")
    public Response create(@Context HttpServletRequest req, MultivaluedMap<String, String> formParams) throws NotAuthenticatedException, ServiceException, BadRequestException{
    	BaseModel baseModel = buildBaseModel(req);
    	
    	//TODO Check validity of name
    	
    	try {
        	ModelPlaylist item = createPlaylist(formParams);
        	
        	PlayList playlist = new PlayList(item.getId());
        	playlist.setName(item.getName());
        	
        	playlistService.add(playlist);
    		
    		return Response.seeOther(settings.getURI(String.format("playlists/%s", playlist.getId()))).build();
		} catch (ServiceException e) {
			log("There was a problem with the service:" + e.getMessage(), LogSeverity.ERROR);
			return handleServiceException(baseModel);
		}
    }
    
    /**
     * Creates new playlist for create from formParams. If values are missing, exception is thrown.
     * @param formParams Parameters. Contains all needed field for ModelUser
     * @return Created ModelUser
     * @throws BadRequestException Thrown formParams contain illegal input.
     */
    private ModelPlaylist createPlaylist(MultivaluedMap<String, String> formParams) throws BadRequestException{       
        String name = formParams.getFirst(FORM_NAME).trim();
        
        ModelPlaylist playlist = new ModelPlaylist(-1);
        playlist.setName(name);     
        
        return playlist;
    }
}
