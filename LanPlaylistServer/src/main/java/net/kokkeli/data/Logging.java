package net.kokkeli.data;

import java.util.Date;

import com.google.inject.Inject;

import net.kokkeli.ISettings;
import net.kokkeli.data.db.DatabaseException;
import net.kokkeli.data.db.ILogDatabase;

/**
 * Logging class.
 * @author Hekku2
 *
 */
public class Logging implements ILogger {
    private final ILogDatabase logDatabase;
    private final ISettings settings;
    private final String source;
    
    /**
     * Creates logging
     * @param settings
     * @param logDatabase
     */
    @Inject
    public Logging(String source, ISettings settings, ILogDatabase logDatabase){
        this.logDatabase = logDatabase;
        this.settings = settings;
        this.source = source;
    }
    
    /**
     * Writes given message to log.
     * @param message Message to write
     * @param severity Severity of message
     */
    @Override
    public void log(String message, LogSeverity severity){
        if (severity.getSeverity() < settings.getLogSeverity().getSeverity())
            return;
        
        //TODO Make async, so logging doesnt slow down usage so much.
        LogRow row = new LogRow();
        row.setMessage(message);
        row.setSeverity(severity);
        row.setSource(source);
        row.setTimestamp(new Date());
        System.out.println(severity + ": " + message);
        try {
            logDatabase.add(row);
        } catch (DatabaseException e) {
            System.out.println("Logging failed: ");
            e.printStackTrace();
        }
    }
}
