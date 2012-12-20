package net.kokkeli.data.services;

import java.util.Collection;

import net.kokkeli.data.User;

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
    User get(long id) throws NotFoundException, ServiceException;
    
    /**
     * Collection of users.
     * @return Collection fo users
     */
    Collection<User> get();
}
