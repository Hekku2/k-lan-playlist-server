package net.kokkeli.player.webservice;

import java.util.HashMap;
import java.util.Map;

import net.kokkeli.data.db.IConnectionStorage;
import net.kokkeli.data.db.ILogDatabase;
import net.kokkeli.data.db.IPlaylistDatabase;
import net.kokkeli.data.db.LogDatabase;
import net.kokkeli.data.db.PlaylistDatabase;
import net.kokkeli.data.dto.ILogger;
import net.kokkeli.data.dto.Logging;
import net.kokkeli.data.services.IPlaylistService;
import net.kokkeli.data.services.PlaylistService;
import net.kokkeli.player.IPlayer;
import net.kokkeli.settings.ISettings;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.sun.jersey.guice.JerseyServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

public class PlayerServletConfig extends GuiceServletContextListener{
    private final ISettings settings;
    private final IConnectionStorage storage;
    private final IPlayer player;
    
    /**
     * Creates a new lan servet config instance
     * @param settings Settings file given to resources and services.
     * @param queue Database queue 
     */
    public PlayerServletConfig(ISettings settings, IConnectionStorage storage, IPlayer player) {
        this.settings = settings;
        this.storage = storage;
        this.player = player;
    }

    @Override
    protected Injector getInjector() {
        return Guice.createInjector(new JerseyServletModule() {
            @Override
            protected void configureServlets() {
                //Settings
                bind(ISettings.class).toInstance(settings);
                bind(IConnectionStorage.class).toInstance(storage);
                
                //Databases
                bind(IPlaylistDatabase.class).to(PlaylistDatabase.class).asEagerSingleton();
                bind(ILogDatabase.class).to(LogDatabase.class).asEagerSingleton();
                
                //Logging
                bind(ILogger.class).to(Logging.class);
                
                //Services
                bind(IPlaylistService.class).to(PlaylistService.class);
                bind(IPlayer.class).toInstance(player);
                
                //Resources
                bind(PlayerWebservice.class);
                
                Map<String, String> initParams = new HashMap<String, String>();
                initParams.put("com.sun.jersey.api.json.POJOMappingFeature", "true");
                serve("/*").with(GuiceContainer.class, initParams);
            }
        });
    }
}
