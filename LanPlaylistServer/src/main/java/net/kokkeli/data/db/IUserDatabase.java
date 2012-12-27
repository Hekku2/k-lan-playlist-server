package net.kokkeli.data.db;

import net.kokkeli.data.User;

/**
 * User database
 * @author Hekku2
 *
 */
public interface IUserDatabase extends IDatabase<User>{
    /**
     * Updates user.
     * Doesn't update if  user is same.
     * @param user User
     * @throws DatabaseException Thrown if there is problem with database.
     */
    void update(User user) throws DatabaseException;
    
    /**
     * Checks if username exists
     * @param username Username
     * @return True, if username exists
     * @throws DatabaseException Thrown, if there is problem with database
     */
    boolean exists(String username) throws DatabaseException;
}
