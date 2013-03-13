package net.kokkeli.server;

import net.kokkeli.ISettings;
import net.kokkeli.data.ILogger;
import net.kokkeli.data.Logging;
import net.kokkeli.data.db.IPlaylistDatabase;
import net.kokkeli.data.db.IUserDatabase;
import net.kokkeli.data.db.PlaylistDatabase;
import net.kokkeli.data.db.UserDatabase;
import net.kokkeli.data.services.IPlaylistService;
import net.kokkeli.data.services.ISessionService;
import net.kokkeli.data.services.IUserService;
import net.kokkeli.data.services.PlaylistService;
import net.kokkeli.data.services.SessionService;
import net.kokkeli.data.services.UserService;
import net.kokkeli.player.IPlayer;
import net.kokkeli.player.MockPlayer;
import net.kokkeli.resources.Access;
import net.kokkeli.resources.StaticResources;
import net.kokkeli.resources.IndexResource;
import net.kokkeli.resources.PlaylistsResource;
import net.kokkeli.resources.RenderExceptionMapper;
import net.kokkeli.resources.RootResource;
import net.kokkeli.resources.ServiceExceptionMapper;
import net.kokkeli.resources.UsersResource;
import net.kokkeli.resources.authentication.AuthenticationInceptor;
import net.kokkeli.resources.authentication.AuthenticationResource;

import com.google.inject.Guice;
import com.google.inject.Injector;
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
                
                //Services
                bind(ISessionService.class).to(SessionService.class).asEagerSingleton();
                bind(ITemplateService.class).to(Templates.class);
                bind(IPlayer.class).to(MockPlayer.class);
                bind(IUserService.class).to(UserService.class);
                bind(IPlaylistService.class).to(PlaylistService.class);
                bind(IFileSystem.class).to(FileSystem.class);

                //Resources
                bind(StaticResources.class);
                bind(RootResource.class);
                bind(UsersResource.class);
                bind(AuthenticationResource.class);
                bind(IndexResource.class);
                bind(PlaylistsResource.class);
                
                //Aspects
                AuthenticationInceptor interceptor = new AuthenticationInceptor();
                requestInjection(interceptor);
                
                bindInterceptor(Matchers.any(), 
                    Matchers.annotatedWith(Access.class), interceptor);
             
                
                // Route all requests through GuiceContainer
                serve("/*").with(GuiceContainer.class);
            }
        });
    }
}