package net.kokkeli.server;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;

import net.kokkeli.ISettings;
import net.kokkeli.data.ILogger;
import net.kokkeli.data.LogSeverity;
import net.kokkeli.data.LoggingModule;
import net.kokkeli.data.db.IConnectionStorage;
import net.kokkeli.data.db.SqliteConnectionStorage;

import com.almworks.sqlite4java.SQLite;
import com.google.inject.CreationException;
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
    private final IConnectionStorage storage;
    private final IFileSystem filesystem;
    
    private Server server;
    
    /**
     * Creates server 
     * @throws ServerException thrown if there is problem width server.
     */
    public LanServer(ISettings settings) throws ServerException{
        filesystem = new FileSystem();
        this.settings = settings;
        
        SQLite.setLibraryPath(settings.getLibLocation());
        Logger.getLogger("com.almworks.sqlite4java").setLevel(Level.OFF);
        
        storage = new SqliteConnectionStorage("jdbc:sqlite:" + settings.getDatabaseLocation());
        
        Injector injector = Guice.createInjector(new LoggingModule(settings, storage));
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
        sch.addEventListener(new LanServletConfig(settings, storage));
        sch.addFilter(GuiceFilter.class, "/*", null);
        sch.addServlet(DefaultServlet.class, "/");
        try {
            server.start();
            logger.log("Server started at " + settings.getBaseURI(), LogSeverity.TRACE);
        } catch (InterruptedException e) {
            throw new ServerException("Unable to start server: " + e.getMessage());
        } catch (CreationException e){
            if (e.getCause().getClass() == UnsatisfiedLinkError.class && e.getCause().getMessage().contains("Unable to load library 'libvlc'"))
                throw new ServerException("Unable to locate VLC libraries. Probably caused by invalid VlcLocation-setting.");
            throw new ServerException("Creating injector failed: " + e.getMessage());
        } catch (Exception e) {
            throw new ServerException("Unrecognized exception: Unable to start server: " + e.getMessage());
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
    }
}
