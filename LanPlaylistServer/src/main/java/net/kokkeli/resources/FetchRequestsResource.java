package net.kokkeli.resources;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import com.google.inject.Inject;

import net.kokkeli.ISettings;
import net.kokkeli.data.FetchRequest;
import net.kokkeli.data.ILogger;
import net.kokkeli.data.Role;
import net.kokkeli.data.services.IFetchRequestService;
import net.kokkeli.data.services.ISessionService;
import net.kokkeli.data.services.ServiceException;
import net.kokkeli.player.IPlayer;
import net.kokkeli.resources.models.BaseModel;
import net.kokkeli.resources.models.ModelFetchRequest;
import net.kokkeli.resources.models.ModelFetchRequests;
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
    
    private final IFetchRequestService fetchRequestService;
    
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
            ISessionService sessions, ISettings settings,
            IFetchRequestService fetchRequestService) {
        super(logger, templateService, player, sessions, settings);
        
        this.fetchRequestService = fetchRequestService;
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
