package net.kokkeli.player;

import javax.ws.rs.core.MediaType;

import org.codehaus.jackson.jaxrs.JacksonJsonProvider;

import com.google.inject.Inject;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.representation.Form;

import net.kokkeli.data.ILogger;
import net.kokkeli.data.LogSeverity;
import net.kokkeli.data.services.ServiceException;

public class PlayerCommunicator implements IPlayer {
    private final String baseUri = "http://localhost:9000/player/";
    
    private final ILogger logger;
    private final Client client;
    
    @Inject
    public PlayerCommunicator(ILogger logger){
        this.logger = logger;
        this.client = buildClient();
    }
    
    @Override
    public void play() throws ServiceException {
        logger.log("Play command received, sending...", LogSeverity.TRACE);
        WebResource resource = client.resource(baseUri + "play");
        
        ClientResponse response = resource.type(MediaType.APPLICATION_FORM_URLENCODED_TYPE).accept(MediaType.TEXT_HTML).post(ClientResponse.class);
        if (response.getStatus() != 200)
            throw new ServiceException("Response status was not succesful. It was " + response.getStatus());
    }

    @Override
    public void selectPlaylist(long id) throws ServiceException {
        try {
            logger.log("Select playlist command received, sending...", LogSeverity.TRACE);
            Form form = new Form();
            form.add("id", id);
            WebResource resource = client.resource(baseUri + "selectPlaylist");
            ClientResponse response = resource.type(MediaType.APPLICATION_FORM_URLENCODED_TYPE).accept(MediaType.TEXT_HTML).post(ClientResponse.class, form);
            if (response.getStatus() != 200)
                throw new ServiceException("Response status was not succesful. It was " + response.getStatus());
        } catch (ClientHandlerException e) {
            logger.log("Something went wrong while sending selecting playlist. " + e.getMessage(), LogSeverity.ERROR);
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public boolean playlistPlaying() throws ServiceException {
        logger.log("Requesting playlist status...", LogSeverity.TRACE);
        try {
            WebResource resource = client.resource(baseUri + "playlistPlaying");
            PlayerStatus response = resource.type(MediaType.APPLICATION_FORM_URLENCODED_TYPE).accept(MediaType.APPLICATION_JSON).get(PlayerStatus.class);
            return response.getPlaying();
            
        } catch (ClientHandlerException e) {
            logger.log("Something went wrong while checking if playlist is playing. " + e.getMessage(), LogSeverity.ERROR);
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public long getCurrentPlaylistId() throws NotPlaylistPlayingException {
        logger.log("Requesting playlist status...", LogSeverity.TRACE);
        WebResource resource = client.resource(baseUri + "playlistPlaying");
        PlayerStatus response = resource.type(MediaType.APPLICATION_FORM_URLENCODED_TYPE).accept(MediaType.APPLICATION_JSON).get(PlayerStatus.class);
        if (response.getPlaying())
            return response.getSelectedPlaylist();
        throw new NotPlaylistPlayingException("No playlist playing.");
    }
    
    @Override
    public void addToQueue(String location) throws ServiceException {
        try {
            logger.log("Add location queue command received, sending...", LogSeverity.TRACE);
            Form form = new Form();
            form.add("location", location);
            WebResource resource = client.resource(baseUri + "addToQueue");
            ClientResponse response = resource.type(MediaType.APPLICATION_FORM_URLENCODED_TYPE).accept(MediaType.TEXT_HTML).post(ClientResponse.class, form);
            if (response.getStatus() != 200)
                throw new ServiceException("Response status was not succesful. It was " + response.getStatus());
        } catch (ClientHandlerException e) {
            logger.log("Something went wrong while sending selecting playlist. " + e.getMessage(), LogSeverity.ERROR);
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public boolean readyForPlay() throws ServiceException {
        try {
            logger.log("Requesting ready for play status...", LogSeverity.TRACE);
            WebResource resource = client.resource(baseUri + "playlistPlaying");
            PlayerStatus response = resource.type(MediaType.APPLICATION_FORM_URLENCODED_TYPE).accept(MediaType.APPLICATION_JSON).get(PlayerStatus.class);
            return response.getReadyForPlay();
        } catch (ClientHandlerException e) {
            logger.log("Something went wrong while sending selecting playlist. " + e.getMessage(), LogSeverity.ERROR);
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public void pause() throws ServiceException {
        logger.log("Pause command received, sending...", LogSeverity.TRACE);
        WebResource resource = client.resource(baseUri + "pause");
        
        ClientResponse response = resource.type(MediaType.APPLICATION_FORM_URLENCODED_TYPE).accept(MediaType.TEXT_HTML).post(ClientResponse.class);
        if (response.getStatus() != 200)
            throw new ServiceException("Response status was not succesful. It was " + response.getStatus());
    }

    @Override
    public String getTitle() throws ServiceException {
        try {
            logger.log("Requesting title...", LogSeverity.TRACE);
            WebResource resource = client.resource(baseUri + "playlistPlaying");
            PlayerStatus response = resource.type(MediaType.APPLICATION_FORM_URLENCODED_TYPE).accept(MediaType.APPLICATION_JSON).get(PlayerStatus.class);
            return response.getTitle();
        } catch (ClientHandlerException e) {
            logger.log("Something went wrong while sending selecting playlist. " + e.getMessage(), LogSeverity.ERROR);
            throw new ServiceException(e.getMessage(), e);
        }
    }

    private static Client buildClient(){
        ClientConfig config = new DefaultClientConfig();
        config.getClasses().add(JacksonJsonProvider.class);
        return Client.create(config);
    }
}
