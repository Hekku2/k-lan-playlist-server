package net.kokkeli.resources.models;

import net.kokkeli.resources.Field;

public class ModelTrack extends ViewModel {
    private String trackName;
    private String artist;
    private long id;
    private String location;
    private String uploader;
    private boolean exists;
    
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
    @Field
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
    @Field
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
    @Field
    public String getArtist(){
        return artist;
    }

    /**
     * Getter for location
     * @return Location of track
     */
    @Field
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
    public void setUploader(String uploader){
        this.uploader = uploader;
    }
    
    /**
     * Getter for uploader
     * @return Uploader
     */
    @Field
    public String getUploader(){
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
    @Field
    public boolean getExists() {
        return exists;
    }
}
