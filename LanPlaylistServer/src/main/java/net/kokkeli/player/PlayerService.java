package net.kokkeli.player;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;

import com.google.inject.servlet.GuiceFilter;

import net.kokkeli.ISettings;
import net.kokkeli.data.ILogger;
import net.kokkeli.data.LogSeverity;
import net.kokkeli.data.db.IConnectionStorage;
import net.kokkeli.player.webservice.PlayerServletConfig;

public class PlayerService {
    private Server server;
    
    private final ILogger logger;
    private boolean running = true;
    
    public PlayerService(ISettings settings, IConnectionStorage storage, ILogger logger, IPlayer player) {
        this.logger = logger;
        
        server = new Server(settings.getPlayerServicePort());
        ServletContextHandler sch = new ServletContextHandler(server, "/");
        sch.addEventListener(new PlayerServletConfig(settings, storage, player));
        sch.addFilter(GuiceFilter.class, "/*", null);
        sch.addServlet(DefaultServlet.class, "/");
    }
    
    public void start() {
        try {
            server.start();
        } catch (Exception e) {
            logger.log("Thread interrupted", LogSeverity.ERROR);
        }
    }
    
    public void stop(){
        running = false;
        try {
            server.stop();
        } catch (Exception e) {
            logger.log("Something went wrong when stopping service", LogSeverity.ERROR);
        }
    }
    
    public boolean isRunning(){
        return running;
    }
}
