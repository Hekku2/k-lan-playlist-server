package net.kokkeli.data;

import java.util.ArrayList;
import java.util.Collection;

import com.sun.jersey.api.NotFoundException;

/**
 * Mock implementation for user service
 * TODO Implement database.
 * @author Hekku2
 *
 */
public class UserService implements IUserService {
    private ArrayList<User> users = new ArrayList<User>();
    
    public UserService(){
        users.add(new User(3, "hekku2", Role.ADMIN));
        users.add(new User(5, "random1", Role.USER));
        users.add(new User(8, "random666", Role.USER));
    }
    
    @Override
    public User get(int id) throws NotFoundException {
        for (User user : users) {
            if (user.getId() == id)
                return user;
        }
        
        throw new NotFoundException("No user exists with id: " + id);
    }

    @Override
    public Collection<User> get() {
        return users;
    }

}
