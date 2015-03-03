package net.kokkeli.data.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import net.kokkeli.data.dto.FetchRequest;
import net.kokkeli.data.dto.FetchStatus;
import net.kokkeli.data.dto.Track;
import net.kokkeli.data.dto.UploadType;

/**
 * Table representing fetch_requests table in database
 * @author Hekku2
 */
public class FetchRequestsTable {
    private static final String TABLENAME = "fetch_requests";
    private static final String ALLREQUESTS = "SELECT * FROM " + TABLENAME;
    private static final String DATEFORMAT = "yyyy-MM-dd hh:mm:ss";
    private static final SimpleDateFormat formatter = new SimpleDateFormat(DATEFORMAT);
    
    private static final String COLUMN_ID = "Id";
    private static final String COLUMN_LOCATION = "Location";
    private static final String COLUMN_HANDLER = "Handler";
    private static final String COLUMN_DESTINATIONFILE = "DestinationFile";
    private static final String COLUMN_LASTUPDATED = "LastUpdated";
    private static final String COLUMN_STATUS = "FetchStatus";
    private static final String COLUMN_TRACK = "Track";
    
    private final IConnectionStorage storage;
    
    /**
     * Database location
     * @param queue Queue
     */
    public FetchRequestsTable(IConnectionStorage storage){
        this.storage = storage;
    }

    /**
     * Fetches all fetch requests from database.
     * Note: Only track id is included
     * @return
     * @throws DatabaseException
     */
    @SuppressWarnings("resource")
    public ArrayList<FetchRequest> get() throws DatabaseException {
        ArrayList<FetchRequest> requests = new ArrayList<FetchRequest>();
        try (Connection connection = storage.getConnection()){
            try (Statement statement = connection.createStatement()){
                ResultSet rs = statement.executeQuery(ALLREQUESTS);
                while(rs.next())
                {
                    requests.add(createFetchRequest(rs));
                }
            } 
            return requests;
        } catch (SQLException | ParseException e) {
            throw new DatabaseException("Getting fetch requests failed.", e);
        }
    }
    
    /**
     * Oldest undhandled request or null
     * @param handler Handler
     * @return Oldest unhandled request or null.
     * @throws DatabaseException Thrown if there is a problem with the database.
     */
    @SuppressWarnings("resource")
    public FetchRequest oldestUnhandledfetchRequest(final String handler) throws DatabaseException {
        String command = String.format("%s WHERE FetchStatus = %s AND Handler = '%s' ORDER BY LastUpdated DESC LIMIT 1;", ALLREQUESTS, FetchStatus.WAITING.getStatus(), handler);
        
        try (Connection connection = storage.getConnection()){
            try (Statement statement = connection.createStatement()){
                ResultSet rs = statement.executeQuery(command);
                while(rs.next())
                {
                    return createFetchRequest(rs);
                }
                return null;
            } 
        } catch (SQLException | ParseException e) {
            throw new DatabaseException("Getting oldest fetch request failed.", e);
        }
    }
    
    /**
     * Inserts fetch request to database. 
     * @param item Fetch request
     * @return Added fetchrequest with new id.
     * @throws DatabaseException Thrown if there is a problem with the database.
     */
    public FetchRequest insert(final FetchRequest item) throws DatabaseException {
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
            throw new DatabaseException("Inserting fetch request failed.", e);
        }
    }
    
    /**
     * Updates fetchrequest to wanted status
     * @param id Id of track
     * @param status New status
     * @throws DatabaseException Thrown if there is a problem with the database.
     */
    public void update(final long id, final FetchStatus status) throws DatabaseException {
        try (Connection connection = storage.getConnection()){
            try (Statement statement = connection.createStatement()){
                statement.executeUpdate(createStatusUpdateString(id, status));
            } 
        } catch (SQLException e) {
            throw new DatabaseException("Updating fetch request failed.", e);
        }
    }
    
    /**
     * Removes fetch requests with given status
     * @param status Status
     * @throws DatabaseException Thrown if there is a problem with the database
     */
    public void removeWithStatus(final FetchStatus status) throws DatabaseException {
        try (Connection connection = storage.getConnection()){
            try (Statement statement = connection.createStatement()){
                statement.executeUpdate(createStatusRemove(status));
            } 
        } catch (SQLException e) {
            throw new DatabaseException("Removing fetch requests with status failed.", e);
        }
    }
    
    public void remove(final long requestId) throws DatabaseException {
        try (Connection connection = storage.getConnection()){
            try (Statement statement = connection.createStatement()){
                statement.executeUpdate(createRemove(requestId));
            } 
        } catch (SQLException e) {
            throw new DatabaseException("removing fetch request failed.", e);
        }
    }
    
    /**
     * Create remove command that removes request with given id
     * @param requestId Request id
     * @return Create String
     */
    private static String createRemove(long requestId) {
        return String.format("DELETE FROM %s WHERE Id=%s;", TABLENAME, requestId);
    }

    /**
     * Creates command that removes all requests with given status
     * @param status Status
     * @return Created string
     */
    private static String createStatusRemove(FetchStatus status) {
        return String.format("DELETE FROM %s WHERE FetchStatus=%s;", TABLENAME, status.getStatus());
    }

    /**
     * Creates update string for fetch status
     * @param id Id to update
     * @param status New status
     * @return Created string
     */
    private static String createStatusUpdateString(long id, FetchStatus status) {
        return String.format("UPDATE %s SET FetchStatus=%s WHERE Id=%s;",TABLENAME, status.getStatus(), id);
    }

    /**
     * Creates insert string for fetch request
     * @param item Fetch request to insert
     * @return Insert String
     */
    private static String createInsertString(FetchRequest item) {
        return String.format("INSERT INTO %s(Location, Handler, DestinationFile, LastUpdated, FetchStatus, Track) VALUES ('%s', '%s', '%s', '%s', %s, %s); ",
                TABLENAME,
                item.getLocation(),
                item.getType().getText(),
                item.getDestinationFile(),
                formatter.format(item.getLastUpdated()),
                item.getStatus().getStatus(),
                item.getTrack().getId());
    }

    /**
     * Returns FectchStatus with given id);
     * @param id Id of status
     * @return FectchStatus with given id.
     * @throws IndexOutOfBoundsException Thrown if the is no such status
     */
    private static FetchStatus getStatus(int status){
        for (FetchStatus possible : FetchStatus.values()) {
            if (possible.getStatus() == status){
                return possible;
            }
        }
        throw new IndexOutOfBoundsException(String.format("There was no fetch status with given Id (%s).", status));
    }
    
    private static FetchRequest createFetchRequest(ResultSet rs) throws SQLException, ParseException{
        FetchRequest request = new FetchRequest();
        request.setId(rs.getLong(COLUMN_ID));
        request.setLocation(rs.getString(COLUMN_LOCATION));
        request.setType(UploadType.getUploadType(rs.getString(COLUMN_HANDLER)));
        request.setDestinationFile(rs.getString(COLUMN_DESTINATIONFILE));
        request.setLastUpdated(formatter.parse(rs.getString(COLUMN_LASTUPDATED)));
        request.setStatus(getStatus(rs.getInt(COLUMN_STATUS)));
        request.setTrack(new Track(rs.getLong(COLUMN_TRACK)));
        return request;
    }
}
