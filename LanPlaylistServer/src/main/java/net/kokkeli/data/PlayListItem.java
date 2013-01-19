package net.kokkeli.data;

/**
 * PlayListItem
 * @author Hekku2
 *
 */
public class PlayListItem {
    private String trackName;
    private String artist;
    
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
}
