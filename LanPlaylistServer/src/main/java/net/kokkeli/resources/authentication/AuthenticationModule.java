package net.kokkeli.resources.authentication;

import net.kokkeli.resources.Access;

import com.google.inject.AbstractModule;
import com.google.inject.matcher.Matchers;

/**
 * Class for authentication module
 * @author Hekku2
 *
 */
public class AuthenticationModule extends AbstractModule{
    
    /**
     * Configures annotation to match authentication inceptor
     */
    protected void configure() {
        
        bindInterceptor(Matchers.any(), Matchers.annotatedWith(Access.class), 
            new AuthenticationInceptor());
    }
}
