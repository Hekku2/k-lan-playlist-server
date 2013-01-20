package net.kokkeli.data.db;

import java.io.File;

import com.almworks.sqlite4java.SQLiteConnection;
import com.almworks.sqlite4java.SQLiteException;
import com.almworks.sqlite4java.SQLiteStatement;

import net.kokkeli.data.Track;

public class TracksTable {
    private static final String TABLENAME = "tracks";
    private static final String ALLTRACKS = "SELECT * FROM " + TABLENAME;
    private static final String COLUMN_ID = "Id";
    
    private final String databaseLocation;
    
    /**
     * Creates TracksTable with given databaselocation
     * @param databaseLocation
     */
    public TracksTable(String databaseLocation) {
        this.databaseLocation = databaseLocation;
    }

    public Track get(long item) throws DatabaseException, NotFoundInDatabase {
        SQLiteConnection db = new SQLiteConnection(new File(databaseLocation));
        Track track = null;
        
        try {
            db.open(false);
            SQLiteStatement st = db.prepare(getSingleItemQuery(item));
            try {
                while (st.step()) {
                    track = new Track(st.columnLong(0));
                    track.setTrackName(st.columnString(1));
                    track.setArtist(st.columnString(2));
                    track.setLocation(st.columnString(3));
                }
            } finally {
                st.dispose();
            }
            db.dispose();
        } catch (SQLiteException e) {
            throw new DatabaseException("Unable to get tracks for playlist with Id: " + item, e);
        }
        
        if (track == null)
            throw new NotFoundInDatabase("No track with id: " + item + " in database.");
        
        return track;
    }

    /**
     * Creates query selecting single user.
     * @param id Id of wanted user
     * @return Query for selecting single user.
     */
    private static String getSingleItemQuery(long id){
        return ALLTRACKS + " WHERE "+ COLUMN_ID+" = " + id;
    }
}
