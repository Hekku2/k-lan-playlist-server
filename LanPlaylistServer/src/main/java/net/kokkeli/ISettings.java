package net.kokkeli;

public interface ISettings {
    /**
     * Returns location of database
     * @return Location of database.
     */
    String getDatabaseLocation();
    
    /**
     * Returns location of library
     * @return Location of library
     */
    String getLibLocation();
    
    /**
     * Returns location of templates
     * @return Location of templates
     */
    String getTemplatesLocation();
}
