package net.kokkeli.resources.models;

import java.util.ArrayList;

import net.kokkeli.resources.ModelCollection;

/**
 * Model for fetch request creation
 * @author Hekku2
 */
public class ModelFetchRequestCreate extends FetchRequestBase{
    private ArrayList<ModelPlaylistListItem> items = new ArrayList<ModelPlaylistListItem>();
    
    /**
     * Playlists
     * @return playlists
     */
    @ModelCollection
    public ArrayList<ModelPlaylistListItem> getItems(){
        return items;
    }
}
