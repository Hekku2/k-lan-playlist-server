package net.kokkeli;

import java.io.IOException;
import java.net.URI;

import net.kokkeli.data.LogSeverity;

public interface ISettings {
    
    /**
     * Loads settings from file
     * @param file File
     * @throws IOException
     */
    void loadSettings(String file) throws IOException;
    
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
    
    /**
     * Returns location of tracks
     * @return Locaiton of tracks
     */
    String getTracksFolder();
    
    /**
     * Returns password salt
     * @return Salt
     */
    String getPasswordSalt();
    
    /**
     * Returns location of VLC-player
     * @return VLC location
     */
    String getVlcLocation();
    
    /**
     * Returns uri of server
     * @return URI of server
     */
    String getServerUri();
    
    /**
     * Returns base uri
     * @returnBase uri
     */
    URI getBaseURI();
    
    /**
     * Uri of server
     * @param endPart
     * @return
     */
    URI getURI(String endPart);

    /**
     * Returns folder of resources
     * @return Folder of resources
     */
    String getResourcesFolder();

    /**
     * Returns log severity.
     * @return Log severity
     */
    LogSeverity getLogSeverity();
    
    boolean getPlayerEnabled();
}
