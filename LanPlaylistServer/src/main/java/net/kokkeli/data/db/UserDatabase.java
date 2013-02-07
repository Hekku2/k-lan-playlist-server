package net.kokkeli.data.db;

import java.util.Collection;

import com.google.inject.Inject;

import net.kokkeli.ISettings;
import net.kokkeli.data.User;

public class UserDatabase extends Database implements IUserDatabase {   
    private final UsersTable users;
    
    /**
     * Creates User database interface with given settings
     * @param settings Settings user for database.
     */
    @Inject
    public UserDatabase(ISettings settings) throws DatabaseException{
        super(settings);
        users = new UsersTable(getDatabaseLocation());
        CheckDatabaseFormat();
    }
    
    @Override
    public void CheckDatabaseFormat() throws DatabaseException {
        get();
    }
    
    @Override
    public User get(long id) throws DatabaseException, NotFoundInDatabase {       
        return users.get(id);
    }

    @Override
    public Collection<User> get() throws DatabaseException {
        return users.get();
    }

    @Override
    public User add(User item) throws DatabaseException {
        return users.add(item);
    }

    @Override
    public void update(User user) throws DatabaseException {
        users.update(user);
    }

    @Override
    public boolean exists(String username) throws DatabaseException {
        return users.exists(username);
    }
}
