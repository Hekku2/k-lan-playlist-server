package net.kokkeli.server;

import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.codehaus.jackson.map.ObjectMapper;

import net.kokkeli.ISettings;
import net.kokkeli.data.ILogger;
import net.kokkeli.data.Logging;
import net.kokkeli.data.db.FetchRequestDatabase;
import net.kokkeli.data.db.IFetchRequestDatabase;
import net.kokkeli.data.db.ILogDatabase;
import net.kokkeli.data.db.IPlaylistDatabase;
import net.kokkeli.data.db.ITrackDatabase;
import net.kokkeli.data.db.IUserDatabase;
import net.kokkeli.data.db.LogDatabase;
import net.kokkeli.data.db.PlaylistDatabase;
import net.kokkeli.data.db.TrackDatabase;
import net.kokkeli.data.db.UserDatabase;
import net.kokkeli.data.services.FetchRequestService;
import net.kokkeli.data.services.IFetchRequestService;
import net.kokkeli.data.services.IPlaylistService;
import net.kokkeli.data.services.ISessionService;
import net.kokkeli.data.services.ITrackService;
import net.kokkeli.data.services.IUserService;
import net.kokkeli.data.services.PlaylistService;
import net.kokkeli.data.services.SessionService;
import net.kokkeli.data.services.TrackService;
import net.kokkeli.data.services.UserService;
import net.kokkeli.player.IPlayer;
import net.kokkeli.player.VlcPlayer;
import net.kokkeli.resources.Access;
import net.kokkeli.resources.FetchRequestsResource;
import net.kokkeli.resources.ManagementResource;
import net.kokkeli.resources.StaticResources;
import net.kokkeli.resources.IndexResource;
import net.kokkeli.resources.PlaylistsResource;
import net.kokkeli.resources.RenderExceptionMapper;
import net.kokkeli.resources.RootResource;
import net.kokkeli.resources.ServiceExceptionMapper;
import net.kokkeli.resources.TracksResource;
import net.kokkeli.resources.UsersResource;
import net.kokkeli.resources.authentication.AuthenticationInceptor;
import net.kokkeli.resources.authentication.AuthenticationResource;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import com.google.inject.matcher.Matchers;
import com.google.inject.servlet.GuiceServletContextListener;
import com.sun.jersey.guice.JerseyServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

/**
 * Config for servlet. All new resources should be added to injector
 * @author Hekku2
 * @version 15.11.2012
 */
public class LanServletConfig extends GuiceServletContextListener {
    private ISettings settings;
    
    public LanServletConfig(ISettings settings) {
        this.settings = settings;
    }

    @Override
    protected Injector getInjector() {
        return Guice.createInjector(new JerseyServletModule() {
            @Override
            protected void configureServlets() {
                //Settings
                bind(ISettings.class).toInstance(settings);
                
                //Exceptions
                bind(RenderExceptionMapper.class).asEagerSingleton();
                bind(ServiceExceptionMapper.class).asEagerSingleton();
                
                //Logging
                bind(ILogger.class).to(Logging.class);
                
                //Database
                bind(IPlaylistDatabase.class).to(PlaylistDatabase.class).asEagerSingleton();
                bind(IUserDatabase.class).to(UserDatabase.class).asEagerSingleton();
                bind(ITrackDatabase.class).to(TrackDatabase.class).asEagerSingleton();
                bind(IFetchRequestDatabase.class).to(FetchRequestDatabase.class).asEagerSingleton();
                bind(ILogDatabase.class).to(LogDatabase.class).asEagerSingleton();
                
                //Services
                bind(ISessionService.class).to(SessionService.class).asEagerSingleton();
                bind(ITemplateService.class).to(Templates.class);
                bind(IPlayer.class).to(VlcPlayer.class).asEagerSingleton();
                bind(IUserService.class).to(UserService.class);
                bind(IPlaylistService.class).to(PlaylistService.class);
                bind(IFileSystem.class).to(FileSystem.class);
                bind(ITrackService.class).to(TrackService.class);
                bind(IFetchRequestService.class).to(FetchRequestService.class);
                

                //Resources
                bind(StaticResources.class);
                bind(RootResource.class);
                bind(UsersResource.class);
                bind(AuthenticationResource.class);
                bind(IndexResource.class);
                bind(PlaylistsResource.class);
                bind(ManagementResource.class);
                bind(TracksResource.class);
                bind(FetchRequestsResource.class);
                
                //Aspects
                AuthenticationInceptor interceptor = new AuthenticationInceptor();
                requestInjection(interceptor);
                
                bindInterceptor(Matchers.any(), 
                    Matchers.annotatedWith(Access.class), interceptor);
             
                Map<String, String> initParams = new HashMap<String, String>();
                initParams.put("com.sun.jersey.config.feature.Trace", "true");
                initParams.put("com.sun.jersey.api.json.POJOMappingFeature", "true");
                
                //JSON mapping
                bind(ObjectMapper.class).toProvider(ObjectMapperProvider.class).in(Singleton.class);
                bind(JacksonJsonProvider.class).toProvider(JacksonJsonProviderProvider.class).in(Singleton.class);
                
                // Route all requests through GuiceContainer
                serve("/*").with(GuiceContainer.class, initParams);
            }
        });
    }
}