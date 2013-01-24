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
    private static final String DELETE = "DELETE FROM " + TABLENAME + " ";
    private static final String INSERT = "INSERT INTO " + TABLENAME + " (" + TRACK_ID_COLUMN + "," + PLAYLIST_ID_COLUMN + ") VALUES ";
    
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
     * Deleted connection between playlist and track.
     * @param trackId Track Id
     * @param playlistId Playlist Id
     * @throws DatabaseException Thrown if there is problem with database
     */
    public void delete(long trackId, long playlistId) throws DatabaseException{
        SQLiteConnection db = new SQLiteConnection(new File(databaseLocation));
        
        try {
            db.open(false);
            SQLiteStatement st = db.prepare(DELETE + selectorForRow(trackId, playlistId));
            try {
                st.stepThrough();
            } finally {
                st.dispose();
            }
        } catch (SQLiteException e) {
            throw new DatabaseException("Unable to delete item. TrackId: " + trackId + ", PlaylistId: " + playlistId, e);
        }
    }
    
    /**
     * Inserts connection between playlist and track
     * @param trackId Track Id
     * @param playlistId Playlist id
     * @throws DatabaseException Thrown if there is problem with database
     */
    public void insert(long trackId, long playlistId) throws DatabaseException {
        SQLiteConnection db = new SQLiteConnection(new File(databaseLocation));
        
        try {
            db.open(false);
            SQLiteStatement st = db.prepare(buildInsert(trackId, playlistId));
            try {
                st.stepThrough();
            } finally {
                st.dispose();
            }
        } catch (SQLiteException e) {
            throw new DatabaseException("Unable to insert item. TrackId: " + trackId + ", PlaylistId: " + playlistId, e);
        }
    }
    
    /**
     * Creates query selecting single user.
     * @param id Id of wanted user
     * @return Query for selecting single user.
     */
    private static String getAllTracksWithPlaylistId(long id){
        return TRACK_IDS + " WHERE "+ PLAYLIST_ID_COLUMN+" = " + id;
    }
    
    private static String selectorForRow(long trackId, long playlistId){
        return "WHERE " + TRACK_ID_COLUMN + "=" + trackId + " AND " + PLAYLIST_ID_COLUMN + "=" + playlistId;
    }

    private static String buildInsert(long trackId, long playlistId){
        return INSERT + "(" + trackId + "," + playlistId + ")";
    }
}
