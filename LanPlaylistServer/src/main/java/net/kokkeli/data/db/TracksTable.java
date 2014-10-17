package net.kokkeli.data.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
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

    private final IConnectionStorage storage;

    /**
     * Creates TracksTable with given databaselocation
     * 
     * @param databaseLocation
     */
    public TracksTable(IConnectionStorage storage) {
        this.storage = storage;
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
    @SuppressWarnings("resource")
    public Track get(final long id) throws DatabaseException, NotFoundInDatabase {
        try (Connection connection = storage.getConnection()){
            try (Statement statement = connection.createStatement()){
                ResultSet rs = statement.executeQuery(getSingleItemQuery(id));
                while(rs.next())
                {
                    return createTrack(rs);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Getting user failed.", e);
        }
        throw new NotFoundInDatabase("No track with id: " + id + " in database.");
    }
    
    /**
     * Returns all tracks from database. User only holds Id.
     * 
     * @return Collection of tracks
     * @throws DatabaseException
     *             Thrown if there is a problem with the database
     */
    @SuppressWarnings("resource")
    public ArrayList<Track> get() throws DatabaseException {
        try (Connection connection = storage.getConnection()){
            try (Statement statement = connection.createStatement()){
                ArrayList<Track> tracks = new ArrayList<Track>();
                ResultSet rs = statement.executeQuery(ALLTRACKS);
                while(rs.next())
                {
                    tracks.add(createTrack(rs));
                }
                return tracks;
            }
        } catch (SQLException e) {
            throw new DatabaseException("Getting user failed.", e);
        }
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
        try (Connection connection = storage.getConnection()){
            try (Statement statement = connection.createStatement()){
                //TODO FIX
                int id = statement.executeUpdate(insertItemRow(newTrack), Statement.RETURN_GENERATED_KEYS);
                newTrack.setId(id);
                return newTrack;
            }
        } catch (SQLException e) {
            throw new DatabaseException("Getting user failed.", e);
        }
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
        try (Connection connection = storage.getConnection()){
            try (Statement statement = connection.createStatement()){
                statement.executeUpdate(updateItemRow(track));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Getting user failed.", e);
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
        return String.format("UPDATE %s SET %s='%s', %s='%s', %s='%s' WHERE Id = %s", TABLENAME, COLUMN_TRACK,
                track.getTrackName(), COLUMN_ARTIST, track.getArtist(), COLUMN_LOCATION, track.getLocation(),
                track.getId());
    }
    
    private static Track createTrack(ResultSet rs) throws SQLException{
        Track track = new Track(rs.getLong(COLUMN_ID));
        track.setTrackName(rs.getString(COLUMN_TRACK));
        track.setArtist(rs.getString(COLUMN_ARTIST));
        track.setLocation(rs.getString(COLUMN_LOCATION));

        User uploader = new User(rs.getLong(COLUMN_UPLOADER), null, Role.NONE);
        track.setUploader(uploader);
        return track;
    }
}
