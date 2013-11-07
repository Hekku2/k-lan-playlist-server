package net.kokkeli.data.db;

import java.util.ArrayList;
import java.util.Collection;

import com.google.inject.Inject;

import net.kokkeli.ISettings;
import net.kokkeli.data.Track;

public class TrackDatabase extends Database implements ITrackDatabase{
    private final TracksTable tracksTable;
    private final UsersTable usersTable;
    
    @Inject
    public TrackDatabase(ISettings settings){
        super(settings);
        tracksTable = new TracksTable(getDatabaseLocation());
        usersTable = new UsersTable(getDatabaseLocation());
    }
    
    @Override
    public void CheckDatabaseFormat() throws DatabaseException {
        tracksTable.get();
    }

    @Override
    public Track get(long id) throws DatabaseException, NotFoundInDatabase {
        Track track = tracksTable.get(id);
        track.setUploader(usersTable.get(track.getUploader().getId()));
        
        return track;
    }

    @Override
    public Collection<Track> get() throws DatabaseException {
        ArrayList<Track> tracks = tracksTable.get();
        try {
            for (Track track : tracks) {
                track.setUploader(usersTable.get(track.getUploader().getId()));
            }
        } catch (NotFoundInDatabase e) {
            throw new DatabaseException("There was an uploader that did not match any user.", e);
        }

        return tracks;
    }

    @Override
    public Track add(Track item) throws DatabaseException {
        return tracksTable.insert(item);
    }

    @Override
    public boolean exists(Track track) throws DatabaseException {
        try {
            tracksTable.get(track.getId());
            return true;
        } catch (NotFoundInDatabase e) {
            return false;
        }
    }

    @Override
    public void update(Track track) throws NotFoundInDatabase, DatabaseException  {
        if (!exists(track)){
            throw new NotFoundInDatabase(String.format("Track with id %s not found", track.getId()));
        }
        
        tracksTable.update(track);
    }

}
