package net.kokkeli.data.db;

import java.util.Collection;

import net.kokkeli.data.Track;

public class PlaylistDatabase implements IPlaylistDatabase {

    @Override
    public void CheckDatabaseFormat() throws DatabaseException {
        // TODO Database check;
    }

    @Override
    public PlayList get(long id) throws DatabaseException, NotFoundInDatabase {
        PlayList playlist = new PlayList();
        
        playlist.setName("Mock playlist 666");
        
        for (int i = 0; i < 27; i++) {
            Track item = new Track();
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
