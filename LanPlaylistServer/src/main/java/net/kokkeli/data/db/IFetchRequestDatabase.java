package net.kokkeli.data.db;

import net.kokkeli.data.FetchRequest;
import net.kokkeli.data.FetchStatus;

/**
 * Interface for database containing fetch requests
 * @author Hekku2
 */
public interface IFetchRequestDatabase extends IDatabase<FetchRequest>{

    /**
     * Updates fetch status of request having this id
     * @param id Id of fetch status
     * @param status Wanted status
     * @throws DatabaseException Thrown if there is a problem with the database
     */
    void updateRequest(long id, FetchStatus status) throws DatabaseException;

    /**
     * Fetches oldest unhandled fetchrequest. If none exists null is returned.
     * @param handler Handler
     * @return FetcheRequest. Null if no exists.
     * @throws DatabaseException Thrown if there is a problem with the database
     */
    FetchRequest oldestUnhandledFetchRequestOrNull(String handler) throws DatabaseException;

    /**
     * Removes handled fetch requests from database
     * @throws DatabaseException Thrown if there is a problem with the database
     */
    void removeHandled() throws DatabaseException;

    /**
     * Removes request with given id
     * @param requestId Request id
     * @throws DatabaseException Thrown if there is a problem with the database
     */
    void remove(long requestId) throws DatabaseException;
}
