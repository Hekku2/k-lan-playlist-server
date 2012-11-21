package net.kokkeli.resources.models;

import java.util.ArrayList;
import java.util.Collection;

import net.kokkeli.resources.Model;
import net.kokkeli.resources.ModelCollection;

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
}
