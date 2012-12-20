package net.kokkeli.data.db;

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
        databaseLocation = settings.getDatabaseLocation();
        
        CheckDatabaseFormat();
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
