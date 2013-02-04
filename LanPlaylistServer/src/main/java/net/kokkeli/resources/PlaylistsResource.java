package net.kokkeli.resources;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.FileAlreadyExistsException;
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
import javax.ws.rs.core.Response;

import com.google.inject.Inject;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;

import net.kokkeli.ISettings;
import net.kokkeli.ValidationUtils;
import net.kokkeli.data.ILogger;
import net.kokkeli.data.Role;
import net.kokkeli.data.Track;
import net.kokkeli.data.db.NotFoundInDatabase;
import net.kokkeli.data.db.PlayList;
import net.kokkeli.data.services.IPlaylistService;
import net.kokkeli.data.services.ISessionService;
import net.kokkeli.data.services.ServiceException;
import net.kokkeli.player.IPlayer;
import net.kokkeli.resources.models.BaseModel;
import net.kokkeli.resources.models.ModelPlaylist;
import net.kokkeli.resources.models.ModelPlaylists;
import net.kokkeli.server.ITemplateService;
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
    
    private final IPlaylistService playlistService;
    private final ISettings settings;
    
    /**
     * Creates resource
     * @param logger
     * @param templateService
     * @param player
     */
    @Inject
    protected PlaylistsResource(ILogger logger, ITemplateService templateService, IPlayer player, ISessionService sessions, ISettings settings, IPlaylistService playlistService) {
        super(logger, templateService, player, sessions);
        
        this.playlistService = playlistService;
        this.settings = settings;
    }

    /**
     * Shows list of users
     * @return HTML-page for user list
     * @throws ServiceException Thrown if there was a problem with service.
     * @throws RenderException Thrown if there is problem with rendering template
     */
    @GET
    @Produces("text/html")
    @Access(Role.ADMIN)
    public Response playlists(@Context HttpServletRequest req) throws ServiceException {
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
    public Response add(@Context HttpServletRequest req, @PathParam("playlistId") long playlistId) throws ServiceException {
        BaseModel model = buildBaseModel(req);
        
        try {
            return Response.ok(templates.process(PLAYLIST_TRACK_ADD_TEMPLATE, model)).build();
        } catch (RenderException e) {
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
            @FormDataParam("file") FormDataContentDisposition fileDetail) throws ServiceException, NotFoundInDatabase {
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
                log("User tried to upload with no file.", 1);
                model.setError("Select a file to upload.");
                return Response.ok(templates.process(PLAYLIST_TRACK_ADD_TEMPLATE, model)).build();
            }
            
            log("User trying to upload file: " + fileDetail.getFileName() + ", Filetype: " + fileDetail.getType(), 1);
            String filename = settings.getTracksFolder() + "/" + fileDetail.getFileName();
            
            if (fileExists(filename)){
                model.setError("Similar file already exists. Remove existing file, or upload different.");
                log("User tried to upload file with same name with file already in system. File: " + filename, 1);
                return Response.ok(templates.process(PLAYLIST_TRACK_ADD_TEMPLATE, model)).build();
            }
            
            writeToFile(uploadedInputStream, filename);
            log("Uploading succeeded.", 1);

            //TODO Validate that file is audio
            //TODO Check that disk has space

            PlayList playlist = playlistService.getPlaylist(playlistId);
            Track item = new Track();
            
            item.setArtist(artist);
            item.setTrackName(track);
            item.setLocation(filename);

            playlist.getItems().add(item);
            playlistService.update(playlist);
            
            return Response.ok(templates.process(PLAYLIST_TRACK_ADD_TEMPLATE, model)).build();
        } catch (RenderException e) {
            log("There was a problem with rendering:" + e.getMessage(), 5);
            throw new ServiceException("There was a problem with rendering.", e);
        } catch (IOException e) {
            log("There was a problem with IO:" + e.getMessage(), 5);
            throw new ServiceException("There was a problem with file uploading.", e);
        }
    }
    
    /**
     * Writes uploaded file to disk.
     * @param uploadedInputStream Inputstream
     * @param uploadedFileLocation Location of file
     * @throws IOException thrown if there is problem with uploading.
     */
    private static void writeToFile(InputStream uploadedInputStream, String uploadedFileLocation) throws IOException {
        int read = 0;
        byte[] bytes = new byte[1024];
 
        File file = new File(uploadedFileLocation);
        if (file.exists()){
            throw new FileAlreadyExistsException("File already exists.");
        }
        
        OutputStream out = new FileOutputStream(file);
        while ((read = uploadedInputStream.read(bytes)) != -1) {
            out.write(bytes, 0, read);
        }
        out.flush();
        out.close();
    }
    
    /**
     * Checks if file exists.
     * @param file File name
     * @return True, if file exists.
     */
    private static boolean fileExists(String file){
        return new File(file).exists();
    }
}
