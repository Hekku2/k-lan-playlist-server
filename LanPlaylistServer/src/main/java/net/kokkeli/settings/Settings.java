package net.kokkeli.settings;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;

import javax.ws.rs.core.UriBuilder;

import net.kokkeli.data.dto.LogSeverity;

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
    private String serverUri;
    private String resourcesFolder;
    private String youtubeDlFolder;
    private LogSeverity logSeverity = LogSeverity.TRACE;
    private int port;
    private int playerServicePort;
    private String playerServiceUri;
    private boolean playerEnabled;
    private boolean requireAuthentication;
    
    /**
     * Loads settings from file
     * @param file File
     * @throws IOException
     * @throws SettingsParseException 
     */
    @Override
    public void loadSettings(String file) throws IOException, IllegalArgumentException, SettingsParseException{
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
                    throw new SettingsParseException("File format is invalid. Broken line: " + line);
                
                String[] splitted = line.split(SPLITTER + "");
                
                if (splitted.length != 2)
                    throw new SettingsParseException("File format is invalid. Broken line: " + line);
                
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
    @Override
    public String getDatabaseLocation(){
        return databaseLocation;
    }
    
    /**
     * Returns location of library
     * @return Location of library
     */
    @Override
    public String getLibLocation(){
        return libLocation;
    }

    /**
     * Returns location of templates
     * @return Location of templates
     */
    @Override
    public String getTemplatesLocation() {
        return templatesLocation;
    }
    
    /**
     * Returns location of tracks
     * @return location of tracks.
     */
    @Override
    public String getTracksFolder() {
        return tracksFolder;
    }

    /**
     * Returns password salt
     * @return password salt
     */
    @Override
    public String getPasswordSalt() {
        return passwordSalt;
    }
    
    /**
     * Returns location of VLC
     * @return location of vlc
     */
    @Override
    public String getVlcLocation() {
        return vlcLocation;
    }
    
    /**
     * Returns uri of server
     * @return uri of server
     */
    @Override
    public String getServerUri() {
        return serverUri;
    }
    
    /**
     * Returns the port of the server
     * @return Server port
     */
    public int getServerPort(){
        return port;
    }
    
    /**
     * Build base uri for localhost.
     * @return Base uri for localhost
     */
    @Override
    public URI getBaseURI() {
        return UriBuilder.fromUri(serverUri).port(port).build();
    }
    
    /**
     * Builds uri with base uri and end part.
     * @param endPart End part
     * @return Uri
     */
    @Override
    public URI getURI(String endPart){
        return UriBuilder.fromUri(serverUri + endPart).port(port).build();
    }
    
    @Override
    public String getResourcesFolder() {
        return resourcesFolder;
    }
    
    @Override
    public LogSeverity getLogSeverity() {
        return logSeverity;
    }

    @Override
    public boolean getPlayerEnabled() {
        return playerEnabled;
    }
    
    @Override
    public boolean getRequireAuthentication() {
        return requireAuthentication;
    }
    
    @Override
    public String getYoutubeDlLocation() {
        return youtubeDlFolder;
    }

    @Override
    public int getPlayerServicePort() {
        return playerServicePort;
    }
    
    @Override
    public String getPlayerServiceUri() {
        return playerServiceUri;
    }
    
    /**
     * Loads settings
     * @param key Key
     * @param value Value
     * @throws IOException Thrown if there was no such key.
     */
    private void loadSetting(String key, String value) throws SettingsParseException{
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
        case "ServerUri":
            serverUri = value;
            break;
        case "Port":
            port = Integer.parseInt(value);
            break;
        case "ResourcesFolder":
            resourcesFolder = value;
            break;
        case "PlayerEnabled":
            playerEnabled = Boolean.parseBoolean(value);
            break;
        case "RequireAuthentication":
            requireAuthentication = Boolean.parseBoolean(value);
            break;
        case "LogSeverity":
            int parsed = Integer.parseInt(value);
            logSeverity = LogSeverity.getSeverity(parsed);
            break;
        case "YoutubeDlLocation":
            youtubeDlFolder = value;
            break;
        case "PlayerServicePort":
            playerServicePort = Integer.parseInt(value);
            break;
        case "PlayerServiceUri":
            playerServiceUri = value;
            break;
        default:
            throw new SettingsParseException(String.format("Not recognized setting name: \"%s\". Value was \"%s\"", key, value));
        }
    }
}
