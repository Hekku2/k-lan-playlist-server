package net.kokkeli.data.db;

import java.util.ArrayList;
import java.util.Collection;

import com.almworks.sqlite4java.SQLiteConnection;
import com.almworks.sqlite4java.SQLiteException;
import com.almworks.sqlite4java.SQLiteJob;
import com.almworks.sqlite4java.SQLiteQueue;
import com.almworks.sqlite4java.SQLiteStatement;

public class TrackPlaylistTable {
    private static final String TABLENAME = "tracks_playlists";
    private static final String TRACK_ID_COLUMN = "TrackId";
    private static final String PLAYLIST_ID_COLUMN = "PlayListId";
    
    private static final String TRACK_IDS = "SELECT "+ TRACK_ID_COLUMN +" FROM " + TABLENAME;
    private static final String DELETE = "DELETE FROM " + TABLENAME + " ";
    private static final String INSERT = "INSERT INTO " + TABLENAME + " (" + TRACK_ID_COLUMN + "," + PLAYLIST_ID_COLUMN + ") VALUES ";
    
    private final SQLiteQueue queue;
    
    /**
     * Creates new table with given database location
     * @param queue
     */
    public TrackPlaylistTable(SQLiteQueue queue) {
        this.queue = queue;
    }

    /**
     * Returns trackIds in given playlist
     * @param playlistId Playlist id
     * @return Collection of trackIds.
     * @throws DatabaseException Thrown if there is problem with database
     */
    public Collection<Long> getTracks(final long playlistId) throws DatabaseException {
        Collection<Long> ids = queue.execute(new SQLiteJob<Collection<Long>>() {
            @Override
            protected Collection<Long> job(SQLiteConnection connection) throws SQLiteException {
                SQLiteStatement st = connection.prepare(getAllTracksWithPlaylistId(playlistId));

                Collection<Long> ids = new ArrayList<Long>();
                try {
                    while (st.step()) {
                        ids.add(st.columnLong(0));
                    }
                } finally {
                    st.dispose();
                }

                return ids;
            }
        }).complete();
        
        if (ids == null){
            throw new DatabaseException("Unable to get tracks for playlist with Id: " + playlistId);
        }
        
        return ids;
    }

    /**
     * Deleted connection between playlist and track.
     * @param trackId Track Id
     * @param playlistId Playlist Id
     * @throws DatabaseException Thrown if there is problem with database
     */
    public void delete(final long trackId, final long playlistId) throws DatabaseException{
        queue.execute(new SQLiteJob<Object>() {
            @Override
            protected Object job(SQLiteConnection connection) throws SQLiteException {
                SQLiteStatement st = connection.prepare(DELETE + selectorForRow(trackId, playlistId));
                try {
                    st.stepThrough();
                } finally {
                    st.dispose();
                }
                return null;
            }
        });
    }
    
    /**
     * Inserts connection between playlist and track
     * @param trackId Track Id
     * @param playlistId Playlist id
     * @throws DatabaseException Thrown if there is problem with database
     */
    public void insert(final long trackId, final long playlistId) throws DatabaseException {
        queue.execute(new SQLiteJob<Object>() {
            @Override
            protected Object job(SQLiteConnection connection) throws SQLiteException {
                SQLiteStatement st = connection.prepare(buildInsert(trackId, playlistId));
                try {
                    st.stepThrough();
                } finally {
                    st.dispose();
                }
                return null;
            }
        });
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
