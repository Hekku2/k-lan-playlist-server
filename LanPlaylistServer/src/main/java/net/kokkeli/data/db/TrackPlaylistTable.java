package net.kokkeli.data.db;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import com.almworks.sqlite4java.SQLiteConnection;
import com.almworks.sqlite4java.SQLiteException;
import com.almworks.sqlite4java.SQLiteStatement;

public class TrackPlaylistTable {
    private static final String TABLENAME = "tracks_playlists";
    private static final String TRACK_ID_COLUMN = "TrackId";
    private static final String PLAYLIST_ID_COLUMN = "PlayListId";
    
    private static final String TRACK_IDS = "SELECT "+ TRACK_ID_COLUMN +" FROM " + TABLENAME;
    
    private final String databaseLocation;
    
    /**
     * Creates new table with given database location
     * @param databaseLocation
     */
    public TrackPlaylistTable(String databaseLocation) {
        this.databaseLocation = databaseLocation;
    }

    /**
     * Returns trackIds in given playlist
     * @param playlistId Playlist id
     * @return Collection of trackIds.
     * @throws DatabaseException Thrown if there is problem with database
     */
    public Collection<Long> getTracks(long playlistId) throws DatabaseException {
        SQLiteConnection db = new SQLiteConnection(new File(databaseLocation));
        Collection<Long> ids = new ArrayList<Long>();
        
        try {
            db.open(false);
            SQLiteStatement st = db.prepare(getAllTracksWithPlaylistId(playlistId));
            try {
                while (st.step()) {
                    ids.add(st.columnLong(0));
                }
            } finally {
                st.dispose();
            }
            db.dispose();
        } catch (SQLiteException e) {
            throw new DatabaseException("Unable to get tracks for playlist with Id: " + playlistId, e);
        }
        
        return ids;
    }

    /**
     * Creates query selecting single user.
     * @param id Id of wanted user
     * @return Query for selecting single user.
     */
    private static String getAllTracksWithPlaylistId(long id){
        return TRACK_IDS + " WHERE "+ PLAYLIST_ID_COLUMN+" = " + id;
    }
}
