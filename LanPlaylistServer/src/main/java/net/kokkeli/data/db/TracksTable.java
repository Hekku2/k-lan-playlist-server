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
    private static final String COLUMN_TRACK = "Track";
    private static final String COLUMN_ARTIST = "Artist";
    private static final String COLUMN_LOCATION = "Location";
    
    private static final String INSERT = "INSERT INTO "+ TABLENAME +" ("+ COLUMN_TRACK +","+ COLUMN_ARTIST+", "+ COLUMN_LOCATION +") VALUES ";
    
    private final String databaseLocation;
    
    /**
     * Creates TracksTable with given databaselocation
     * @param databaseLocation
     */
    public TracksTable(String databaseLocation) {
        this.databaseLocation = databaseLocation;
    }

    /**
     * Returns track with given id.
     * @param id Id of track
     * @return Track Fetched track.
     * @throws DatabaseException Thrown if there is problem with database.
     * @throws NotFoundInDatabase Thrown if track is not in database.
     */
    public Track get(long id) throws DatabaseException, NotFoundInDatabase {
        SQLiteConnection db = new SQLiteConnection(new File(databaseLocation));
        Track track = null;
        
        try {
            db.open(false);
            SQLiteStatement st = db.prepare(getSingleItemQuery(id));
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
            throw new DatabaseException("Unable to get tracks for playlist with Id: " + id, e);
        }
        
        if (track == null)
            throw new NotFoundInDatabase("No track with id: " + id + " in database.");
        
        return track;
    }

    /**
     * Inserts track to database.
     * @param newTrack Track to insert
     * @return Database id of added track.
     * @throws DatabaseException thrown if there is problem with the database.
     */
    public long insert(Track newTrack) throws DatabaseException {
        SQLiteConnection db = new SQLiteConnection(new File(databaseLocation));
        
        long id;
        try {
            db.open(false);
            SQLiteStatement st = db.prepare(insertItemRow(newTrack));
            try {
                st.stepThrough();
            } finally {
                st.dispose();
            }
            
            id = db.getLastInsertId();
            db.dispose();
        } catch (SQLiteException e) {
            throw new DatabaseException("Unabe to insert track to database.", e);
        }
        
        return id;
    }
    
    /**
     * Creates query selecting single user.
     * @param id Id of wanted user
     * @return Query for selecting single user.
     */
    private static String getSingleItemQuery(long id){
        return ALLTRACKS + " WHERE "+ COLUMN_ID+" = " + id;
    }

    /**
     * Creates insert statement
     * @param track Track to insert
     * @return Insert statement
     */
    private static String insertItemRow(Track track){
        return INSERT + "('" + track.getTrackName() + "','" + track.getArtist() + "','" + track.getLocation() + "')";
    }
}