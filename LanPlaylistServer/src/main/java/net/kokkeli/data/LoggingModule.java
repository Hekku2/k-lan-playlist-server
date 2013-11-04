package net.kokkeli.data;

import net.kokkeli.ISettings;
import net.kokkeli.data.db.ILogDatabase;
import net.kokkeli.data.db.LogDatabase;

import com.google.inject.AbstractModule;

/**
 * Logging module
 * @author Hekku2
 *
 */
public class LoggingModule extends AbstractModule {
    private ISettings settings;
    
    /**
     * Creates logging module.
     * @param settings Settings to be used.
     */
    public LoggingModule(ISettings settings) {
        this.settings = settings;
    }

    /**
     * Configures annotation for logging module.
     */
    @Override
    protected void configure() {
        bind(ISettings.class).toInstance(settings);
        bind(ILogDatabase.class).to(LogDatabase.class);
        bind(ILogger.class).to(Logging.class);
    }
}
