package net.kokkeli.data.services;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;

import net.kokkeli.ISettings;
import net.kokkeli.data.ILogger;
import net.kokkeli.data.User;
import net.kokkeli.data.db.DatabaseException;
import net.kokkeli.data.db.IUserDatabase;
import net.kokkeli.data.db.NotFoundInDatabase;
import com.google.inject.Inject;

/**
 * User service implementation
 * @author Hekku2
 *
 */
public class UserService implements IUserService {
    private final ILogger logger;
    private final IUserDatabase userDatabase;
    private final MessageDigest hasher;
    
    private final String passwordSalt;
    
    /**
     * Creates user service with given logger
     * @param logger Logger.
     * @throws ServiceException Thrown if service can't be created.
     */
    @Inject
    public UserService(ILogger logger, IUserDatabase database, ISettings settings) throws ServiceException{
        this.logger = logger;
        this.userDatabase = database;
        
        passwordSalt = settings.getPasswordSalt();
        
        try {
            hasher = MessageDigest.getInstance("SHA");
        } catch (NoSuchAlgorithmException e) {
            throw new ServiceException("Service can't be created: " + e, e);
        }
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
        if (matches(password, user.getPasswordHash())){
            return user;
        }
        throw new NotFoundInDatabase("Wrong username or password.");
    }
    
    /**
     * Checks if password matches hash
     * @param password Password
     * @param passwordHash Password hash
     * @return True, if password matches hash
     */
    private boolean matches(String password, String passwordHash) {
        String combined = password + passwordSalt;
        hasher.update(combined.getBytes(), 0, combined.length());
        String hash = new BigInteger(1,hasher.digest()).toString(16);
        
        logger.log(password + passwordSalt, 3);
        logger.log("Calculated: " + hash, 3);
        logger.log("Database  : " + passwordHash, 3);
        return hash.equals(passwordHash);
    }
}
