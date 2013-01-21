package net.kokkeli.resources.models;

import java.util.ArrayList;

import net.kokkeli.resources.ModelCollection;
import net.kokkeli.resources.Field;
import net.kokkeli.resources.Model;

/**
 * View model for playlist
 * @author Hekku2
 *
 */
@Model
public class ModelPlaylist extends ViewModel{
    private ArrayList<ModelPlaylistItem> items = new ArrayList<ModelPlaylistItem>();
    private String name;
    private long id;
    
    /**
     * Create object with given id.
     * @param id Id
     */
    public ModelPlaylist(long id){
        this.id = id;
    }
    
    @Field
    public String getName(){
        return name;
    }
    
    /**
     * Setter for name
     * @param name Playlist name
     */
    public void setName(String name){
        this.name = name;
    }
    
    @ModelCollection
    public ArrayList<ModelPlaylistItem> getItems(){
        return items;
    }
    
    @Field
    public long getId(){
        return id;
    }
}
