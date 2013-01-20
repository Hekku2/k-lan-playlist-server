package net.kokkeli.data.db;

import java.util.ArrayList;

import net.kokkeli.data.Track;

public class PlayList {
    private ArrayList<Track> items = new ArrayList<Track>();
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
    public ArrayList<Track> getItems(){
        return items;
    }
}
