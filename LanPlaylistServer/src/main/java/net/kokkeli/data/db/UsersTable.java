package net.kokkeli.data.db;

import java.util.ArrayList;
import java.util.Collection;

import net.kokkeli.data.Role;
import net.kokkeli.data.User;

import com.almworks.sqlite4java.SQLiteConnection;
import com.almworks.sqlite4java.SQLiteConstants;
import com.almworks.sqlite4java.SQLiteException;
import com.almworks.sqlite4java.SQLiteJob;
import com.almworks.sqlite4java.SQLiteQueue;
import com.almworks.sqlite4java.SQLiteStatement;

public class UsersTable {
    private static final String TABLENAME = "users";
    private static final String ALLUSERS = "SELECT * FROM " + TABLENAME;
    
    private static final String COLUMN_ID = "Id";
    private static final String COLUMN_USERNAME = "Username";
    private static final String COLUMN_ROLE = "Role";
    private static final String COLUMN_PASSWORDHASH = "PasswordHash";
    
    private final SQLiteQueue queue;
    
    public UsersTable(SQLiteQueue queue){
        this.queue = queue;
    }
    
    /**
     * Returns user from table.
     * @param id Id to search
     * @return User
     * @throws DatabaseException Thrown if there is a problem with the database
     * @throws NotFoundInDatabase Thrown if user is not found in database.
     */
    public User get(final long id) throws DatabaseException, NotFoundInDatabase {
        User user = queue.execute(new SQLiteJob<User>() {
            @Override
            protected User job(SQLiteConnection connection) throws SQLiteException {
                SQLiteStatement st = connection.prepare(getSingleUserQuery(id));

                User user = null;
                try {
                    while (st.step()) {
                        long userId = st.columnLong(0);
                        String userName = st.columnString(1);
                        int roleId = st.columnInt(2);
                        
                        Role role;
                        
                        try {
                            role = Role.getRole(roleId);
                        } catch (IndexOutOfBoundsException e) {
                            throw new SQLiteException(SQLiteConstants.SQLITE_MISMATCH, String.format("Value in Role column was invalid. It was %s", roleId));
                        }

                        user = new User(userId, userName, role);
                        user.setPasswordHash(st.columnString(3));
                        
                    }
                } finally {
                    st.dispose();
                }

                return user;
            }
        }).complete();
        
        //TODO How to handle database exception?
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
        ArrayList<User> users = queue.execute(new SQLiteJob<ArrayList<User>>() {
            @Override
            protected ArrayList<User> job(SQLiteConnection connection) throws SQLiteException {
                ArrayList<User> users = new ArrayList<User>();

                SQLiteStatement st = connection.prepare(ALLUSERS);

                try {
                    while (st.step()) {
                        long id = st.columnLong(0);
                        String userName = st.columnString(1);
                        int roleId = st.columnInt(2);
                        
                        Role role;
                        
                        try {
                            role = Role.getRole(roleId);
                        } catch (IndexOutOfBoundsException e) {
                            throw new SQLiteException(SQLiteConstants.SQLITE_MISMATCH, String.format("Value in Role column was invalid. It was %s", roleId));
                        }

                        User user = new User(id, userName, role);
                        user.setPasswordHash(st.columnString(3));
                        
                        users.add(user);
                    }
                } finally {
                    st.dispose();
                }

                return users;

            }
        }).complete();

        if (users == null){
            throw new DatabaseException("Error occurred in database when trying to get all users.");
        }

        return users;
    }

    /**
     * Inserts given user to database.
     * @param item User to add.
     * @return Inserted user, with id.
     * @throws DatabaseException Thrown if there is a problem with the database
     */
    public User insert(final User item) throws DatabaseException {
        if (exists(item.getUserName()))
            throw new DatabaseException("Username already exists.");
        
        Long id = queue.execute(new SQLiteJob<Long>() {
            @Override
            protected Long job(SQLiteConnection connection) throws SQLiteException {
                SQLiteStatement st = connection.prepare(createInsertString(item));

                try {
                    st.stepThrough();
                } finally {
                    st.dispose();
                }

                return connection.getLastInsertId();
            }
        }).complete();

        if (id == null) {
            throw new DatabaseException("Inserting user failed.");
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
    public boolean exists(final String username) throws DatabaseException {
        Boolean exists = queue.execute(new SQLiteJob<Boolean>() {
            @Override
            protected Boolean job(SQLiteConnection connection) throws SQLiteException {
                SQLiteStatement st = connection.prepare(getUserWithUsername(username));
                try {
                    while (st.step()) {
                        return true;
                    }
                } finally {
                    st.dispose();
                }
                return false;
            }
        }).complete();

        if (exists == null) {
            throw new DatabaseException("Unable to check if username exists in database.");
        }
        
        return exists;
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
        
        final String query = String.format("%s %s %s", update,set,where);

        queue.execute(new SQLiteJob<Object>() {
            @Override
            protected Object job(SQLiteConnection connection) throws SQLiteException {
                SQLiteStatement st = connection.prepare(query);
                try {
                    st.stepThrough();
                } finally {
                    st.dispose();
                }
                return null;
            }
        });
    }
    
    /**
     * Udpates password hash
     * @param id
     * @param passwordHash
     */
    public void updatePasswordHash(long id, String passwordHash) {
        String update = String.format("UPDATE %s", TABLENAME);
        String set = String.format("SET %s", format(COLUMN_PASSWORDHASH, passwordHash));
        String where = String.format("WHERE %s", format(COLUMN_ID, id+""));
        
        final String query = String.format("%s %s %s", update,set,where);

        queue.execute(new SQLiteJob<Object>() {
            @Override
            protected Object job(SQLiteConnection connection) throws SQLiteException {
                SQLiteStatement st = connection.prepare(query);
                try {
                    st.stepThrough();
                } finally {
                    st.dispose();
                }
                return null;
            }
        });
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
        return ALLUSERS + " WHERE "+ COLUMN_USERNAME + " = '" + username + "'";
    }
    
    /**
     * Creates insert statement for User.
     * @param user User
     * @return Insert statement
     */
    private static String createInsertString(User user){
        return String.format("INSERT INTO %s (%s, %s, %s) VALUES ('%s', %s, '%s')",TABLENAME, COLUMN_USERNAME, COLUMN_ROLE, COLUMN_PASSWORDHASH, user.getUserName(), user.getRole().getId(), user.getPasswordHash());
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
