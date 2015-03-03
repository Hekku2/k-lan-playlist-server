package net.kokkeli.data.dto;

/**
 * Track-class
 * 
 * Contains all data related to track.
 * @author Hekku2
 *
 */
public class Track {
    private String trackName;
    private String artist;
    private long id;
    private String location;
    private User uploader;
    private boolean exists;
    
    /**
     * Creates track with id.
     * @param id
     */
    public Track(long id){
        this.id = id;
    }
    
    /**
     * Creates track with no id.
     */
    public Track(){
        
    }
    
    /**
     * Setter for id
     * @param id Id
     */
    public void setId(long id){
        this.id = id;
    }
    
    /**
     * Returns id of track.
     * @return
     */
    public long getId(){
        return id;
    }
    
    /**
     * Sets name of item
     * @param name
     */
    public void setTrackName(String name){
        this.trackName = name;
    }
    
    /**
     * Getter for name
     * @return Name of item
     */
    public String getTrackName(){
        return trackName;
    }
    
    /**
     * Sets name for artist
     * @param artist
     */
    public void setArtist(String artist){
        this.artist = artist;
    }
    
    /**
     * Getter for artist name
     * @return
     */
    public String getArtist(){
        return artist;
    }

    /**
     * Getter for location
     * @return Location of track
     */
    public String getLocation() {
        return location;
    }

    /**
     * LSetter for location
     * @param location Location of track
     */
    public void setLocation(String location) {
        this.location = location;
    }
    
    /**
     * Sets uploader for item
     * @param uploader Name of uploader
     */
    public void setUploader(User uploader){
        this.uploader = uploader;
    }
    
    /**
     * Getter for uploader
     * @return Uploader
     */
    public User getUploader(){
        return uploader;
    }

    /**
     * Sets exists for this item. Used when it is checked if track exists on filesystem.
     * @param exists
     */
    public void setExists(boolean exists){
        this.exists = exists;
    }
    
    /**
     * Getter for exists
     * @return
     */
    public boolean getExists() {
        return exists;
    }
    
    public boolean hasUploader(){
        return this.uploader != null;
    }
}
