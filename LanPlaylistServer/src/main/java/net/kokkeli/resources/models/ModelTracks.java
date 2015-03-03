package net.kokkeli.resources.models;

import java.util.ArrayList;

/**
 * Viewmodel for Tracks
 * @author Hekku2
 *
 */
@Model
public final class ModelTracks extends ViewModel {
    private ArrayList<ModelPlaylistItem> items = new ArrayList<ModelPlaylistItem>();
    
    @ModelCollection
    public ArrayList<ModelPlaylistItem> getItems(){
        return items;
    }
}
