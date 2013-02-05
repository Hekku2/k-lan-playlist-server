package net.kokkeli.data.services;

import java.util.Collection;

import net.kokkeli.data.ILogger;
import net.kokkeli.data.User;
import net.kokkeli.data.db.DatabaseException;
import net.kokkeli.data.db.IUserDatabase;
import net.kokkeli.data.db.NotFoundInDatabase;
import net.kokkeli.resources.authentication.AuthenticationUtils;

import com.google.inject.Inject;

/**
 * User service implementation
 * @author Hekku2
 *
 */
public class UserService implements IUserService {
    private ILogger logger;
    private IUserDatabase userDatabase;
    
    /**
     * Creates user service with given logger
     * @param logger Logger.
     */
    @Inject
    public UserService(ILogger logger, IUserDatabase database){
        this.logger = logger;
        this.userDatabase = database;
    }
    
    @Override
    public User get(long id) throws ServiceException, NotFoundInDatabase {
        try {
            User user = userDatabase.get(id);
            logger.log("User gotten with id: " + id, 1);
            return user;
        } catch (NotFoundInDatabase e) {
            logger.log("No user exists with id: " + id, 1);
            throw e;
        } catch (DatabaseException e) {
            throw new ServiceException("There was problem with database.", e);
        }
    }

    @Override
    public Collection<User> get() throws ServiceException {
        try {
            Collection<User> users = userDatabase.get();
            logger.log("Users gotten.", 1);
            return users;
        } catch (DatabaseException e) {
            logger.log("Something went wrong.", 5);
            throw new ServiceException("There was problem with database.", e);
        }
    }

    @Override
    public void update(User user) throws NotFoundInDatabase, ServiceException {
        try {
            User oldUser = userDatabase.get(user.getId());
            //TODO SQL Injection protection...
            
            //No point updating if user is same.
            if (user.equals(oldUser))
                return;
            
            userDatabase.update(user);
            logger.log(String.format("User (ID: %s) updated", user.getId()), 1);
            
        } catch (NotFoundInDatabase e) {
            logger.log("No user exists with id: " + user.getId(), 1);
            throw new NotFoundInDatabase(String.format("User with id %s not found.", user.getId()));
        } catch (DatabaseException e) {
            throw new ServiceException("There was problem with database.", e);
        }
    }

    @Override
    public void add(User user) throws ServiceException {
        //TODO SQL Injection protection
        
        try {
            userDatabase.add(user);
        } catch (DatabaseException e) {
            throw new ServiceException("There was problem with database.", e);
        }
    }

    @Override
    public User get(String username) throws ServiceException, NotFoundInDatabase {
        try {
            Collection<User> users = userDatabase.get();
            
            for (User user : users) {
                if (username.equals(user.getUserName()))
                    return user;
            }
            throw new NotFoundInDatabase(String.format("User with username %s not found.", username));
            
        } catch (DatabaseException e) {
            throw new ServiceException("There is a problem with the database.", e);
        }
    }

    @Override
    public boolean exists(String username) throws ServiceException {
        try {
            get(username);
            return true;
        } catch (NotFoundInDatabase e) {
            return false;
        }
    }

    @Override
    public User get(String username, String password) throws NotFoundInDatabase, ServiceException {
        User user = get(username);
        if (AuthenticationUtils.matches(password, user.getPasswordHash())){
            return user;
        }
        throw new NotFoundInDatabase("Wrong username or password.");
    }
}
