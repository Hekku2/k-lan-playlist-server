package net.kokkeli.data.db;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Abstract class for database classes.
 * @author Hekku2
 *
 */
public abstract class Database {   
    /**
     * Creates database and checks its format.
     * @param settings
     * @throws DatabaseException
     */
    public Database() {
        Logger.getLogger("com.almworks.sqlite4java").setLevel(Level.OFF);
    }
    
    /**
     * This method should check that database is formatted correctly (All objects can be constructed)
     * @throws DatabaseException
     */
    public abstract void CheckDatabaseFormat() throws DatabaseException;
}
