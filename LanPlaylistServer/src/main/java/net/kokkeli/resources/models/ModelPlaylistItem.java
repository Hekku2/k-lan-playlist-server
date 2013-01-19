package net.kokkeli.resources.models;

import net.kokkeli.resources.Field;

/**
 * Playlist item
 * @author Hekku2
 *
 */
public final class ModelPlaylistItem extends ViewModel{
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
}
