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
     * @throws ServiceException Thrown if there is problem with service
     * @throws NotFoundException Thrown if there is no user with given id.
     */
    Collection<User> get();

    /**
     * Updates given user.
     * @param user Updated user.
     * @throws ServiceException Thrown if there is problem with service
     * @throws NotFoundException Thrown if there is no old user with given id.
     */
    void update(User user) throws ServiceException, NotFoundException;

    /**
     * Adds given user. If user doesn't have Id, enw is created.
     * @param user New user.
     * @throws ServiceException Thrown if there is problem with service
     */
    void add(User user) throws ServiceException;
}
