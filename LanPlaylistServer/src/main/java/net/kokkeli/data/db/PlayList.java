package net.kokkeli.data.db;

import java.util.ArrayList;

import net.kokkeli.data.PlayListItem;

public class PlayList {
    private ArrayList<PlayListItem> items = new ArrayList<PlayListItem>();
    private String name;
    
    /**
     * Returns name of playlist.
     * @return
     */
    public String getName(){
        return name;
    }
    
    /**
     * Sets name for playlist
     * @param name Name of playlist
     */
    public void setName(String name){
        this.name = name;
    }
    
    /**
     * Returns items of playlist
     * @return Items of playlist
     */
    public ArrayList<PlayListItem> getItems(){
        return items;
    }
}
