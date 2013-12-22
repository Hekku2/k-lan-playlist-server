package net.kokkeli.data.services;

import java.util.Collection;

import net.kokkeli.data.LogRow;

/**
 * Service interface for viewing the log rows.
 * @author Hekku2
 */
public interface ILogService {
    
    /**
     * Returns all log rows.
     * @return Log rows
     * @throws ServiceException Thrown if there is a problem with the database
     */
    Collection<LogRow> get() throws ServiceException;
}
