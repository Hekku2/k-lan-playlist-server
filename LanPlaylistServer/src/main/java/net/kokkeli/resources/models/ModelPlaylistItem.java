package net.kokkeli.resources.models;

import net.kokkeli.resources.Field;

/**
 * Playlist item
 * @author Hekku2
 *
 */
public final class ModelPlaylistItem extends ViewModel{
    private String name;
    
    /**
     * Sets name of item
     * @param name
     */
    public void setName(String name){
        this.name = name;
    }
    
    /**
     * Getter for name
     * @return Name of item
     */
    @Field
    public String getName(){
        return name;
    }
}
