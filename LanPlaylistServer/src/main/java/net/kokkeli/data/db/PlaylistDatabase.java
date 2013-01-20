package net.kokkeli.data.db;

import java.util.Collection;

import com.google.inject.Inject;
import net.kokkeli.ISettings;
import net.kokkeli.data.Track;

public class PlaylistDatabase extends Database implements IPlaylistDatabase {
    private final PlaylistsTable playlistTable;
    
    @Inject
    public PlaylistDatabase(ISettings settings) throws DatabaseException {
        super(settings);
        playlistTable = new PlaylistsTable(getDatabaseLocation());
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
        // Fetch tracks from database
        for (int i = 0; i < 27; i++) {
            Track item = new Track(i);
            item.setArtist("Best artist");
            item.setTrackName("Cool song " + i);
            
            playlist.getItems().add(item);
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
