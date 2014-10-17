package net.kokkeli.data.db;

import java.sql.Connection;
import java.sql.DriverManager;

public class SqliteConnectionStorage implements IConnectionStorage{

    private final String connection;
    
    public SqliteConnectionStorage(String connection){
        this.connection = connection;
    }
    
    @Override
    public Connection getConnection() throws DatabaseException {
        try {
            Class.forName("org.sqlite.JDBC");
            return DriverManager.getConnection(connection);
        } catch (Exception e) {
            throw new DatabaseException("Unable to provide DB connection.", e);
        }
    }

}
