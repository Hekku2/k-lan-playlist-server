package net.kokkeli.data.db;

import java.util.Collection;

import com.google.inject.Inject;
import net.kokkeli.ISettings;
import net.kokkeli.data.PlayList;
import net.kokkeli.data.Track;
import net.kokkeli.data.User;

public class PlaylistDatabase extends Database implements IPlaylistDatabase {
    private final PlaylistsTable playlistTable;
    private final TrackPlaylistTable trackPlaylistTable;
    private final TracksTable tracksTable;
    private final UsersTable usersTable;
    
    @Inject
    public PlaylistDatabase(ISettings settings) throws DatabaseException {
        super(settings);
        playlistTable = new PlaylistsTable(getDatabaseLocation());
        trackPlaylistTable = new TrackPlaylistTable(getDatabaseLocation());
        tracksTable = new TracksTable(getDatabaseLocation());
        usersTable = new UsersTable(getDatabaseLocation());
        
        CheckDatabaseFormat();
    }

    @Override
    public void CheckDatabaseFormat() throws DatabaseException {
        get();
    }

    @Override
    public PlayList get(long id) throws DatabaseException, NotFoundInDatabase {
        // Fetch playlist data
        PlayList playlist = playlistTable.get(id);
        
        // Fetch track ids from track-playlist database
        Collection<Long> tracks = trackPlaylistTable.getTracks(playlist.getId());
        
        // Fetch tracks from database
        for (Long trackId : tracks) {
            Track track = tracksTable.get(trackId);
            User uploader = usersTable.get(track.getUploader().getId());
            track.setUploader(uploader);
            playlist.getItems().add(track);
        }
        
        return playlist;
    }

    @Override
    public Collection<PlayList> get() throws DatabaseException {
        Collection<PlayList> playlists = playlistTable.get();
        
        try {
            for (PlayList playList : playlists) {
                Collection<Long> trackIds = trackPlaylistTable.getTracks(playList.getId());
                
                for (Long trackId : trackIds) {
                    Track track = tracksTable.get(trackId);
                    User uploader = usersTable.get(track.getUploader().getId());
                    track.setUploader(uploader);
                    playList.getItems().add(track);
                }
            }
        } catch (NotFoundInDatabase e) {
            throw new DatabaseException("Some key was missing.", e);
        }

        return playlists;
    }

    @Override
    public PlayList add(PlayList item) throws DatabaseException {
        throw new DatabaseException("Method not implemented.");
    }

    @Override
    public Collection<PlayList> getOnlyIdAndName() throws DatabaseException {
        return playlistTable.get();
    }

    @Override
    public void update(PlayList playlist) throws DatabaseException, NotFoundInDatabase {
        PlayList old = get(playlist.getId());
        
        if (!old.getItems().equals(playlist.getName())){
            playlistTable.update(playlist);
        }
        
        // Removes removed tracks from playlist.
        for (Track oldTrack : old.getItems()) {
            if (!contains(oldTrack, playlist.getItems())){
                trackPlaylistTable.delete(oldTrack.getId(), old.getId());
            }
        }
        
        // Adds new tracks to playlist. If track is not already in database, it is now added to database.
        for (Track newTrack : playlist.getItems()) {
            if (!contains(newTrack, old.getItems())){
                try {
                    tracksTable.get(newTrack.getId());
                } catch (NotFoundInDatabase e) {
                    newTrack.setId(tracksTable.insert(newTrack));
                }
                
                trackPlaylistTable.insert(newTrack.getId(), old.getId());
            }
        }
    }
    
    /**
     * Checks if collection contains given track. Only checks id.
     * @param track Track
     * @param collection Colleciton of tracks
     * @return True, if track is in collection.
     */
    private static boolean contains(Track track, Collection<Track> collection){
        for (Track candidate : collection) {
            if (candidate.getId() == track.getId())
                return true;
        }
        return false;
    }
}
