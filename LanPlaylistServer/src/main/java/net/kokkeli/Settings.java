package net.kokkeli;

/**
 * Class for settings
 * @author Hekku2
 *
 */
public class Settings implements ISettings {
    
    /**
     * Location of database.
     * @return Location of database.
     */
    public String getDatabaseLocation(){
        return "db/database.db";
    }
    
    public String getLibLocation(){
        return "lib";
    }
}
