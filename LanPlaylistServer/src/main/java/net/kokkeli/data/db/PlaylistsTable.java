package net.kokkeli.data.db;

import java.util.ArrayList;
import java.util.Collection;

import net.kokkeli.data.PlayList;
import com.almworks.sqlite4java.SQLiteConnection;
import com.almworks.sqlite4java.SQLiteException;
import com.almworks.sqlite4java.SQLiteJob;
import com.almworks.sqlite4java.SQLiteQueue;
import com.almworks.sqlite4java.SQLiteStatement;

public class PlaylistsTable {
    private static final String TABLENAME = "playlists";
    private static final String ALLLISTS = "SELECT * FROM " + TABLENAME;
    private static final String COLUMN_ID = "Id";
    private static final String COLUMN_NAME = "Name";

    private final SQLiteQueue queue;

    /**
     * Creates PlaylistTable with given databaselocation
     * 
     * @param queue
     *            Location of database
     */
    public PlaylistsTable(SQLiteQueue queue) {
        this.queue = queue;
    }

    /**
     * Returns Playlist with given id. Playlist doesn't contain tracks.
     * 
     * @param id
     *            Id of playlist
     * @return Found playlist
     * @throws DatabaseException
     *             thrown if there is problem with database
     * @throws NotFoundInDatabase
     *             thrown if no such item is found with given id.
     */
    public PlayList get(final long id) throws DatabaseException, NotFoundInDatabase {
        PlayList list = queue.execute(new SQLiteJob<PlayList>() {
            @Override
            protected PlayList job(SQLiteConnection connection) throws SQLiteException {
                SQLiteStatement st = connection.prepare(getSingleItemQuery(id));

                PlayList playlist = null;
                try {
                    while (st.step()) {
                        playlist = new PlayList(st.columnLong(0));
                        playlist.setName(st.columnString(1));
                    }
                } finally {
                    st.dispose();
                }

                return playlist;
            }
        }).complete();

        //TODO Handle database exception.
        if (list == null)
            throw new NotFoundInDatabase("No such playlist in database.");

        return list;
    }

    /**
     * Returns collection of playlists. Doesn't contain tracks.
     * 
     * @return Collection of paylists
     * @throws DatabaseException
     *             thrown if there is problem with database.
     */
    public Collection<PlayList> get() throws DatabaseException {
        Collection<PlayList> lists = queue.execute(new SQLiteJob<ArrayList<PlayList>>() {
            @Override
            protected ArrayList<PlayList> job(SQLiteConnection connection) throws SQLiteException {
                ArrayList<PlayList> playlists = new ArrayList<PlayList>();

                SQLiteStatement st = connection.prepare(ALLLISTS);
                try {
                    while (st.step()) {
                        PlayList list = new PlayList(st.columnLong(0));
                        list.setName(st.columnString(1));
                        playlists.add(list);
                    }
                } finally {
                    st.dispose();
                }

                return playlists;

            }
        }).complete();

        if (lists == null) {
            throw new DatabaseException("Unable to get all playlists. There was a problem with the database.");
        }

        return lists;
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

        Long id = queue.execute(new SQLiteJob<Long>() {
            @Override
            protected Long job(SQLiteConnection connection) throws SQLiteException {
                SQLiteStatement st = connection.prepare(createInsertString(item));

                try {
                    st.stepThrough();
                } finally {
                    st.dispose();
                }

                return connection.getLastInsertId();
            }
        }).complete();

        if (id == null) {
            throw new DatabaseException("Inserting playlist failed.");
        }

        item.setId(id);
        return item;
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
    public boolean exists(final String name) throws DatabaseException {
        Boolean exists = queue.execute(new SQLiteJob<Boolean>() {
            @Override
            protected Boolean job(SQLiteConnection connection) throws SQLiteException {
                SQLiteStatement st = connection.prepare(getPlaylistWithName(name));
                try {
                    while (st.step()) {
                        return true;
                    }
                } finally {
                    st.dispose();
                }
                return false;
            }
        }).complete();

        if (exists == null) {
            throw new DatabaseException("Unable to check if username exists in database.");
        }

        return exists;
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
}
