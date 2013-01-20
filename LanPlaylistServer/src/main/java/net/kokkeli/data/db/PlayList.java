package net.kokkeli.data.db;

import java.util.ArrayList;

import net.kokkeli.data.Track;

public class PlayList {
    private ArrayList<Track> items = new ArrayList<Track>();
    private String name;
    private final long id;
    
    /**
     * Creates playlist with specific Id.
     * @param id Id
     */
    public PlayList(long id){
        this.id = id;
    }
    
    /**
     * Getter for id
     * @return Id of playlsit
     */
    public long getId(){
        return id;
    }
    
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
