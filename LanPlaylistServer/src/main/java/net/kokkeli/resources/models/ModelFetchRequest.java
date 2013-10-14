package net.kokkeli.resources.models;

import net.kokkeli.data.FetchStatus;
import net.kokkeli.data.Track;
import net.kokkeli.resources.Field;

/**
 * Model for fetch request item
 * @author Hekku2
 */
public class ModelFetchRequest extends FetchRequestBase{
    private FetchStatus status;
    private Track track;
    private long id;

    /**
     * Setter for status
     * @param status Status
     */
    public void setStatus(FetchStatus status) {
        this.status = status;
    }

    /**
     * Getter for status
     * @return Status
     */
    @Field
    public String getStatus() {
        return status == null ? "" : status.toString();
    }

    /**
     * Gets track
     * @return Track
     */
    @Field
    public String getTrack() {
        return track == null ? "" : String.format("%s - %s", track.getArtist(), track.getTrackName());
    }

    /**
     * Sets track
     * @param track Track
     */
    public void setTrack(Track track) {
        this.track = track;
    }
    
    /**
     * Gets the id of the track
     * @return Track id
     */
    @Field
    public long getTrackId(){
        return track == null ? 0 : track.getId();
    }

    /**
     * Sets the id
     * @param id Id
     */
    public void setId(long id) {
        this.id = id;
    }
    
    /**
     * Gets the id
     * @return Id
     */
    @Field
    public long getId(){
        return id;
    }
}
