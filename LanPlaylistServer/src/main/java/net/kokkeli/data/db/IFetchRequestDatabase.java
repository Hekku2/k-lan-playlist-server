package net.kokkeli.data.db;

import net.kokkeli.data.FetchRequest;
import net.kokkeli.data.FetchStatus;

public interface IFetchRequestDatabase extends IDatabase<FetchRequest>{

    /**
     * Updates fetch status of request having this id
     * @param id Id of fetch status
     * @param status Wanted status
     */
    void updateRequest(long id, FetchStatus status);

    /**
     * Fetches oldest unhandled fetchrequest. If none exists null is returned.
     * @param handler Handler
     * @return FetcheRequest. Null if no exists.
     */
    FetchRequest oldestUnhandledFetchRequestOrNull(String handler) throws DatabaseException;
}
