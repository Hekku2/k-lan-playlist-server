package net.kokkeli.data.services;

import java.util.Collection;

import com.google.inject.Inject;

import net.kokkeli.data.FetchRequest;
import net.kokkeli.data.ILogger;
import net.kokkeli.data.LogSeverity;
import net.kokkeli.data.db.DatabaseException;
import net.kokkeli.data.db.IFetchRequestDatabase;

/**
 * Service for fetch requets
 * @author Hekku2
 *
 */
public class FetchRequestService implements IFetchRequestService {
    private final ILogger logger;
    private final IFetchRequestDatabase fetchRequestDatabase;
    
    /**
     * Creates fetch request service
     * @param logger Logger
     * @param fetchRequestDatabase Database containing fetch requests
     */
    @Inject
    public FetchRequestService(ILogger logger, IFetchRequestDatabase fetchRequestDatabase){
        this.logger = logger;
        this.fetchRequestDatabase = fetchRequestDatabase;
    }
    
    @Override
    public Collection<FetchRequest> get() throws ServiceException {
        try {
            return fetchRequestDatabase.get();
        } catch (DatabaseException e) {
            logger.log("Something when wrong with the database while fetching fetch requests.", LogSeverity.ERROR);
            throw new ServiceException("Something when wrong with the database while fetching fetch requests.", e);
        }
    }

}
