package net.kokkeli.data;

/**
 * Session object. Contains session related data.
 * @author Hekku2
 *
 */
public class Session {
    private User user;
    
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
}
