package net.kokkeli.data;

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
    
    /**
     * Creates track with id.
     * @param id
     */
    public Track(long id){
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
}
