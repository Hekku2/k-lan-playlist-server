package net.kokkeli.resources.authentication;

import net.kokkeli.data.ILogger;
import net.kokkeli.data.LoggingModule;

import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provider;

/**
 * Logging module tests
 * @author Hekku2
 */
public class TestLoggingModule {
    private Provider<ILogger> testLogger;
    
    @Test
    public void testLoggingModuleCreation() throws Throwable {
        Injector injector = Guice.createInjector(new LoggingModule());
        testLogger = injector.getProvider(ILogger.class);
        
        testLogger.get();
    }
    
}
