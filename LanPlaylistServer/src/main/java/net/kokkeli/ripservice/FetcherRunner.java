package net.kokkeli.ripservice;

import net.kokkeli.data.db.DatabaseException;
import net.kokkeli.data.db.IFetchRequestDatabase;
import net.kokkeli.data.dto.FetchRequest;
import net.kokkeli.data.dto.FetchStatus;
import net.kokkeli.data.dto.ILogger;
import net.kokkeli.data.dto.LogSeverity;

/**
 * General fetcher runner
 * @author Hekku2
 *
 */
public class FetcherRunner implements Runnable {
    private static final long DEFAULT_SLEEP = 1000;
    
    private final IFetcher fetcher;
    private final ILogger logger;
    private final IFetchRequestDatabase fetchRequestDatabase;

    /**
     * Creates fecther runner
     * @param logger Logger
     * @param fetcher Fetcher
     * @param fetchRequestDatabase Fetch request fatabase
     */
    public FetcherRunner(ILogger logger, IFetcher fetcher, IFetchRequestDatabase fetchRequestDatabase){
        this.fetcher = fetcher;
        this.logger = logger;
        this.fetchRequestDatabase = fetchRequestDatabase;
    }
    
    /**
     * Runs handling and sleeps.
     */
    @Override
    public void run() {
        try {
            while(true){
                handleItems();
                Thread.sleep(DEFAULT_SLEEP);
            }
        } catch (InterruptedException e) {
            logger.log("Thread interrupted", LogSeverity.ERROR);
        }
    }
    
    /**
     * Handles items
     */
    private void handleItems() {
        try {
            // Fetch work item from queue
            FetchRequest request = fetchRequestDatabase.oldestUnhandledFetchRequestOrNull(fetcher.getHandledType());
            if (request == null)
                return;
            
            // Mark it as being worked
            fetchRequestDatabase.updateRequest(request.getId(), FetchStatus.HANDLING);
            
            // Fetch it
            try {
                fetcher.fetch(request);
            } catch (FetchFailedException e) {
                fetchRequestDatabase.updateRequest(request.getId(), FetchStatus.ERROR);
                return;
            }
            
            //TODO Verify that fetched file exists on hard drive
            
            // Mark it as being ready
            fetchRequestDatabase.updateRequest(request.getId(), FetchStatus.HANDLED);
            logger.log(String.format("Request handled with id %s", request.getId()), LogSeverity.TRACE);
        } catch (DatabaseException e) {
            logger.log("Something went wrong with the database while processing fetch items. " + e.getMessage(), LogSeverity.ERROR);
        }
    }
}
