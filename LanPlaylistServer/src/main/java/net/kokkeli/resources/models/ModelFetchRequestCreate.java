package net.kokkeli.resources.models;

import java.util.ArrayList;

import net.kokkeli.resources.ModelCollection;

/**
 * Model for fetch request creation
 * @author Hekku2
 */
public class ModelFetchRequestCreate extends FetchRequestBase{
    private ArrayList<ModelPlaylistListItem> items = new ArrayList<ModelPlaylistListItem>();
    private long selectedPlaylistId;
    private String track;
    private String artist;
    
    /**
     * Playlists
     * @return playlists
     */
    @ModelCollection
    public ArrayList<ModelPlaylistListItem> getItems(){
        return items;
    }

    /**
     * Gets selected playlist id
     * @return SelectedPlaylistId
     */
    public long getSelectedPlaylistId() {
        return selectedPlaylistId;
    }

    /**
     * Sets selected playlist id
     * @param selectedPlaylistId SelectedPlaylistId
     */
    public void setSelectedPlaylistId(long selectedPlaylistId) {
        this.selectedPlaylistId = selectedPlaylistId;
    }

    /**
     * @return the artist
     */
    public String getArtist() {
        return artist;
    }

    /**
     * @param artist the artist to set
     */
    public void setArtist(String artist) {
        this.artist = artist;
    }

    /**
     * @return the track
     */
    public String getTrack() {
        return track;
    }

    /**
     * @param track the track to set
     */
    public void setTrack(String track) {
        this.track = track;
    }

}
