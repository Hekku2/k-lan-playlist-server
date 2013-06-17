package net.kokkeli.data.db;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.almworks.sqlite4java.SQLite;

import net.kokkeli.ISettings;

/**
 * Abstract class for database classes.
 * @author Hekku2
 *
 */
public abstract class Database {

    private final String databaseLocation;
    
    /**
     * Creates database and checks its format.
     * @param settings
     * @throws DatabaseException
     */
    public Database(ISettings settings) throws DatabaseException{
        SQLite.setLibraryPath(settings.getLibLocation());
        Logger.getLogger("com.almworks.sqlite4java").setLevel(Level.OFF);
        
        databaseLocation = settings.getDatabaseLocation();
    }
    
    /**
     * Database location
     * @return Database location
     */
    protected String getDatabaseLocation(){
        return databaseLocation;
    }
    
    public abstract void CheckDatabaseFormat() throws DatabaseException;
}
