package net.kokkeli.data.db;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import net.kokkeli.data.PlayList;
import com.almworks.sqlite4java.SQLiteConnection;
import com.almworks.sqlite4java.SQLiteException;
import com.almworks.sqlite4java.SQLiteStatement;

public class PlaylistsTable{
    private static final String TABLENAME = "playlists";
    private static final String ALLLISTS = "SELECT * FROM " + TABLENAME;
    private static final String COLUMN_ID = "Id";
    private static final String COLUMN_NAME = "Name";
    
    
    private final String databaseLocation;
    
    /**
     * Creates PlaylistTable with given databaselocation
     * @param databaseLocation Location of database
     */
    public PlaylistsTable(String databaseLocation) {
        this.databaseLocation = databaseLocation;
    }
    
    /**
     * Returns Playlist with given id. Playlist doesn't contain tracks.
     * @param id Id of playlist
     * @return Found playlist
     * @throws DatabaseException thrown if there is problem with database
     * @throws NotFoundInDatabase thrown if no such item is found with given id.
     */
    public PlayList get(long id) throws DatabaseException, NotFoundInDatabase{
        SQLiteConnection db = new SQLiteConnection(new File(databaseLocation));
        PlayList list = null;
        
        try {
            db.open(false);
            SQLiteStatement st = db.prepare(getSingleItemQuery(id));
            try {
                while (st.step()) {
                    list = new PlayList(st.columnLong(0));
                    list.setName(st.columnString(1));
                }
            } finally {
                st.dispose();
            }
            db.dispose();
        } catch (SQLiteException e) {
            throw new DatabaseException("Unable to get playlist with Id: " + id, e);
        }
        
        if (list == null)
            throw new NotFoundInDatabase("No such playlist in database.");
        
        return list;
    }
    
    /**
     * Creates query selecting single user.
     * @param id Id of wanted user
     * @return Query for selecting single user.
     */
    private static String getSingleItemQuery(long id){
        return ALLLISTS + " WHERE "+ COLUMN_ID+" = " + id;
    }

    /**
     * Returns collection of playlists. Doesn't contain tracks.
     * @return Collection of paylists
     * @throws DatabaseException thrown if there is problem with database.
     */
    public Collection<PlayList> get() throws DatabaseException {
        SQLiteConnection db = new SQLiteConnection(new File(databaseLocation));
        Collection<PlayList> lists = new ArrayList<PlayList>();
        
        try {
            db.open(false);
            SQLiteStatement st = db.prepare(ALLLISTS);
            try {
                while (st.step()) {
                    PlayList list = new PlayList(st.columnLong(0));
                    list.setName(st.columnString(1));
                    lists.add(list);
                }
            } finally {
                st.dispose();
            }
            db.dispose();
        } catch (SQLiteException e) {
            throw new DatabaseException("Unable to get playlists.", e);
        }
        
        return lists;
    }

    /**
     * Updates playlist. Doesn't update tracks.
     * @param playlist Playlist
     * @throws DatabaseException Thrown if there is problem with database
     */
    public void update(PlayList playlist) throws DatabaseException {
        //TODO Update playlist
    }
    
    /**
     * Inserts the playlist. Doesn't update tracks.
     * @param playlist Playlist
     * @throws DatabaseException Thrown if there is problem with database
     */
    public PlayList insert(PlayList item) throws DatabaseException {
        if (exists(item.getName()))
            throw new DatabaseException("Playlist with given name already exists.");
        
        SQLiteConnection db = new SQLiteConnection(new File(databaseLocation));
        long id;
        try {
            db.open(false);
            SQLiteStatement st = db.prepare(createInsertString(item));
            try {
                st.stepThrough();
            } finally {
                st.dispose();
            }
            id = db.getLastInsertId();
            
            db.dispose();
        } catch (SQLiteException e) {
            throw new DatabaseException("There was problem with database", e);
        }
        
        item.setId(id);
        return item;
    }
    
    /**
     * Checks if user with username exists. exist.
     * @param username Username
     * @return True, if user with username exists.
     * @throws DatabaseException Thrown if there is a problem with the database
     */
    public boolean exists(String username) throws DatabaseException {
        SQLiteConnection db = new SQLiteConnection(new File(databaseLocation));
        
        try {
            db.open(false);
            SQLiteStatement st = db.prepare(getPlaylistWithName(username));
            try {
                while (st.step()) {
                    return true;
                }
            } finally {
                st.dispose();
            }
            
            db.dispose();
        } catch (SQLiteException e) {
            throw new DatabaseException("Problem with database.", e);
        }
        return false;
    }
    
    /**
     * Creates insert statement for Playlist
     * @param PlayList playlist
     * @return Insert statement
     */
    private static String createInsertString(PlayList playlist){
        return String.format("INSERT INTO %s (%s) VALUES ('%s')",TABLENAME, COLUMN_NAME, playlist.getName());
    }
    
    /**
     * Creates query selecting users with given username.
     * @param username Username of user
     * @return Query for selecting users with given username.
     */
    private static String getPlaylistWithName(String name){
        return ALLLISTS + " WHERE "+ COLUMN_NAME + " = '" + name + "'";
    }
}
