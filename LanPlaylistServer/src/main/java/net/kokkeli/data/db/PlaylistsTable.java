package net.kokkeli.data.db;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import com.almworks.sqlite4java.SQLiteConnection;
import com.almworks.sqlite4java.SQLiteException;
import com.almworks.sqlite4java.SQLiteStatement;

public class PlaylistsTable{
    private static final String TABLENAME = "playlists";
    private static final String ALLLISTS = "SELECT * FROM " + TABLENAME;
    private static final String COLUMN_ID = "Id";
    
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
}
