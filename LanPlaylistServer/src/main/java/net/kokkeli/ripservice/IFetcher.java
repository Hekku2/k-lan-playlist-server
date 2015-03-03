package net.kokkeli.ripservice;

import net.kokkeli.data.dto.FetchRequest;

/**
 * Interface for classes that fecth stuff from internet.
 * @author Hekku2
 *
 */
public interface IFetcher {
    /**
     * Fetches the request
     * @param request Request
     * @throws FetchFailedException Thrown if fetching fails for some reason
     */
    void fetch(FetchRequest request) throws FetchFailedException;
    
    /**
     * Returns type this fetcher handles
     * @return Handler type
     */
    String getHandledType();
}
