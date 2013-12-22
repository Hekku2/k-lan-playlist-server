package net.kokkeli.data.services;

import java.util.Collection;

import com.google.inject.Inject;

import net.kokkeli.data.ILogger;
import net.kokkeli.data.LogRow;
import net.kokkeli.data.LogSeverity;
import net.kokkeli.data.db.DatabaseException;
import net.kokkeli.data.db.ILogDatabase;

/**
 * Log service class implementing log service. Used for viewing logs
 * @author Hekku2
 */
public class LogService implements ILogService {
    private ILogDatabase logDatabase;
    private ILogger logger;
    
    /**
     * Creates a new log service
     * @param logger Logger
     * @param logDatabase LogDatabase
     */
    @Inject
    public LogService(ILogger logger, ILogDatabase logDatabase){
        this.logDatabase = logDatabase;
        this.logger = logger;
    }
    
    /**
     * Returns all log rows.
     * @return Log rows
     * @throws ServiceException Thrown if there is a problem with the database
     */
    @Override
    public Collection<LogRow> get() throws ServiceException {
        try {
            return logDatabase.get();
        } catch (DatabaseException e) {
            String message = "Could not fetch log rows from database.";
            logger.log(message, LogSeverity.ERROR);
            throw new ServiceException(message, e);
        }
    }

}
