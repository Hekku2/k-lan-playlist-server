package net.kokkeli.resources.models;

import net.kokkeli.resources.Field;

public class ModelPlaylistItem extends ViewModel{
    private String name;
    
    public void setName(String name){
        this.name = name;
    }
    
    @Field
    public String getName(){
        return name;
    }
}
