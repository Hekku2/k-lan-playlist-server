package net.kokkeli.resources.models;

import net.kokkeli.data.FetchStatus;
import net.kokkeli.data.Track;
import net.kokkeli.resources.Field;

/**
 * Model for fetch request item
 * @author Hekku2
 */
public class ModelFetchRequest extends ViewModel{
    private String location;
    private String handler;
    private FetchStatus status;
    private String destination;
    private Track track;

    /**
     * Setter for status
     * @param status Status
     */
    public void setStatus(FetchStatus status) {
        this.status = status;
    }

    /**
     * Sets location of model
     * @param location Location
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Sets handler of model
     * @param handler Handler
     */
    public void setHandler(String handler) {
        this.handler = handler;
    }

    /**
     * Returns location of model
     * @return Model location
     */
    @Field
    public String getLocation() {
        return location;
    }

    /**
     * Returns handler of model
     * @return Model handler
     */
    @Field
    public String getHandler() {
        return handler;
    }
    
    /**
     * Getter for status
     * @return Status
     */
    @Field
    public String getStatus() {
        return status.toString();
    }

    /**
     * Gets destination
     * @return Destination
     */
    @Field
    public String getDestination() {
        return destination;
    }

    /**
     * Sets destiantion
     * @param destination Destination
     */
    public void setDestination(String destination) {
        this.destination = destination;
    }

    /**
     * Gets track
     * @return Track
     */
    @Field
    public String getTrack() {
        return String.format("%s - %s", track.getArtist(), track.getTrackName());
    }

    /**
     * Sets track
     * @param track Track
     */
    public void setTrack(Track track) {
        this.track = track;
    }
}
