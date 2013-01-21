package net.kokkeli.resources.models;

import java.util.ArrayList;

import net.kokkeli.resources.ModelCollection;

public class ModelPlaylists extends ViewModel {
    private ArrayList<ModelPlaylist> items = new ArrayList<ModelPlaylist>();
    
    /**
     * Playlists
     * @return Playlists
     */
    @ModelCollection
    public ArrayList<ModelPlaylist> getItems(){
        return items;
    }
}
