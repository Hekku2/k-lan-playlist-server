package net.kokkeli.data.db;

import net.kokkeli.data.User;

/**
 * User database
 * @author Hekku2
 *
 */
public interface IUserDatabase extends IDatabase<User>{
    void update(User user) throws DatabaseException;
}
