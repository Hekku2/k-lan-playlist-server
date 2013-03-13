package net.kokkeli.server;

import java.io.File;
import java.net.URI;

import javax.ws.rs.core.UriBuilder;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;

import net.kokkeli.ISettings;
import net.kokkeli.data.ILogger;
import net.kokkeli.data.LogSeverity;
import net.kokkeli.data.LoggingModule;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceFilter;

/**
 * Lan server class
 * @author Hekku2
 *
 */
public class LanServer {
    private static final String URL = "http://localhost/";
    private static final int PORT = 9998;
    private static final URI BASE_URI = getBaseURI();
    private final ILogger logger;
    private final ISettings settings;
    
    private Server server;
    
    /**
     * Creates server 
     * @throws ServerException thrown if there is problem width server.
     */
    public LanServer() throws ServerException{
        Injector injector = Guice.createInjector(new LoggingModule());
        logger = injector.getInstance(ILogger.class);
        
        settings = injector.getInstance(ISettings.class);
        
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
        sch.addEventListener(new LanServletConfig());
        sch.addFilter(GuiceFilter.class, "/*", null);
        sch.addServlet(DefaultServlet.class, "/");  
        try {
            server.start();
            
            logger.log("Server started at " + BASE_URI, LogSeverity.TRACE);
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
    }
    
    /**
     * Build base uri for localhost.
     * @return Base uri for localhost
     */
    public static URI getBaseURI() {
        return UriBuilder.fromUri(URL).port(PORT).build();
    }
    
    /**
     * Builds uri with base uri and end part.
     * @param endPart End part
     * @return Uri
     */
    public static URI getURI(String endPart){
        return UriBuilder.fromUri(URL + endPart).port(PORT).build();
    }
}
