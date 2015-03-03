package net.kokkeli.ripservice;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.almworks.sqlite4java.SQLite;

import net.kokkeli.data.db.FetchRequestDatabase;
import net.kokkeli.data.db.IFetchRequestDatabase;
import net.kokkeli.data.db.LogDatabase;
import net.kokkeli.data.db.SqliteConnectionStorage;
import net.kokkeli.data.dto.ILogger;
import net.kokkeli.data.dto.LogSeverity;
import net.kokkeli.data.dto.Logging;
import net.kokkeli.settings.ISettings;
import net.kokkeli.settings.Settings;
import net.kokkeli.settings.SettingsParseException;

/**
 * Program running rippers
 * @author Hekku2
 *
 */
public class Program {
    
    /**
     * @param args
     * @throws InterruptedException 
     * @throws IOException 
     */
    public static void main(String[] args) throws InterruptedException, IOException {
        String settingsFile = "settings/default.dat";
        
        if (args.length > 0){
            settingsFile = args[0];
        }
        
        ISettings settings = new Settings();
        try {
            settings.loadSettings(settingsFile);
        } catch (IOException | IllegalArgumentException | SettingsParseException e) {
            System.out.println("Unable to load settings from file " + settingsFile);
            System.out.println(e.getMessage());
        }
        SQLite.setLibraryPath(settings.getLibLocation());
        SqliteConnectionStorage storage = new SqliteConnectionStorage("jdbc:sqlite:" + settings.getDatabaseLocation());
        
        IFetchRequestDatabase fetcherDatabase = new FetchRequestDatabase(storage);
        ILogger logger = new Logging("Ripper", settings, new LogDatabase(storage));
        
        //TODO Show better error mesage if file is not found.
        IFetcher ripper = new YoutubeDlRipper(settings, logger);
        ExecutorService executor = Executors.newFixedThreadPool(2);
        Runnable worker = new FetcherRunner(logger, ripper, fetcherDatabase);
        executor.execute(worker);
        
        System.out.println("Started to handle requests.");
        
        System.in.read();
        
        executor.shutdown();
        //Wait until all are handled.
        while (!executor.isTerminated()) {
            //TODO Handling for hangups
        }
        
        
        logger.log("Stopped fetch request handlers.", LogSeverity.TRACE);
    }
}
