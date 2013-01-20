package net.kokkeli.data.db;

import java.util.Collection;

import com.google.inject.Inject;
import net.kokkeli.ISettings;

public class PlaylistDatabase extends Database implements IPlaylistDatabase {
    private final PlaylistsTable playlistTable;
    private final TrackPlaylistTable trackPlaylistTable;
    private final TracksTable tracksTable;
    
    @Inject
    public PlaylistDatabase(ISettings settings) throws DatabaseException {
        super(settings);
        playlistTable = new PlaylistsTable(getDatabaseLocation());
        trackPlaylistTable = new TrackPlaylistTable(getDatabaseLocation());
        tracksTable = new TracksTable(getDatabaseLocation());
        
        CheckDatabaseFormat();
    }

    @Override
    public void CheckDatabaseFormat() throws DatabaseException {
        // TODO Database check;
    }

    @Override
    public PlayList get(long id) throws DatabaseException, NotFoundInDatabase {
        // Fetch playlist data
        PlayList playlist = playlistTable.get(id);
        
        // Fetch track ids from track-playlist database
        Collection<Long> tracks = trackPlaylistTable.getTracks(playlist.getId());
        
        // Fetch tracks from database
        for (Long item : tracks) {
            playlist.getItems().add(tracksTable.get(item));
        }
        
        return playlist;
    }

    @Override
    public Collection<PlayList> get() throws DatabaseException {
        throw new DatabaseException("Method not implemented.");
    }

    @Override
    public void add(PlayList item) throws DatabaseException {
        throw new DatabaseException("Method not implemented.");
    }
}
