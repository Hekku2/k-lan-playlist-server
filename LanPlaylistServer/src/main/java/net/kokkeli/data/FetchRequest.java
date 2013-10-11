package net.kokkeli.data;

import java.util.Date;

/**
 * Fetch request holds status
 * @author Hekku2
 *
 */
public class FetchRequest {
    private String location;
    private String handler;
    private String destinationFile;
    private Date lastUpdated;
    private FetchStatus status;
    private long id;
    private Track track;
    
    /**
     * Sets location of the fetch request
     * @param location Location of fetch request
     */
    public void setLocation(String location){
        this.location = location;
    }
    
    /**
     * Gets the fetch location
     * @return Location
     */
    public String getLocation(){
        return location;
    }
    
    /**
     * Sets the handler
     * @param handler The handler
     */
    public void setHandler(String handler){
        this.handler = handler;
    }
    
    /**
     * Gets the handler
     * @return Handler
     */
    public String getHandler() {
        return handler;
    }

    /**
     * Sets the destination file
     * @param destinationFile Destination file
     */
    public void setDestinationFile(String destinationFile){
        this.destinationFile = destinationFile;
    }
    
    /**
     * Gets the destination file
     * @return Destination file
     */
    public String getDestinationFile() {
        return destinationFile;
    }
    
    /**
     * Sets last updated date of fetch request
     * @param lastUpdated Last updated
     */
    public void setLastUpdated(Date lastUpdated){
        this.lastUpdated = lastUpdated;
    }
    
    /**
     * Gets the last updated date of this request
     * @return Last updated
     */
    public Date getLastUpdated(){
        return lastUpdated;
    }
    
    /**
     * Sets the fetch status of this request
     * @param status Fetch request
     */
    public void setStatus(FetchStatus status){
        this.status = status;
    }
    
    /**
     * Gets the fetch status of this fetch request
     * @return Fetch request
     */
    public FetchStatus getStatus(){
        return status;
    }
    
    /**
     * Sets the id of this request
     * @param id Id of this request
     */
    public void setId(long id){
        this.id = id;
    }
    
    /**
     * Gets the id of this request
     * @return Id
     */
    public long getId(){
        return id;
    }

    /**
     * Gets the track
     * @return Track
     */
    public Track getTrack() {
        return track;
    }

    /**
     * Sets the track
     * @param track Track
     */
    public void setTrack(Track track) {
        this.track = track;
    }
}
