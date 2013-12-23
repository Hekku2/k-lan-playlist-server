package net.kokkeli.server;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;

import net.kokkeli.ISettings;
import net.kokkeli.Settings;
import net.kokkeli.data.ILogger;
import net.kokkeli.data.LogSeverity;
import net.kokkeli.data.LoggingModule;

import com.almworks.sqlite4java.SQLite;
import com.almworks.sqlite4java.SQLiteQueue;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceFilter;

/**
 * Lan server class
 * @author Hekku2
 *
 */
public class LanServer {
    private static final int PORT = 9998;
    private final ILogger logger;
    private final ISettings settings;
    private final IFileSystem filesystem;
    private final SQLiteQueue queue;
    
    private Server server;
    
    /**
     * Creates server 
     * @throws ServerException thrown if there is problem width server.
     */
    public LanServer(String settingsFile) throws ServerException{
        filesystem = new FileSystem();
        settings = new Settings();
        try {
            settings.loadSettings(settingsFile);
        } catch (IOException | IllegalArgumentException e) {
            System.out.println(e.toString());
            throw new ServerException("Settings file " + settingsFile + " is missings or invalid format!");
        }
        
        SQLite.setLibraryPath(settings.getLibLocation());
        Logger.getLogger("com.almworks.sqlite4java").setLevel(Level.OFF);
        queue = new SQLiteQueue(new File(settings.getDatabaseLocation()));
        queue.start();
        
        Injector injector = Guice.createInjector(new LoggingModule(settings));
        logger = injector.getInstance(ILogger.class);
        
        if (!filesystem.fileExists(settings.getDatabaseLocation())){
            throw new ServerException(String.format("Databasefile did not exist: %s", settings.getDatabaseLocation()));
        }
        
        File trackFolder = new File(settings.getTracksFolder());

        if (!trackFolder.exists()){
              logger.log("creating directory: " + settings.getTracksFolder(), LogSeverity.TRACE);
              if(!trackFolder.mkdir())
                  throw new ServerException("Can't create folder.");
        }
    }
    
    /**
     * Creates new HttpServer and starts it.
     * @throws ServerException Thrown if server cannot be started.
     */
    public void start() throws ServerException{
        server = new Server(PORT);
        ServletContextHandler sch = new ServletContextHandler(server, "/");
        sch.addEventListener(new LanServletConfig(settings,queue));
        sch.addFilter(GuiceFilter.class, "/*", null);
        sch.addServlet(DefaultServlet.class, "/");
        try {
            server.start();
            
            logger.log("Server started at " + settings.getBaseURI(), LogSeverity.TRACE);
        } catch (InterruptedException e) {
            throw new ServerException("Unable to start server: " + e.getMessage());
        } catch (Exception e) {
            throw new ServerException("Unable to start server: " + e.getMessage());
        }
    }
    
    /**
     * Stops the server
     * @throws Exception 
     * @throws ServerException Thrown if service was not started.
     */
    public void stop() throws Exception {
        logger.log("Shutting down server.", LogSeverity.TRACE);
        if (server != null){
            server.stop();
        }
        logger.log("Server shut down.", LogSeverity.TRACE);
        if (queue != null){
            queue.stop(true);
        }
    }
}
