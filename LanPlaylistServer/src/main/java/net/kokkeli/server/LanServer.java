package net.kokkeli.server;

import java.io.IOException;
import java.net.URI;

import javax.ws.rs.core.UriBuilder;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;

import net.kokkeli.data.Logging;
import net.kokkeli.resources.authentication.AuthenticationModule;

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
    private static final String TEMPLATE_LOCATION = "target\\classes\\net\\kokkeli\\resources\\views";
    
    private Server server;
    
    /**
     * Creates server 
     * @throws ServerException thrown if there is problem width server.
     */
    public LanServer() throws ServerException{
        try {
            Injector injector = Guice.createInjector(new AuthenticationModule());
            injector.injectMembers(this);
            
            Templates.initialize(TEMPLATE_LOCATION);
        } catch (IOException e) {
            throw new ServerException("Templates could not be found: " + e.getMessage());
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
            
            Logging.Log("Server started at " + BASE_URI, 1);
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
        Logging.Log("Shutting down server.", 1);
        if (server != null){
            server.stop();
        }
        Logging.Log("Server shut down.", 1);
    }
    
    /**
     * Build base uri for localhost.
     * @return Base uri for localhost
     */
    public static URI getBaseURI() {
        return UriBuilder.fromUri(URL).port(PORT).build();
    }
}
