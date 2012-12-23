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
    
    private static final String COLUMN_ID = "Id";
    private static final String COLUMN_USERNAME = "Username";
    private static final String COLUMN_ROLE = "Role";
    
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
                    int roleId = st.columnInt(2);
                    
                    Role role;
                    
                    try {
                        role = getRole(roleId);
                    } catch (IndexOutOfBoundsException e) {
                        throw new DatabaseException(String.format("Value in Role column was invalid. It was %s", roleId));
                    }

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
    public void update(User user) throws DatabaseException {
        String update = String.format("UPDATE %s", TABLENAME);
        String set = String.format("SET %s, %s", format(COLUMN_USERNAME, user.getUserName()), format(COLUMN_ROLE, user.getRole().getId()+""));
        String where = String.format("WHERE %s", format(COLUMN_ID, user.getId()+""));
        
        String query = String.format("%s %s %s", update,set,where);
        SQLiteConnection db = new SQLiteConnection(new File(getDatabaseLocation()));
        try {
            db.open(false);
            SQLiteStatement st = db.prepare(query);
            try {
                while (st.step());
            } finally {
                st.dispose();
            }
        } catch (SQLiteException e) {
            throw new DatabaseException("There was problem with database.", e);
        }
        

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
                    int roleId = st.columnInt(2);
                    
                    Role role;
                    
                    try {
                        role = getRole(roleId);
                    } catch (IndexOutOfBoundsException e) {
                        throw new DatabaseException(String.format("Value in Role column was invalid. It was %s", roleId));
                    }

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
    
    /**
     * Returns Role with given id.
     * @param id Id of role
     * @return Role with given id.
     */
    private static Role getRole(int id){
        
        for (Role r : Role.values()) {
            if (r.getId() == id){
                return r;
            }
        }
        throw new IndexOutOfBoundsException("There was no role with given Id.");
    }
    
    /**
     * Return String.format("%s = '%s'", columnName,value);
     * @param columnName Name of column
     * @param value Value of cell
     * @return Formated string.
     */
    private static String format(String columnName, String value){
        return String.format("%s = '%s'", columnName,value);
    }
}
