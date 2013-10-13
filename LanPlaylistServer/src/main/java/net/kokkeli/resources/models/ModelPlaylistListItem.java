package net.kokkeli.resources.models;

import net.kokkeli.resources.Field;
import net.kokkeli.resources.models.ViewModel;

/**
 * Playlist item model representing playlist when only name and id is needed.
 * @author Hekku2
 *
 */
public class ModelPlaylistListItem extends ViewModel{
    private String name;
    private long id;
    
    /**
     * Create object with given id.
     * @param id Id
     */
    public ModelPlaylistListItem(long id){
        this.id = id;
    }
    
    /**
     * Gets the name of playlist
     * @return
     */
    @Field
    public String getName(){
        return name;
    }
    
    /**
     * Gets the id of playlist
     * @return
     */
    @Field
    public long getId(){
        return id;
    }
    
    /**
     * Setter for name
     * @param name Playlist name
     */
    public void setName(String name){
        this.name = name;
    }
}
