package net.kokkeli.data.db;

import java.util.ArrayList;
import java.util.Collection;

import com.almworks.sqlite4java.SQLiteQueue;
import com.google.inject.Inject;

import net.kokkeli.data.FetchRequest;
import net.kokkeli.data.FetchStatus;

/**
 * Fetch request database
 * @author Hekku2
 *
 */
public class FetchRequestDatabase extends Database implements IFetchRequestDatabase {
    private final FetchRequestsTable table;
    private final TracksTable tracks;
    
    /**
     * Creates fetch request database
     * @param settings Settings
     */
    @Inject
    public FetchRequestDatabase(SQLiteQueue queue) {
        super();
        
        tracks = new TracksTable(queue);
        table = new FetchRequestsTable(queue);
    }

    @Override
    public FetchRequest get(long id) throws DatabaseException,
            NotFoundInDatabase {
        return null;
    }

    @Override
    public Collection<FetchRequest> get() throws DatabaseException {
        ArrayList<FetchRequest> requests = table.get();
        try {
            for (FetchRequest fetchRequest : requests) {
                fetchRequest.setTrack(tracks.get(fetchRequest.getTrack().getId()));
            }
        } catch (NotFoundInDatabase e) {
            throw new DatabaseException("For some reason, track was not in database. This should not happen!", e);
        }
        
        return requests;
    }

    @Override
    public FetchRequest add(FetchRequest item) throws DatabaseException {
        return table.insert(item);
    }

    @Override
    public void updateRequest(long id, FetchStatus status) throws DatabaseException {
        table.update(id, status);
    }

    @Override
    public FetchRequest oldestUnhandledFetchRequestOrNull(String handler) throws DatabaseException{
        return table.oldestUnhandledfetchRequest(handler);
    }
    
    @Override
    public void CheckDatabaseFormat() throws DatabaseException {
        get();
    }

    @Override
    public void removeHandled() throws DatabaseException {
        table.removeWithStatus(FetchStatus.HANDLED);
    }

    @Override
    public void remove(long requestId) throws DatabaseException {
        table.remove(requestId);
    }

}
