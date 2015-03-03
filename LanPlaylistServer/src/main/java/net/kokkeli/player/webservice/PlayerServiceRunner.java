package net.kokkeli.player.webservice;

import java.io.IOException;
import java.nio.file.Paths;

import com.almworks.sqlite4java.SQLite;

import net.kokkeli.data.db.DatabaseException;
import net.kokkeli.data.db.IConnectionStorage;
import net.kokkeli.data.db.IPlaylistDatabase;
import net.kokkeli.data.db.LogDatabase;
import net.kokkeli.data.db.PlaylistDatabase;
import net.kokkeli.data.db.SqliteConnectionStorage;
import net.kokkeli.data.dto.ILogger;
import net.kokkeli.data.dto.LogSeverity;
import net.kokkeli.data.dto.Logging;
import net.kokkeli.data.services.IPlaylistService;
import net.kokkeli.data.services.PlaylistService;
import net.kokkeli.player.VlcPlayer;
import net.kokkeli.settings.ISettings;
import net.kokkeli.settings.Settings;
import net.kokkeli.settings.SettingsLoadException;
import net.kokkeli.settings.SettingsParseException;

public class PlayerServiceRunner {
    private static final long DEFAULT_SLEEP = 100;
    private static boolean running = true;
    
    private static PlayerService service;
    private static VlcPlayer player;
    
    public static void main(String[] args) throws Exception {
        String settingsFile = "settings/default.dat";

        if (args.length > 0) {
            settingsFile = args[0];
        }

        ISettings settings = loadSettings(settingsFile);
        SqliteConnectionStorage storage = createAndInitializeConnectionStorage(settings);
        ILogger logger = new Logging("Player", settings, new LogDatabase(storage));
        player = createPlayer(settings, logger, storage);
        
        service = new PlayerService(settings, storage, logger, player);
        
        logger.log("Starting service", LogSeverity.INFO);
        service.start();
        while(running){
            Thread.sleep(DEFAULT_SLEEP);
        }
    }

    private static VlcPlayer createPlayer(ISettings settings, ILogger logger, IConnectionStorage storage) throws DatabaseException{
        IPlaylistDatabase database = new PlaylistDatabase(storage);
        IPlaylistService playlistService = new PlaylistService(logger, database);
        return new VlcPlayer(settings, logger, playlistService);
    }
    
    private static ISettings loadSettings(String settingsFile) throws SettingsLoadException{
        ISettings settings = new Settings();
        try {
            settings.loadSettings(settingsFile);
        } catch (IOException | IllegalArgumentException | SettingsParseException e) {
            String path = Paths.get("").toAbsolutePath().toString();
            throw new SettingsLoadException("Problems with loading the settings. Current path: " + path, e);
        }
        return settings;
    }
    
    private static SqliteConnectionStorage createAndInitializeConnectionStorage(ISettings settings){
        SQLite.setLibraryPath(settings.getLibLocation());
        return new SqliteConnectionStorage("jdbc:sqlite:" + settings.getDatabaseLocation());
    }
    
    /**
     * Stop this service instance
     */
    public static void stop(String[] args) {
        //TODO Stopping takes a long time.
        running = false;
        player.stop();
        service.stop();
    }
}
