package net.kokkeli.server;

import net.kokkeli.data.ILogger;
import net.kokkeli.data.LoggingModule;
import net.kokkeli.resources.Access;
import net.kokkeli.resources.CssResource;
import net.kokkeli.resources.RootResource;
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
   @Override
   protected Injector getInjector() {
      return Guice.createInjector(new JerseyServletModule() {
         @Override
         protected void configureServlets() {
            Injector injector = Guice.createInjector(new LoggingModule());
            bind(CssResource.class);
            bind(RootResource.class);
            bind(AuthenticationResource.class);
            
            bindInterceptor(Matchers.any(), 
                    Matchers.annotatedWith(Access.class), new AuthenticationInceptor(injector.getInstance(ILogger.class)));
             
            // Route all requests through GuiceContainer
            serve("/*").with(GuiceContainer.class);
         }
      });
   }
}