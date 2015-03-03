package net.kokkeli.data.services;

import java.util.Collection;

import net.kokkeli.data.db.NotFoundInDatabaseException;
import net.kokkeli.data.dto.User;

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
    User get(long id) throws NotFoundInDatabaseException, ServiceException;
    
    /**
     * Collection of users.
     * @return Collection fo users
     * @throws ServiceException Thrown if there is problem with service
     * @throws NotFoundException Thrown if there is no user with given id.
     */
    Collection<User> get() throws ServiceException;

    /**
     * Updates given user.
     * @param user Updated user.
     * @throws ServiceException Thrown if there is problem with service
     * @throws NotFoundException Thrown if there is no old user with given id.
     */
    void update(User user) throws ServiceException, NotFoundInDatabaseException;

    /**
     * Adds given user. If user doesn't have Id, enw is created.
     * @param user New user.
     * @return Created user.
     * @throws ServiceException Thrown if there is problem with service
     */
    User add(User user) throws ServiceException;

    /**
     * Returns user that has given username
     * @param username Username
     * @return Username
     * @throws ServiceException Thrown if there is problem with the service
     * @throws NotFoundInDatabaseException Thrown if there is no such user in the database. 
     */
    User get(String username) throws ServiceException, NotFoundInDatabaseException;

    /**
     * Checks that user with given username exists
     * @param username
     * @return True, if user with username exists.
     * @throws ServiceException Thrown is there is problem with the database
     */
    boolean exists(String username) throws ServiceException;

    /**
     * Returns user with given username, ONLY if password matches
     * @param username Username
     * @param password Password
     * @return User Found user.
     * @throws NotFoundInDatabaseException Thrown if username or password is wrong.
     * @throws ServiceException Thrown if there is problem with the database.
     */
    User get(String username, String password) throws NotFoundInDatabaseException, ServiceException;
    
    /**
     * Changes users password
     * @param id Id of user
     * @param password Password of user
     * @throws ServiceException Thrown if there is problem with the database.
     */
    void changePassword(long id, String password) throws ServiceException;
    
    /**
     * Calculates hash from password
     * @param password Password
     * @return Hash
     */
    String calculateHash(String password);
}
