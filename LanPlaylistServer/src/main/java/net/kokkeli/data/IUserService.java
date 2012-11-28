package net.kokkeli.data;

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
    
}
