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
}
