package net.kokkeli.data.db;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import net.kokkeli.data.Role;
import net.kokkeli.data.User;

import com.almworks.sqlite4java.SQLiteConnection;
import com.almworks.sqlite4java.SQLiteException;
import com.almworks.sqlite4java.SQLiteStatement;

public class UsersTable {
    private static final String TABLENAME = "users";
    private static final String ALLUSERS = "SELECT * FROM " + TABLENAME;
    
    private static final String COLUMN_ID = "Id";
    private static final String COLUMN_USERNAME = "Username";
    private static final String COLUMN_ROLE = "Role";
    
    private final String databaseLocation;
    
    public UsersTable(String databaseLocation){
        this.databaseLocation = databaseLocation;
    }
    
    /**
     * Returns user from table.
     * @param id Id to search
     * @return User
     * @throws DatabaseException Thrown if there is a problem with the database
     * @throws NotFoundInDatabase Thrown if user is not found in database.
     */
    public User get(long id) throws DatabaseException, NotFoundInDatabase {
        SQLiteConnection db = new SQLiteConnection(new File(databaseLocation));
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
                    user.setPasswordHash(st.columnString(3));
                    
                }
            } finally {
                st.dispose();
            }
            db.dispose();
        } catch (SQLiteException e) {
            throw new DatabaseException("Unable to get user with Id: " + id, e);
        }
        if (user == null)
            throw new NotFoundInDatabase("User not found in database");
        
        return user;
    }
    
    /**
     * Returns list of users. If no users exists, empty list is returned.
     * @return List of users.
     * @throws DatabaseException Thrown if there is a problem with the database
     */
    public Collection<User> get() throws DatabaseException {
        SQLiteConnection db = new SQLiteConnection(new File(databaseLocation));
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

                    User user = new User(id, userName, role);
                    user.setPasswordHash(st.columnString(3));
                    
                    users.add(user);
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
     * Inserts given user to database.
     * @param item User to add.
     * @return Inserted user, with id.
     * @throws DatabaseException Thrown if there is a problem with the database
     */
    public User insert(User item) throws DatabaseException {
        if (equals(item.getUserName()))
            throw new DatabaseException("Username already exists.");
        
        SQLiteConnection db = new SQLiteConnection(new File(databaseLocation));
        long id;
        try {
            db.open(false);
            SQLiteStatement st = db.prepare(createInsertString(item));
            try {
                st.stepThrough();
            } finally {
                st.dispose();
            }
            id = db.getLastInsertId();
            
            db.dispose();
        } catch (SQLiteException e) {
            throw new DatabaseException("There was problem with database", e);
        }
        
        item.setId(id);
        return item;
    }
    
    /**
     * Checks if user with username exists. exist.
     * @param username Username
     * @return True, if user with username exists.
     * @throws DatabaseException Thrown if there is a problem with the database
     */
    public boolean exists(String username) throws DatabaseException {
        SQLiteConnection db = new SQLiteConnection(new File(databaseLocation));
        
        try {
            db.open(false);
            SQLiteStatement st = db.prepare(getUserWithUsername(username));
            try {
                while (st.step()) {
                    return true;
                }
            } finally {
                st.dispose();
            }
            
            db.dispose();
        } catch (SQLiteException e) {
            throw new DatabaseException("Problem with database.", e);
        }
        return false;
    }
    
    /**
     * Updates user to database.
     * @param user User to update
     * @throws DatabaseException Thrown if there is a problem with the database
     */
    public void update(User user) throws DatabaseException {
        String update = String.format("UPDATE %s", TABLENAME);
        String set = String.format("SET %s, %s", format(COLUMN_USERNAME, user.getUserName()), format(COLUMN_ROLE, user.getRole().getId()+""));
        String where = String.format("WHERE %s", format(COLUMN_ID, user.getId()+""));
        
        String query = String.format("%s %s %s", update,set,where);
        SQLiteConnection db = new SQLiteConnection(new File(databaseLocation));
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
     * Creates query selecting single user.
     * @param id Id of wanted user
     * @return Query for selecting single user.
     */
    private static String getSingleUserQuery(long id){
        return ALLUSERS + " WHERE "+ COLUMN_ID+" = " + id;
    }
    
    /**
     * Creates query selecting users with given username.
     * @param username Username of user
     * @return Query for selecting users with given username.
     */
    private static String getUserWithUsername(String username){
        return ALLUSERS + " WHERE "+ COLUMN_USERNAME + " = " + username;
    }
    
    /**
     * Creates insert statement for User.
     * @param user User
     * @return Insert statement
     */
    private static String createInsertString(User user){
        return String.format("INSERT INTO %s (%s, %s) VALUES ('%s', %s)",TABLENAME, COLUMN_USERNAME, COLUMN_ROLE, user.getUserName(), user.getRole().getId());
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
