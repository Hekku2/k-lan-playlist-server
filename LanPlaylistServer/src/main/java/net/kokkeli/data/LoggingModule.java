package net.kokkeli.data;

import net.kokkeli.ISettings;
import net.kokkeli.Settings;
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
    protected void configure() {
        bind(ILogger.class).to(Logging.class);
        bind(ISettings.class).to(Settings.class);
    }
}
