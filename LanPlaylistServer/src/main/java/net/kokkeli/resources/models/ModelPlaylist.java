package net.kokkeli.resources.models;

import java.util.ArrayList;

/**
 * View model for playlist
 * @author Hekku2
 *
 */
@Model
public class ModelPlaylist extends ModelPlaylistListItem{
    private ArrayList<ModelPlaylistItem> items = new ArrayList<ModelPlaylistItem>();
    
    /**
     * Create object with given id.
     * @param id Id
     */
    public ModelPlaylist(long id){
        super(id);
    }
    
    @ModelCollection
    public ArrayList<ModelPlaylistItem> getItems(){
        return items;
    }
    

}
