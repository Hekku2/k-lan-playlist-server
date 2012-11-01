package net.kokkeli.server;

import java.io.IOException;
import java.net.URI;

import javax.ws.rs.core.UriBuilder;

import org.glassfish.grizzly.http.server.HttpServer;

import com.sun.jersey.api.container.grizzly2.GrizzlyServerFactory;
import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.api.core.ResourceConfig;

/**
 * Lan server class
 * @author Hekku2
 *
 */
public class LanServer {
    private static final String URL = "http://localhost/";
    private static final int PORT = 9998;
    private static final String ROUTING_RESOURCES_LOCATION = "net.kokkeli.resources";
    private static final URI BASE_URI = getBaseURI();
    private static final String TEMPLATE_LOCATION = "target\\classes\\net\\kokkeli\\resources\\views";
    
    private HttpServer server;
    private ResourceConfig resourceConfig = new PackagesResourceConfig(ROUTING_RESOURCES_LOCATION);
    
    /**
     * Creates server 
     * @throws ServerException thrown if there is problem width server.
     */
    public LanServer() throws ServerException{
        try {
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
        try {
            server = GrizzlyServerFactory.createHttpServer(BASE_URI, resourceConfig);
        } catch (IOException e) {
            throw new ServerException(e.toString());
        }
    }
    
    /**
     * Stops the server
     * @throws ServerException Thrown if service was not started.
     */
    public void stop() {
        if (server != null){
            server.stop();
        }
    }
    
    /**
     * Build base uri for localhost.
     * @return Base uri for localhost
     */
    private static URI getBaseURI() {
        return UriBuilder.fromUri(URL).port(PORT).build();
    }
}
