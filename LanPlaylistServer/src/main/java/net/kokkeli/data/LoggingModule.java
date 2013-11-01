package net.kokkeli.data;

import com.google.inject.AbstractModule;

/**
 * Logging module
 * @author Hekku2
 *
 */
public class LoggingModule extends AbstractModule {
    /**
     * Configures annotation for logging module.
     */
    @Override
    protected void configure() {
        bind(ILogger.class).to(Logging.class);
    }
}
