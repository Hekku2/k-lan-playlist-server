package net.kokkeli.resources.models;

import net.kokkeli.data.Role;
import net.kokkeli.resources.Field;

/**
 * Viewmodel for User
 * @author Hekku2
 *
 */
public final class ModelUser extends ViewModel {
    private final String username;
    private final Role role;
    private final int id;
    
    /**
     * Creates user model
     */
    public ModelUser(int id, String username, Role role){
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
     * Getter for int 
     * @return Id of user
     */
    @Field
    public int getId(){
        return id;
    }
}
