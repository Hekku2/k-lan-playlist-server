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
    private Role role;
    private long id;
    private String newPassword;
    private String confirmPassword;
    
    /**
     * Empty constructor
     */
    public ModelUser(){
        
    }
    
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
     * Sets role
     * @param role
     */
    public void setRole(Role role){
        this.role = role;
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
     * Setter for id
     * @param id
     * @return Sets id
     */
    public void setId(long id){
        this.id = id;
    }
    
    /**
     * Setter for username
     * @param username Username
     */
    public void setUsername(String username){
        this.username = username;
    }

    /**
     * Returns new password
     * @return the newPassword
     */
    public String getNewPassword() {
        return newPassword;
    }

    /**
     * Sets new password
     * @param newPassword the newPassword to set
     */
    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    /**
     * Returns confirm password
     * @return the confirmPassword
     */
    public String getConfirmPassword() {
        return confirmPassword;
    }

    /**
     * Sets confirm password
     * @param confirmPassword the confirmPassword to set
     */
    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
