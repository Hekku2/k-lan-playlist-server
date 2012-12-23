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
    
    /**
     * Returns location of library
     * @return Location of library
     */
    public String getLibLocation(){
        return "lib";
    }

    /**
     * Returns location of templates
     * @return Location of templates
     */
    public String getTemplatesLocation() {
        return "target\\classes\\net\\kokkeli\\resources\\views";
    }
}
