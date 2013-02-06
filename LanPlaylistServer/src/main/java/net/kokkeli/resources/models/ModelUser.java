package net.kokkeli.resources.models;

import net.kokkeli.data.Role;
import net.kokkeli.resources.Field;

/**
 * Viewmodel for User
 * @author Hekku2
 *
 */
public final class ModelUser extends ViewModel {
    private String username;
    private final Role role;
    private final long id;
    
    /**
     * Creates user model
     */
    public ModelUser(long id, String username, Role role){
        this.username = username;
        this.role = role;
        this.id = id;
    }
    
    /**
     * Getter for username.
     * @return Username
     */
    @Field
    public String getUsername(){
        return username;
    }
    
    /**
     * Getter for role
     * @return Role name as string.
     */
    @Field
    public String getRole(){
        return role.toString();
    }
    
    /**
     * Getter for role enum
     * @return Role as enum.
     */
    public Role getRoleEnum(){
        return role;
    }
    
    /**
     * Getter for int 
     * @return Id of user
     */
    @Field
    public long getId(){
        return id;
    }
    
    /**
     * Setter for username
     * @param username Username
     */
    public void setUsername(String username){
        this.username = username;
    }
}
