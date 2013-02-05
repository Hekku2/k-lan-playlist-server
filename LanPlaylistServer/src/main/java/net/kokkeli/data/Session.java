package net.kokkeli.data;

/**
 * Session object. Contains session related data.
 * @author Hekku2
 *
 */
public class Session {
    private User user;
    private String authId;
    private String error;
    
    /**
     * Constructs session from user.
     * @param user
     */
    public Session(User user){
        this.user = user;
    }
    
    /**
     * User of session
     * @return User
     */
    public User getUser(){
        return user;
    }

    /**
     * Authentication ID for this session
     * @return
     */
    public String getAuthId() {
        return authId;
    }
    
    /**
     * Sets authentication id for this session.
     * @param authId Authentication id
     */
    public void setAuthId(String authId){
        this.authId = authId;
    }
    
    /**
     * setter for temporary error field
     * @param error Error
     */
    public void setError(String error){
        this.error = error;
    }
    
    /**
     * Getter for temporary error field
     * @return Error
     */
    public String getError(){
        return error;
    }
}
