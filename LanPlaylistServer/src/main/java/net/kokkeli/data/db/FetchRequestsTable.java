package net.kokkeli.data.db;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import com.almworks.sqlite4java.SQLiteConnection;
import com.almworks.sqlite4java.SQLiteException;
import com.almworks.sqlite4java.SQLiteStatement;

import net.kokkeli.data.FetchRequest;
import net.kokkeli.data.FetchStatus;
import net.kokkeli.data.Track;

/**
 * Table representing fetch_requests table in database
 * @author Hekku2
 */
public class FetchRequestsTable {
    private final String databaseLocation;
    private static final String TABLENAME = "fetch_requests";
    private static final String ALLREQUESTS = "SELECT * FROM " + TABLENAME;
    private static final String DATEFORMAT = "yyyy-MM-dd hh:mm:ss";
    private static final SimpleDateFormat formatter = new SimpleDateFormat(DATEFORMAT);
    
    private static final int IDCOLUMN = 0;
    private static final int LOCATIONCOLUMN = 1;
    private static final int HANDLERCOLUMN = 2;
    private static final int DESTINATIONFILECOLUMN = 3;
    private static final int LASTUPDATEDCOLUMN = 4;
    private static final int STATUSCOLUMN = 5;
    private static final int TRACKCOLUMN = 6;
    
    /**
     * Database location
     * @param databaseLocation
     */
    public FetchRequestsTable(String databaseLocation){
        this.databaseLocation = databaseLocation;
    }

    /**
     * Fetches all fetch requests from database.
     * Note: Only track id is included
     * @return
     * @throws DatabaseException
     */
    public ArrayList<FetchRequest> get() throws DatabaseException {
        SQLiteConnection db = new SQLiteConnection(new File(databaseLocation));
        
        ArrayList<FetchRequest> requests = new ArrayList<FetchRequest>();
        try {
            db.openReadonly();
            SQLiteStatement st = db.prepare(ALLREQUESTS);
            try {
                while (st.step()) {
                    FetchRequest request = new FetchRequest();
                    
                    request.setId(st.columnLong(IDCOLUMN));
                    request.setLocation(st.columnString(LOCATIONCOLUMN));
                    request.setHandler(st.columnString(HANDLERCOLUMN));
                    request.setDestinationFile(st.columnString(DESTINATIONFILECOLUMN));
                    
                    request.setLastUpdated(formatter.parse(st.columnString(LASTUPDATEDCOLUMN)));
                    request.setStatus(getStatus(st.columnInt(STATUSCOLUMN)));
                    request.setTrack(new Track(st.columnLong(TRACKCOLUMN)));
                    
                    requests.add(request);
                }
            } finally {
                st.dispose();
            }
            db.dispose();
        } catch (ParseException e) {
            throw new DatabaseException("There was invalid format in LastUpdated field.", e);
        } catch (SQLiteException e) {
            throw new DatabaseException("Unabe to get requests from database.", e);
        }
        return requests;
    }
    
    /**
     * Oldest undhandled request or null
     * @param handler Handler
     * @return Oldest unhandled request or null.
     * @throws DatabaseException Thrown if there is a problem with the database.
     */
    public FetchRequest oldestUnhandledfetchRequest(String handler) throws DatabaseException {
        SQLiteConnection db = new SQLiteConnection(new File(databaseLocation));
        FetchRequest request = null;
        try {
            db.openReadonly();
            
            String command = String.format("%s WHERE FetchStatus = %s AND Handler = '%s' ORDER BY LastUpdated DESC LIMIT 1;",ALLREQUESTS, FetchStatus.WAITING.getStatus(), handler);
            SQLiteStatement st = db.prepare(command);
            
            try {
                while (st.step()) {
                    request = new FetchRequest();
                    
                    request.setId(st.columnLong(IDCOLUMN));
                    request.setLocation(st.columnString(LOCATIONCOLUMN));
                    request.setHandler(st.columnString(HANDLERCOLUMN));
                    request.setDestinationFile(st.columnString(DESTINATIONFILECOLUMN));
                    
                    request.setLastUpdated(formatter.parse(st.columnString(LASTUPDATEDCOLUMN)));
                    request.setStatus(getStatus(st.columnInt(STATUSCOLUMN)));
                    request.setTrack(new Track(st.columnLong(TRACKCOLUMN)));
                    
                }
            } finally {
                st.dispose();
            }
            db.dispose();
        } catch (ParseException e) {
            throw new DatabaseException("There was invalid format in LastUpdated field.", e);
        } catch (SQLiteException e) {
            throw new DatabaseException("Unable to get requests from database.", e);
        }       
        return request;
    }
    
    /**
     * Inserts fetch request to database. 
     * @param item Fetch request
     * @return Added fetchrequest with new id.
     * @throws DatabaseException Thrown if there is a problem with the database.
     */
    public FetchRequest insert(FetchRequest item) throws DatabaseException {
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
            throw new DatabaseException("Unable to insert request to database.", e);
        }
        item.setId(id);
        return item;
    }
    
    /**
     * Updates fetchrequest to wanted status
     * @param id Id of track
     * @param status New status
     * @throws DatabaseException Thrown if there is a problem with the database.
     */
    public void update(long id, FetchStatus status) throws DatabaseException {
        SQLiteConnection db = new SQLiteConnection(new File(databaseLocation));
        try {
            db.open(false);
            SQLiteStatement st = db.prepare(createStatusUpdateString(id, status));
            try {
                st.stepThrough();
            } finally {
                st.dispose();
            }
            db.dispose();
        } catch (SQLiteException e) {
            throw new DatabaseException(String.format("Unable to update fetch status of item with id %s.", id), e);
        }
    }
    
    /**
     * Creates update string for fetch status
     * @param id Id to update
     * @param status New status
     * @return Created string
     */
    private String createStatusUpdateString(long id, FetchStatus status) {
        return String.format("UPDATE %s SET FetchStatus=%s WHERE Id=%s;",TABLENAME, status.getStatus(), id);
    }

    /**
     * Creates insert string for fetch request
     * @param item Fetch request to insert
     * @return Insert String
     */
    private String createInsertString(FetchRequest item) {
        return String.format("INSERT INTO %s(Location, Handler, DestinationFile, LastUpdated, FetchStatus, Track) VALUES ('%s', '%s', '%s', '%s', %s, %s); ",
                TABLENAME,
                item.getLocation(),
                item.getHandler(),
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
}
