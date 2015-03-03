package net.kokkeli.data.dto;

import net.kokkeli.data.db.IConnectionStorage;
import net.kokkeli.data.db.ILogDatabase;
import net.kokkeli.data.db.LogDatabase;
import net.kokkeli.settings.ISettings;

import com.google.inject.AbstractModule;

/**
 * Logging module
 * @author Hekku2
 *
 */
public class LoggingModule extends AbstractModule {
    private ISettings settings;
    private IConnectionStorage storage;
    
    /**
     * Creates logging module.
     * @param settings Settings to be used.
     */
    public LoggingModule(ISettings settings, IConnectionStorage storage) {
        this.settings = settings;
        this.storage = storage;
    }

    /**
     * Configures annotation for logging module.
     */
    @Override
    protected void configure() {
        bind(ISettings.class).toInstance(settings);
        bind(IConnectionStorage.class).toInstance(storage);
        bind(ILogDatabase.class).to(LogDatabase.class);
        bind(ILogger.class).to(Logging.class);
    }
}
