package net.kokkeli.ripservice;

import net.kokkeli.data.FetchRequest;

/**
 * Interface for classes that fecth stuff from internet.
 * @author Hekku2
 *
 */
public interface IFetcher {
    /**
     * Fetches the request
     * @param request Request
     */
    void fetch(FetchRequest request);
    
    /**
     * Returns type this fetcher handles
     * @return Handler type
     */
    String getHandledType();
}
