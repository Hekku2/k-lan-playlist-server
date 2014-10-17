package net.kokkeli.data.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;

public class TrackPlaylistTable {
    private static final String TABLENAME = "tracks_playlists";
    private static final String TRACK_ID_COLUMN = "TrackId";
    private static final String PLAYLIST_ID_COLUMN = "PlayListId";
    
    private static final String TRACK_IDS = "SELECT "+ TRACK_ID_COLUMN +" FROM " + TABLENAME;
    private static final String DELETE = "DELETE FROM " + TABLENAME + " ";
    private static final String INSERT = "INSERT INTO " + TABLENAME + " (" + TRACK_ID_COLUMN + "," + PLAYLIST_ID_COLUMN + ") VALUES ";
    
    private final IConnectionStorage storage;
    
    /**
     * Creates new table with given database location
     * @param queue
     */
    public TrackPlaylistTable(IConnectionStorage storage) {
        this.storage = storage;
    }

    /**
     * Returns trackIds in given playlist
     * @param playlistId Playlist id
     * @return Collection of trackIds.
     * @throws DatabaseException Thrown if there is problem with database
     */
    @SuppressWarnings("resource")
    public Collection<Long> getTracks(final long playlistId) throws DatabaseException {
        try (Connection connection = storage.getConnection()){
            try (Statement statement = connection.createStatement()){
                Collection<Long> ids = new ArrayList<Long>();
                ResultSet rs = statement.executeQuery(getAllTracksWithPlaylistId(playlistId));
                while(rs.next())
                {
                    ids.add(rs.getLong(TRACK_ID_COLUMN));
                }
                return ids;
            } 
            
        } catch (SQLException e) {
            throw new DatabaseException("Getting tracks failed.", e);
        }
    }

    /**
     * Deleted connection between playlist and track.
     * @param trackId Track Id
     * @param playlistId Playlist Id
     * @throws DatabaseException Thrown if there is problem with database
     */
    public void delete(final long trackId, final long playlistId) throws DatabaseException{
        try (Connection connection = storage.getConnection()){
            try (Statement statement = connection.createStatement()){
                statement.executeUpdate(DELETE + selectorForRow(trackId, playlistId));
            } 
            
        } catch (SQLException e) {
            throw new DatabaseException("Deleting track failed.", e);
        }
    }
    
    /**
     * Inserts connection between playlist and track
     * @param trackId Track Id
     * @param playlistId Playlist id
     * @throws DatabaseException Thrown if there is problem with database
     */
    public void insert(final long trackId, final long playlistId) throws DatabaseException {
        try (Connection connection = storage.getConnection()){
            try (Statement statement = connection.createStatement()){
                statement.executeUpdate(buildInsert(trackId, playlistId));
            } 
            
        } catch (SQLException e) {
            throw new DatabaseException("Deleting track failed.", e);
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
