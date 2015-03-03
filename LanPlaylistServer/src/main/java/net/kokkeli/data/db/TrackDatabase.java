package net.kokkeli.data.db;

import java.util.ArrayList;
import java.util.Collection;

import com.google.inject.Inject;

import net.kokkeli.data.dto.Track;

public class TrackDatabase extends Database implements ITrackDatabase{
    private final TracksTable tracksTable;
    private final UsersTable usersTable;
    
    @Inject
    public TrackDatabase(IConnectionStorage storage){
        super();
        tracksTable = new TracksTable(storage);
        usersTable = new UsersTable(storage);
    }
    
    @Override
    public void CheckDatabaseFormat() throws DatabaseException {
        tracksTable.get();
    }

    @Override
    public Track get(long id) throws DatabaseException, NotFoundInDatabaseException {
        Track track = tracksTable.get(id);
        track.setUploader(usersTable.get(track.getUploader().getId()));
        
        return track;
    }

    @Override
    public Collection<Track> get() throws DatabaseException {
        ArrayList<Track> tracks = tracksTable.get();
        try {
            for (Track track : tracks) {
                if (track.getUploader() != null)
                    track.setUploader(usersTable.get(track.getUploader().getId()));
            }
        } catch (NotFoundInDatabaseException e) {
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
        } catch (NotFoundInDatabaseException e) {
            return false;
        }
    }

    @Override
    public void update(Track track) throws NotFoundInDatabaseException, DatabaseException  {
        if (!exists(track)){
            throw new NotFoundInDatabaseException(String.format("Track with id %s not found", track.getId()));
        }
        
        tracksTable.update(track);
    }

}
