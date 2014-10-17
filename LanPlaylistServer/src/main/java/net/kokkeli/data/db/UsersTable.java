package net.kokkeli.data.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;

import net.kokkeli.data.Role;
import net.kokkeli.data.User;

public class UsersTable {
    private static final String TABLENAME = "users";
    private static final String ALLUSERS = "SELECT * FROM " + TABLENAME;
    
    private static final String COLUMN_ID = "Id";
    private static final String COLUMN_USERNAME = "Username";
    private static final String COLUMN_ROLE = "Role";
    private static final String COLUMN_PASSWORDHASH = "PasswordHash";
    
    private final IConnectionStorage storage;
    
    public UsersTable(IConnectionStorage storage){
        this.storage = storage;
    }
    
    /**
     * Returns user from table.
     * @param id Id to search
     * @return User
     * @throws DatabaseException Thrown if there is a problem with the database
     * @throws NotFoundInDatabase Thrown if user is not found in database.
     */
    @SuppressWarnings("resource")
    public User get(final long id) throws DatabaseException, NotFoundInDatabase {
        try (Connection connection = storage.getConnection()){
            try (Statement statement = connection.createStatement()){
                ResultSet rs = statement.executeQuery(getSingleUserQuery(id));
                while(rs.next())
                {
                    return createUser(rs);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Getting user failed.", e);
        }
        
        throw new NotFoundInDatabase("User not found in database");
    }
    
    /**
     * Returns list of users. If no users exists, empty list is returned.
     * @return List of users.
     * @throws DatabaseException Thrown if there is a problem with the database
     */
    @SuppressWarnings("resource") //Result set is closed by statement
    public Collection<User> get() throws DatabaseException {
        ArrayList<User> users = new ArrayList<User>();
        try (Connection connection = storage.getConnection()){
            try (Statement statement = connection.createStatement()){
                ResultSet rs = statement.executeQuery(ALLUSERS);
                while(rs.next())
                {
                    users.add(createUser(rs));
                }
                return users;
            }
        } catch (SQLException e) {
            throw new DatabaseException("Getting users failed.", e);
        }
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
        
        try (Connection connection = storage.getConnection()){
            try (Statement statement = connection.createStatement()){
                connection.setAutoCommit(false);
                statement.executeUpdate(createInsertString(item));
                long id = statement.executeQuery("SELECT last_insert_rowid()").getLong(1);
                item.setId(id);
                connection.commit();
                return item;
            }
        } catch (SQLException e) {
            throw new DatabaseException("Inserting user failed.", e);
        }
    }
    
    /**
     * Checks if user with username exists. exist.
     * @param username Username
     * @return True, if user with username exists.
     * @throws DatabaseException Thrown if there is a problem with the database
     */
    @SuppressWarnings("resource")
    public boolean exists(final String username) throws DatabaseException {
        try (Connection connection = storage.getConnection()){
            try (Statement statement = connection.createStatement()){
                ResultSet rs = statement.executeQuery(getUserWithUsername(username));
                while(rs.next())
                {
                    return true;
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Checking user username existance failed.", e);
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
        
        final String query = String.format("%s %s %s", update,set,where);

        try (Connection connection = storage.getConnection()){
            try (Statement statement = connection.createStatement()){
                statement.executeUpdate(query);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Updating user failed.", e);
        }
    }
    
    /**
     * Udpates password hash
     * @param id
     * @param passwordHash
     * @throws DatabaseException 
     */
    public void updatePasswordHash(long id, String passwordHash) throws DatabaseException {
        String update = String.format("UPDATE %s", TABLENAME);
        String set = String.format("SET %s", format(COLUMN_PASSWORDHASH, passwordHash));
        String where = String.format("WHERE %s", format(COLUMN_ID, id+""));
        
        final String query = String.format("%s %s %s", update,set,where);

        try (Connection connection = storage.getConnection()){
            try (Statement statement = connection.createStatement()){
                statement.executeUpdate(query, Statement.NO_GENERATED_KEYS);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Updating password hash failed.", e);
        }
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
    
    private static User createUser(ResultSet rs) throws SQLException, DatabaseException {
        long userId = rs.getLong(COLUMN_ID);
        String userName = rs.getString(COLUMN_USERNAME);
        Role role = role(rs.getInt(COLUMN_ROLE));
        User user = new User(userId, userName, role);
        user.setPasswordHash(rs.getString(COLUMN_PASSWORDHASH));
        return user;
    }
    
    private static Role role(int roleId) throws DatabaseException{
        try {
            return Role.getRole(roleId);
        } catch (IndexOutOfBoundsException e) {
            throw new DatabaseException(String.format("Value in Role column was invalid. It was %s", roleId));
        }
    }
}
