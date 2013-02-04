package net.kokkeli.data;

import net.kokkeli.ISettings;
import net.kokkeli.Settings;
import net.kokkeli.data.services.ISessionService;
import net.kokkeli.data.services.SessionService;

import com.google.inject.AbstractModule;

public class LoggingModule extends AbstractModule {
    /**
     * Configures annotation to match authentication inceptor
     */
    protected void configure() {
        bind(ILogger.class).to(Logging.class);
        bind(ISettings.class).to(Settings.class);
        bind(ISessionService.class).to(SessionService.class);
    }
}
