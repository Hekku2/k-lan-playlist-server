package net.kokkeli.data;

import java.util.Collection;

import com.sun.jersey.api.NotFoundException;

/**
 * User service interface
 * @author Hekku2
 *
 */
public interface IUserService {

    /**
     * Returns user with given id
     * @param id Id of user
     * @return User with given id.
     */
    User get(int id) throws NotFoundException;
    
    /**
     * Collection of users
     * @return Collection fo users
     */
    Collection<User> get();
}
