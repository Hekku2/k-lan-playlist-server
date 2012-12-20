package net.kokkeli.data.db;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import com.almworks.sqlite4java.SQLiteConnection;
import com.almworks.sqlite4java.SQLiteException;
import com.almworks.sqlite4java.SQLiteStatement;
import com.google.inject.Inject;
import com.sun.jersey.api.NotFoundException;

import net.kokkeli.ISettings;
import net.kokkeli.data.Role;
import net.kokkeli.data.User;

public class UserDatabase extends Database implements IUserDatabase {   
    private static final String TABLENAME = "users";
    private static final String ALLUSERS = "SELECT * FROM " + TABLENAME;
    
    /**
     * Creates query selecting single user.
     * @param id Id of wanted user
     * @return Query for selecting single user.
     */
    private static String getSingleUserQuery(long id){
        return ALLUSERS + " WHERE Id = " + id;
    }
    
    /**
     * Creates User database interface with given settings
     * @param settings Settings user for database.
     * @throws SQLiteException Thrown is database format is not right.
     */
    @Inject
    public UserDatabase(ISettings settings) throws DatabaseException{
        super(settings);
    }
    
    @Override
    public void CheckDatabaseFormat() throws DatabaseException {
        get();
    }
    
    @Override
    public User get(long id) throws DatabaseException {
        SQLiteConnection db = new SQLiteConnection(new File(getDatabaseLocation()));
        User user = null;
        
        try {
            db.open(false);
            SQLiteStatement st = db.prepare(getSingleUserQuery(id));
            try {
                while (st.step()) {
                    long userId = st.columnLong(0);
                    String userName = st.columnString(1);
                    Role role = Role.ADMIN;

                    user = new User(userId, userName, role);
                }
            } finally {
                st.dispose();
            }
            db.dispose();
        } catch (SQLiteException e) {
            throw new DatabaseException("Unable to get user with Id: " + id, e);
        }
        if (user == null)
            throw new NotFoundException();
        
        return user;
    }

    @Override
    public void update(User user) {
        //TODO Implement
    }

    @Override
    public Collection<User> get() throws DatabaseException {
        SQLiteConnection db = new SQLiteConnection(new File(getDatabaseLocation()));
        ArrayList<User> users = new ArrayList<User>();

        try {
            db.open(false);
            SQLiteStatement st = db.prepare(ALLUSERS);
            try {
                while (st.step()) {
                    long id = st.columnLong(0);
                    String userName = st.columnString(1);
                    Role role = Role.ADMIN;

                    users.add(new User(id, userName, role));
                }
            } finally {
                st.dispose();
            }
            db.dispose();
        } catch (SQLiteException e) {
            throw new DatabaseException("There was problem with database", e);
        }

        return users;
    }
}
