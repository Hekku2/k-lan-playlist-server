package net.kokkeli.data.db;

import java.util.Collection;

import net.kokkeli.ISettings;
import net.kokkeli.data.FetchRequest;
import net.kokkeli.data.FetchStatus;

public class FetchRequestDatabase extends Database implements IFetchRequestDatabase {
    private final FetchRequestsTable table;
    
    public FetchRequestDatabase(ISettings settings) throws DatabaseException {
        super(settings);
        
        table = new FetchRequestsTable(settings.getDatabaseLocation());
    }

    @Override
    public FetchRequest get(long id) throws DatabaseException,
            NotFoundInDatabase {
        return null;
    }

    @Override
    public Collection<FetchRequest> get() throws DatabaseException {
        return table.get();
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
