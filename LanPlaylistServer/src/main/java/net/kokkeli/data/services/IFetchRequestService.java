package net.kokkeli.data.services;

import java.util.Collection;

import net.kokkeli.data.FetchRequest;

public interface IFetchRequestService {

    /**
     * Returns all fetch requests in database
     * @return All fetch requests
     * @throws ServiceException Thrown if there is a problem with the database
     */
    Collection<FetchRequest> get() throws ServiceException;

    /**
     * Adds fetch request to database
     * @param request Request to be added
     * @throws ServiceException Thrown if there is a problem with the database
     */
    void add(FetchRequest request) throws ServiceException;

}
