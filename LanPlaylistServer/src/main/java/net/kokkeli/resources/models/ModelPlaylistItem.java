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
    private String uploader;
    private long id;
    
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
     * Getter for Id
     * @return Id
     */
    @Field
    public long getId(){
        return id;
    }
    
    /**
     * Sets id for item
     * @param id Id of track
     */
    public void setId(long id){
        this.id = id;
    }
}
