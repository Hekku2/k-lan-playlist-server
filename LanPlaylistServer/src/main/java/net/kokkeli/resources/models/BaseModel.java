package net.kokkeli.resources.models;

import net.kokkeli.resources.Field;
import net.kokkeli.resources.Model;

/**
 * BaseModel-class.
 * This class should holds all basic information needed to render pages.
 * @author Hekku2
 *
 */
@Model
public class BaseModel extends ViewModel {
    private String nowPlaying;
    private String userName;
    private ViewModel model;
    
    /**
     * Track currently playing
     * @return Track currently playing
     */
    @Field
    public String getNowPlaying(){
        return nowPlaying;
    }
    
    /**
     * Sets
     * @param nowPlaying
     */
    public void setNowPlaying(String nowPlaying){
        this.nowPlaying = nowPlaying;
    }
    
    @Model
    public ViewModel getModel(){
        return model;
    }
    
    public void setModel(ViewModel model){
        this.model = model;
    }
    
    @Field
    public String getUsername(){
        return userName;
    }
    
    public void setUsername(String userName){
        this.userName = userName;
    }
}
