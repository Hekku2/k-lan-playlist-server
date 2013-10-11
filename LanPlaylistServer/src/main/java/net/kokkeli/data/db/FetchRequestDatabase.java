package net.kokkeli.data.db;

import java.util.ArrayList;
import java.util.Collection;

import com.google.inject.Inject;

import net.kokkeli.ISettings;
import net.kokkeli.data.FetchRequest;
import net.kokkeli.data.FetchStatus;

public class FetchRequestDatabase extends Database implements IFetchRequestDatabase {
    private final FetchRequestsTable table;
    private final TracksTable tracks;
    
    /**
     * Creates fetch request database
     * @param settings Settings
     */
    @Inject
    public FetchRequestDatabase(ISettings settings) {
        super(settings);
        
        tracks = new TracksTable(settings.getDatabaseLocation());
        table = new FetchRequestsTable(settings.getDatabaseLocation());
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
        return null;
    }

    @Override
    public void updateRequest(long id, FetchStatus status) {
        
    }

    @Override
    public FetchRequest oldestUnhandledFetchRequestOrNull(String handler){
        return null;
    }
    
    @Override
    public void CheckDatabaseFormat() throws DatabaseException {
        
    }

}
