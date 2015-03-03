package net.kokkeli.resources.models;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Model for user collection
 * @author Hekku2
 *
 */
@Model
public class ModelUsers extends ViewModel {
    private ArrayList<ModelUser> users = new ArrayList<ModelUser>();
    
    /**
     * Return list of users
     * @return
     */
    @ModelCollection
    public ArrayList<ModelUser> getUsers(){
        return users;
    }
    
    /**
     * Adds given users to list
     * @param users Users
     */
    public void add(Collection<ModelUser> users){
        this.users.addAll(users);
    }
    
    /**
     * Adds user to model
     * @param user User
     */
    public void add(ModelUser user){
        this.users.add(user);
    }
}
