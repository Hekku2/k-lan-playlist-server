package net.kokkeli.data;

/**
 * User class
 * @author Hekku2
 *
 */
public class User {
    
    private String userName;
    private long id;
    private Role role;
    
    /**
     * Creates user with given username
     * @param userName Username
     */
    public User(long id, String userName, Role role){
        this.userName = userName;
        this.id = id;
        this.role = role;
    }
    
    /**
     * Getter for username of user.
     * @return Username of user
     */
    public String getUserName(){
        return userName;
    }
    
    /**
     * Getter for id of user
     * @return Id of user
     */
    public long getId(){
        return id;
    }
    
    /**
     * Getter for role of user
     * @return Role of user
     */
    public Role getRole(){
        return role;
    }
}
