package net.kokkeli.resources.models;


/**
 * Playlist item
 * @author Hekku2
 *
 */
public final class ModelPlaylistItem extends ViewModel{
    private String track;
    private String artist;
    private String uploader;
    private String url;
    private long id;
    private boolean exists;
    private long playlistId;
    
    /**
     * Sets name of item
     * @param name
     */
    public void setTrack(String name){
        this.track = name;
    }
    
    /**
     * Getter for name
     * @return Name of item
     */
    @Field
    public String getTrack(){
        return track;
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
    
    /**
     * Sets existance of this track
     * @param exists If true, this track exists
     */
    public void setExists(boolean exists){
        this.exists = exists;
    }
    
    /**
     * If true, this track exists
     * @return If true, this track exists
     */
    @Field
    public boolean getExists(){
        return exists;
    }
    
    /**
     * Gets the id 
     * @return Playlist if
     */
    @Field
    public long getPlaylistId(){
        return playlistId;
    }
    
    /**
     * Setter for playlist id
     * @param playlistId Playlist id
     */
    public void setPlaylistId(long playlistId){
        this.playlistId = playlistId;
    }

    /**
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param url the url to set
     */
    public void setUrl(String url) {
        this.url = url;
    }
}
