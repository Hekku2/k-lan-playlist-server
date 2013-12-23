package net.kokkeli.data.db;

import java.util.ArrayList;
import com.almworks.sqlite4java.SQLiteConnection;
import com.almworks.sqlite4java.SQLiteException;
import com.almworks.sqlite4java.SQLiteJob;
import com.almworks.sqlite4java.SQLiteQueue;
import com.almworks.sqlite4java.SQLiteStatement;

import net.kokkeli.data.Role;
import net.kokkeli.data.Track;
import net.kokkeli.data.User;

/**
 * Class representing Tracks-table in database. Used for operating the specific
 * table only.
 * 
 * @author Hekku2
 * 
 */
public class TracksTable {
    private static final String TABLENAME = "tracks";
    private static final String ALLTRACKS = "SELECT * FROM " + TABLENAME;
    private static final String COLUMN_ID = "Id";
    private static final String COLUMN_TRACK = "Track";
    private static final String COLUMN_ARTIST = "Artist";
    private static final String COLUMN_LOCATION = "Location";
    private static final String COLUMN_UPLOADER = "Uploader";

    private static final String INSERT = "INSERT INTO " + TABLENAME + " (" + COLUMN_TRACK + "," + COLUMN_ARTIST + ", "
            + COLUMN_LOCATION + ", " + COLUMN_UPLOADER + ") VALUES ";

    private final SQLiteQueue queue;

    /**
     * Creates TracksTable with given databaselocation
     * 
     * @param databaseLocation
     */
    public TracksTable(SQLiteQueue queue) {
        this.queue = queue;
    }

    /**
     * Returns track with given id. NOTE: User only contains id, username and
     * role are not set.
     * 
     * @param id
     *            Id of track
     * @return Track Fetched track.
     * @throws DatabaseException
     *             Thrown if there is problem with database.
     * @throws NotFoundInDatabase
     *             Thrown if track is not in database.
     */
    public Track get(final long id) throws DatabaseException, NotFoundInDatabase {
        Track track = queue.execute(new SQLiteJob<Track>() {
            @Override
            protected Track job(SQLiteConnection connection) throws SQLiteException {
                SQLiteStatement st = connection.prepare(getSingleItemQuery(id));

                Track track = null;
                try {
                    while (st.step()) {
                        track = new Track(st.columnLong(0));
                        track.setTrackName(st.columnString(1));
                        track.setArtist(st.columnString(2));
                        track.setLocation(st.columnString(3));

                        User uploader = new User(st.columnLong(4), null, Role.NONE);
                        track.setUploader(uploader);
                    }
                } finally {
                    st.dispose();
                }

                return track;
            }
        }).complete();

        // TODO How to check for database problem?
        if (track == null)
            throw new NotFoundInDatabase("No track with id: " + id + " in database.");

        return track;
    }

    /**
     * Returns all tracks from database. User only holds Id.
     * 
     * @return Collection of tracks
     * @throws DatabaseException
     *             Thrown if there is a problem with the database
     */
    public ArrayList<Track> get() throws DatabaseException {
        ArrayList<Track> tracks = queue.execute(new SQLiteJob<ArrayList<Track>>() {
            @Override
            protected ArrayList<Track> job(SQLiteConnection connection) throws SQLiteException {
                ArrayList<Track> tracks = new ArrayList<Track>();

                SQLiteStatement st = connection.prepare(ALLTRACKS);

                try {
                    while (st.step()) {
                        Track track = new Track(st.columnLong(0));
                        track.setTrackName(st.columnString(1));
                        track.setArtist(st.columnString(2));
                        track.setLocation(st.columnString(3));

                        User uploader = new User(st.columnLong(4), null, Role.NONE);
                        track.setUploader(uploader);
                        tracks.add(track);
                    }
                } finally {
                    st.dispose();
                }

                return tracks;

            }
        }).complete();

        if (tracks == null) {
            throw new DatabaseException("Unable to get tracks from database");
        }

        return tracks;
    }

    /**
     * Inserts track to database.
     * 
     * @param newTrack
     *            Track to insert
     * @return Database id of added track.
     * @throws DatabaseException
     *             thrown if there is problem with the database.
     */
    public Track insert(final Track newTrack) throws DatabaseException {
        Long id = queue.execute(new SQLiteJob<Long>() {
            @Override
            protected Long job(SQLiteConnection connection) throws SQLiteException {
                SQLiteStatement st = connection.prepare(insertItemRow(newTrack));

                try {
                    st.stepThrough();
                } finally {
                    st.dispose();
                }

                return connection.getLastInsertId();
            }
        }).complete();

        if (id == null) {
            throw new DatabaseException("Inserting fetch request failed.");
        }

        newTrack.setId(id);
        return newTrack;
    }

    /**
     * Updates track
     * 
     * @param track
     *            Track
     * @throws DatabaseException
     *             Thrown if there is a problem with the database
     */
    public void update(final Track track) throws DatabaseException {
        queue.execute(new SQLiteJob<Object>() {
            @Override
            protected Object job(SQLiteConnection connection) throws SQLiteException {
                SQLiteStatement st = connection.prepare(updateItemRow(track));
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
     * 
     * @param id
     *            Id of wanted user
     * @return Query for selecting single user.
     */
    private static String getSingleItemQuery(long id) {
        return ALLTRACKS + " WHERE " + COLUMN_ID + " = " + id;
    }

    /**
     * Creates insert statement
     * 
     * @param track
     *            Track to insert
     * @return Insert statement
     */
    private static String insertItemRow(Track track) {
        return INSERT + "('" + track.getTrackName() + "','" + track.getArtist() + "','" + track.getLocation() + "',"
                + track.getUploader().getId() + ")";
    }

    private static String updateItemRow(Track track) {
        /*
         * UPDATE table_name SET column1=value1,column2=value2,... WHERE
         * some_column=some_value;
         */
        return String.format("UPDATE %s SET %s='%s', %s='%s', %s='%s' WHERE Id = %s", TABLENAME, COLUMN_TRACK,
                track.getTrackName(), COLUMN_ARTIST, track.getArtist(), COLUMN_LOCATION, track.getLocation(),
                track.getId());
    }
}
