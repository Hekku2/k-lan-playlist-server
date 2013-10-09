package net.kokkeli.data.db;

import java.util.Collection;

import net.kokkeli.ISettings;
import net.kokkeli.data.FetchRequest;
import net.kokkeli.data.FetchStatus;

public class FetchRequestDatabase extends Database implements IFetchRequestDatabase {
    
    public FetchRequestDatabase(ISettings settings) throws DatabaseException {
        super(settings);
    }

    @Override
    public FetchRequest get(long id) throws DatabaseException,
            NotFoundInDatabase {
        return null;
    }

    @Override
    public Collection<FetchRequest> get() throws DatabaseException {
        return null;
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
