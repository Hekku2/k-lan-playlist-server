package net.kokkeli.resources.models;

import net.kokkeli.data.Session;
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
    private int userRole;
    private long userId;
    private String error;
    private String info;
    
    private boolean anythingPlaying;
    
    private ViewModel model;
    private Session session;
    
    /**
     * Track currently playing
     * @return Track currently playing
     */
    @Field
    public String getNowPlaying(){
        return nowPlaying;
    }
    
    /**
     * Setter for now playing.
     * @param nowPlaying Now playing
     */
    public void setNowPlaying(String nowPlaying){
        this.nowPlaying = nowPlaying;
    }
    
    /**
     * Getter for model
     * @return Model
     */
    @Model
    public ViewModel getModel(){
        return model;
    }
    
    /**
     * Setter for model
     * @param model Model
     */
    public void setModel(ViewModel model){
        this.model = model;
    }
    
    /**
     * Getter for username
     * @return Username
     */
    @Field
    public String getUsername(){
        return userName;
    }
    
    /**
     * Setter for username
     * @param userName Username
     */
    public void setUsername(String userName){
        this.userName = userName;
    }

    /**
     * Setter for error
     * @param error Error
     */
    public void setError(String error) {
        this.error = error;
    }
    
    /**
     * Return error string.
     * @return Error string
     */
    @Field
    public String getError(){
        return error;
    }
    
    /**
     * Setter for Info
     * @param info Info
     */
    public void setInfo(String info) {
        this.info = info;
    }
    
    /**
     * Return Info string.
     * @return Info string
     */
    @Field
    public String getInfo(){
        return info;
    }

    /**
     * Setter for current user.
     * @param session current session
     */
    public void setCurrentSession(Session session) {
        this.session = session;
    }
    
    /**
     * Getter for current sessions
     * @return Current session
     */
    public Session getCurrentSession(){
        return session;
    }
    
    /**
     * Return playlist playing.
     * @return playlist playing
     */
    @Field
    public boolean getAnythingPlaying(){
        return anythingPlaying;
    }
    
    /**
     * Sets value indicating if playlist is selected
     * @return playlist playing
     */
    public void setAnythingPlaying(boolean playing){
        anythingPlaying = playing;
    }

    /**
     * @return the currently logged in user id
     */
    @Field
    public long getUserId() {
        return userId;
    }

    /**
     * Sets the user ID of currently logged in user.
     * @param userId the userId to set
     */
    public void setUserId(long userId) {
        this.userId = userId;
    }

    /**
     * @return the userRole
     */
    @Field
    public int getUserRole() {
        return userRole;
    }

    /**
     * @param userRole the userRole to set
     */
    public void setUserRole(int userRole) {
        this.userRole = userRole;
    }
}
