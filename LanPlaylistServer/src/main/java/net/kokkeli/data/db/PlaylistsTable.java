package net.kokkeli.data.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;

import net.kokkeli.data.dto.PlayList;

public class PlaylistsTable {
    private static final String TABLENAME = "playlists";
    private static final String ALLLISTS = "SELECT * FROM " + TABLENAME;
    private static final String COLUMN_ID = "Id";
    private static final String COLUMN_NAME = "Name";

    private final IConnectionStorage storage;

    /**
     * Creates PlaylistTable with given databaselocation
     * 
     * @param queue
     *            Location of database
     */
    public PlaylistsTable(IConnectionStorage storage) {
        this.storage = storage;
    }

    /**
     * Returns Playlist with given id. Playlist doesn't contain tracks.
     * 
     * @param id
     *            Id of playlist
     * @return Found playlist
     * @throws DatabaseException
     *             thrown if there is problem with database
     * @throws NotFoundInDatabaseException
     *             thrown if no such item is found with given id.
     */
    @SuppressWarnings("resource")
    public PlayList get(final long id) throws DatabaseException, NotFoundInDatabaseException {
        try (Connection connection = storage.getConnection()){
            try (Statement statement = connection.createStatement()){
                ResultSet rs = statement.executeQuery(getSingleItemQuery(id));
                while(rs.next())
                {
                    return createPlayList(rs);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Getting playlist failed.", e);
        }
        throw new NotFoundInDatabaseException("No such playlist in database.");
    }
    
    /**
     * Returns collection of playlists. Doesn't contain tracks.
     * 
     * @return Collection of paylists
     * @throws DatabaseException
     *             thrown if there is problem with database.
     */
    @SuppressWarnings("resource")
    public Collection<PlayList> get() throws DatabaseException {
        try (Connection connection = storage.getConnection()){
            try (Statement statement = connection.createStatement()){
                ArrayList<PlayList> playlists = new ArrayList<PlayList>();
                ResultSet rs = statement.executeQuery(ALLLISTS);
                while(rs.next())
                {
                    playlists.add(createPlayList(rs));
                }
                return playlists;
            }
        } catch (SQLException e) {
            throw new DatabaseException("Getting playlists failed.", e);
        }
    }

    /**
     * Updates playlist. Doesn't update tracks.
     * 
     * @param playlist
     *            Playlist
     * @throws DatabaseException
     *             Thrown if there is problem with database
     */
    public void update(PlayList playlist) throws DatabaseException {
        // TODO Update playlist
    }

    /**
     * Inserts the playlist. Doesn't update tracks.
     * 
     * @param playlist
     *            Playlist
     * @throws DatabaseException
     *             Thrown if there is problem with database
     */
    public PlayList insert(final PlayList item) throws DatabaseException {
        if (exists(item.getName()))
            throw new DatabaseException("Playlist with given name already exists.");

        try (Connection connection = storage.getConnection()){
            try (Statement statement = connection.createStatement()){
                connection.setAutoCommit(false);
                statement.executeUpdate(createInsertString(item));
                long id = statement.executeQuery("SELECT last_insert_rowid()").getLong(1);
                item.setId(id);
                connection.commit();
                return item;
            }
        } catch (SQLException e) {
            throw new DatabaseException("Inserting playlist failed.", e);
        }
    }

    /**
     * Checks if playlist with name already exists.
     * 
     * @param name
     *            Name of playlist
     * @return True, if playlist with name exists.
     * @throws DatabaseException
     *             Thrown if there is a problem with the database
     */
    @SuppressWarnings("resource")
    public boolean exists(final String name) throws DatabaseException {
        try (Connection connection = storage.getConnection()){
            try (Statement statement = connection.createStatement()){
                ResultSet rs = statement.executeQuery(getPlaylistWithName(name));
                while(rs.next())
                {
                    return true;
                }
                return false;
            }
        } catch (SQLException e) {
            throw new DatabaseException("Checkin playlist name existance failed.", e);
        }
    }

    /**
     * Creates query selecting single user.
     * 
     * @param id
     *            Id of wanted user
     * @return Query for selecting single user.
     */
    private static String getSingleItemQuery(long id) {
        return ALLLISTS + " WHERE " + COLUMN_ID + " = " + id;
    }

    /**
     * Creates insert statement for Playlist
     * 
     * @param PlayList
     *            playlist
     * @return Insert statement
     */
    private static String createInsertString(PlayList playlist) {
        return String.format("INSERT INTO %s (%s) VALUES ('%s')", TABLENAME, COLUMN_NAME, playlist.getName());
    }

    /**
     * Creates query selecting users with given username.
     * 
     * @param username
     *            Username of user
     * @return Query for selecting users with given username.
     */
    private static String getPlaylistWithName(String name) {
        return ALLLISTS + " WHERE " + COLUMN_NAME + " = '" + name + "'";
    }
    
    private static PlayList createPlayList(ResultSet rs) throws SQLException{
        PlayList playlist = null;
        playlist = new PlayList(rs.getLong(COLUMN_ID));
        playlist.setName(rs.getString(COLUMN_NAME));
        return playlist;
    }
}
