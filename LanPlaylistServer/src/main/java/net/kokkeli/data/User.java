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
    private String passwordHash;
    
    /**
     * Creates user with given username and role
     * @param userName Username
     * @param role Role
     */
    public User(String userName, Role role){
        this.userName = userName;
        this.role = role;
    }
    
    /**
     * Creates user with given username
     * @param id Id
     * @param userName Username
     * @param role Role
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
     * Sets id for user
     * @param id Id of user
     */
    public void setId(long id){
        this.id = id;
    }
    
    /**
     * Getter for role of user
     * @return Role of user
     */
    public Role getRole(){
        return role;
    }
    
    /**
     * Checks if user is same as this.
     * @param user Compared user
     * @return True, if users are same
     */
    public boolean equals(User user){
        if (user == null) return false;
        
        if (this.id != user.getId())
            return false;
        if (!this.userName.equals(user.getUserName()))
            return false;
        if (this.role != user.getRole())
            return false;
        
        return true;
    }
    
    /**
     * Checks if object is same as this.
     * @param object Compared object
     * @return True, if objects are same
     */
    @Override
    public boolean equals(Object object){
        if (object == null || object.getClass() != this.getClass())
            return false;

        return equals((User)object);
    }

    /**
     * Returns password hash of user
     * @return
     */
    public String getPasswordHash() {
        return passwordHash;
    }

    /**
     * Setter for password hash
     * @param passwordHash Password hash
     */
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }
}
