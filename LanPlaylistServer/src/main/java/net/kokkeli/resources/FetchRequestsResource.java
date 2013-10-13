package net.kokkeli.resources;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import com.google.inject.Inject;

import net.kokkeli.ISettings;
import net.kokkeli.data.FetchRequest;
import net.kokkeli.data.FetchStatus;
import net.kokkeli.data.ILogger;
import net.kokkeli.data.PlayList;
import net.kokkeli.data.Role;
import net.kokkeli.data.Track;
import net.kokkeli.data.services.IFetchRequestService;
import net.kokkeli.data.services.IPlaylistService;
import net.kokkeli.data.services.ISessionService;
import net.kokkeli.data.services.ServiceException;
import net.kokkeli.player.IPlayer;
import net.kokkeli.resources.models.BaseModel;
import net.kokkeli.resources.models.ModelFetchRequest;
import net.kokkeli.resources.models.ModelFetchRequestCreate;
import net.kokkeli.resources.models.ModelFetchRequests;
import net.kokkeli.resources.models.ModelPlaylistListItem;
import net.kokkeli.resources.models.ViewModel;
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
    private static final String REQUEST_CREATE_TEMPLATE = "fetchers/createRequest.ftl";
    private static final String FORM_HANDLER = "handler";
    private static final String FORM_DESTINATION = "destination";
    private static final String FORM_LOCATION = "location";
    private static final String FORM_ARTIST = "artist";
    private static final String FORM_TRACK = "trackname";
    private static final String FORM_SELECTED_PLAYLIST = "playlist";
    
    private final IFetchRequestService fetchRequestService;
    private final IPlaylistService playlistService;
    
    /**
     * Initializes new resource for fetch request related resources
     * @param logger Logger
     * @param templateService Template service
     * @param player Player
     * @param sessions Sessions
     * @param settings Settings
     * @param playlistService Playlist service
     * @param fetchRequestService Fetch request service
     */
    @Inject
    protected FetchRequestsResource(ILogger logger,
            ITemplateService templateService, IPlayer player,
            ISessionService sessions, ISettings settings,
            IFetchRequestService fetchRequestService, IPlaylistService playlistService) {
        super(logger, templateService, player, sessions, settings);
        
        this.fetchRequestService = fetchRequestService;
        this.playlistService = playlistService;
    }

    @GET
    @Produces("text/html")
    @Access(Role.USER)
    public Response index(@Context HttpServletRequest req) throws NotAuthenticatedException{
        BaseModel model = buildBaseModel(req);
        try {
            model.setModel(createRequestsModel());

            return Response.ok(templates.process(INDEX_TEMPLATE, model)).build();
        } catch (RenderException e) {
            return handleRenderingError(model, e);
        } catch (ServiceException e) {
            return handleServiceException(model, e);
        }
    }
    
    @GET
    @Produces("text/html")
    @Access(Role.ADMIN)
    @Path("/createRequest")
    public Response createRequest(@Context HttpServletRequest req) throws NotAuthenticatedException {
        BaseModel model = buildBaseModel(req);
        try {
            Collection<PlayList> playlists = playlistService.getIdNames();
            ModelFetchRequestCreate createModel = new ModelFetchRequestCreate();
            
            for (PlayList playList : playlists) {
                ModelPlaylistListItem modelItem = new ModelPlaylistListItem(playList.getId());
                modelItem.setName(playList.getName());
                createModel.getItems().add(modelItem);
            }
            model.setModel(createModel);
            
            return Response.ok(templates.process(REQUEST_CREATE_TEMPLATE, model)).build();
        } catch (RenderException e) {
            return handleRenderingError(model, e);
        } catch (ServiceException e) {
            return handleServiceException(model, e);
        }
    }
    
    @POST
    @Produces("text/html")
    @Access(Role.ADMIN)
    @Path("/createRequest")
    public Response createRequest(@Context HttpServletRequest req, MultivaluedMap<String, String> formParams) throws NotAuthenticatedException {
        BaseModel model = buildBaseModel(req);
        
        try {
            ModelFetchRequestCreate createModel = createModel(formParams);
            
            FetchRequest newRequest = new FetchRequest();
            newRequest.setDestinationFile(createModel.getDestination());
            newRequest.setHandler(createModel.getHandler());
            newRequest.setLocation(createModel.getLocation());
            newRequest.setStatus(FetchStatus.WAITING);
            PlayList playlist = new PlayList(createModel.getSelectedPlaylistId());
            newRequest.setPlaylist(playlist);
            Track track = new Track();
            track.setArtist(createModel.getArtist());
            track.setTrackName(createModel.getTrack());
            track.setUploader(model.getCurrentSession().getUser());
            track.setLocation(createModel.getLocation());
            newRequest.setTrack(track);
            
            fetchRequestService.add(newRequest);
            
            sessions.setInfo(model.getCurrentSession().getAuthId(), "Fetch request created.");
            return Response.seeOther(settings.getURI("fetchers")).build();
        } catch (ServiceException e) {
            return handleServiceException(model, e);
        }


    }
    
    /**
     * Craete model fetch request from form.
     * @param formParams Form Params
     * @return Created model
     */
    private ModelFetchRequestCreate createModel(MultivaluedMap<String, String> formParams) {
        ModelFetchRequestCreate model = new ModelFetchRequestCreate();
        model.setHandler(formParams.getFirst(FORM_HANDLER).trim());
        model.setDestination(formParams.getFirst(FORM_DESTINATION).trim());
        model.setLocation(formParams.getFirst(FORM_LOCATION).trim());
        model.setArtist(formParams.getFirst(FORM_ARTIST).trim());
        model.setTrack(formParams.getFirst(FORM_TRACK).trim());
        model.setSelectedPlaylistId(Long.parseLong(formParams.getFirst(FORM_SELECTED_PLAYLIST)));
        
        return model;
    }

    /**
     * Creates list model from fetch requests
     * @return Fetch request list model
     * @throws ServiceException Thrown if service fails
     */
    private ViewModel createRequestsModel() throws ServiceException{
        Collection<FetchRequest> requests = fetchRequestService.get();
        
        ModelFetchRequests model = new ModelFetchRequests();
        for (FetchRequest fetchRequest : requests) {
            ModelFetchRequest modelRequest = new ModelFetchRequest();
            
            modelRequest.setLocation(fetchRequest.getLocation());
            modelRequest.setHandler(fetchRequest.getHandler());
            modelRequest.setStatus(fetchRequest.getStatus());
            modelRequest.setDestination(fetchRequest.getDestinationFile());
            modelRequest.setTrack(fetchRequest.getTrack());

            model.getItems().add(modelRequest);
        }
        
        return model;
    }
}
