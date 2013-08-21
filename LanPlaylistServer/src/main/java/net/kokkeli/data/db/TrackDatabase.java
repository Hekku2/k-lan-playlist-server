package net.kokkeli.data.db;

import java.util.ArrayList;
import java.util.Collection;

import com.google.inject.Inject;

import net.kokkeli.ISettings;
import net.kokkeli.data.Track;

public class TrackDatabase extends Database implements ITrackDatabase{
    private final TracksTable tracksTable;
    
    @Inject
    public TrackDatabase(ISettings settings) throws DatabaseException{
        super(settings);
        tracksTable = new TracksTable(getDatabaseLocation());
    }
    
    @Override
    public void CheckDatabaseFormat() throws DatabaseException {
    }

    @Override
    public Track get(long id) throws DatabaseException, NotFoundInDatabase {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Collection<Track> get() throws DatabaseException {
        ArrayList<Track> tracks = tracksTable.get();
        
        //TODO Add user
        return tracks;
    }

    @Override
    public Track add(Track item) throws DatabaseException {
        return null;
    }

}
