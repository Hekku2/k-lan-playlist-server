package net.kokkeli;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Class for settings
 * @author Hekku2
 *
 */
public class Settings implements ISettings {
    private static final char COMMENT_LINE = '#';
    private static final char SPLITTER = '=';
    
    private String databaseLocation;
    private String libLocation;
    private String templatesLocation;
    private String tracksFolder;
    private String passwordSalt;
    private String vlcLocation;
    
    /**
     * Loads settings from file
     * @param file File
     * @throws IOException
     */
    public void loadSettings(String file) throws IOException{
        DataInputStream in = null;
        BufferedReader br = null;
        
        try {
            FileInputStream fstream = new FileInputStream(file);

            in = new DataInputStream(fstream);
            br = new BufferedReader(new InputStreamReader(in));
            
            String line;
            while ((line = br.readLine()) != null)   {
                if (line.length() <= 0 || line.charAt(0) == COMMENT_LINE)
                    continue;
                
                int split = line.indexOf(SPLITTER);
                if (split < 1)
                    throw new IOException("File format is invalid. Broken line: " + line);
                
                String[] splitted = line.split(SPLITTER + "");
                
                if (splitted.length != 2)
                    throw new IOException("File format is invalid. Broken line: " + line);
                
                loadSetting(splitted[0], splitted[1]);
            }

            in.close();
        } finally {
            if (in != null)
                in.close();
            if (br != null)
                br.close();
        }

    }
    
    /**
     * Location of database.
     * @return Location of database.
     */
    public String getDatabaseLocation(){
        return databaseLocation;
    }
    
    /**
     * Returns location of library
     * @return Location of library
     */
    public String getLibLocation(){
        return libLocation;
    }

    /**
     * Returns location of templates
     * @return Location of templates
     */
    public String getTemplatesLocation() {
        return templatesLocation;
    }
    
    /**
     * Returns location of tracks
     * @return location of tracks.
     */
    public String getTracksFolder() {
        return tracksFolder;
    }

    /**
     * Returns password salt
     * @return password salt
     */
    public String getPasswordSalt() {
        return passwordSalt;
    }
    
    public String getVlcLocation() {
        return vlcLocation;
    }
    
    private void loadSetting(String key, String value) throws IOException{
        switch (key) {
        case "DatabaseLocation":
            databaseLocation = value;
            break;
        case "LibLocation":
            libLocation = value;
            break;
        case "TemplatesLocation":
            templatesLocation = value;
            break;
        case "TracksFolder":
            tracksFolder = value;
            break;
        case "PasswordSalt":
            passwordSalt = value;
            break;
        case "VlcLocation":
            vlcLocation = value;
            break;
        default:
            throw new IOException("Invalid setting: " + key);
        }
    }
}
