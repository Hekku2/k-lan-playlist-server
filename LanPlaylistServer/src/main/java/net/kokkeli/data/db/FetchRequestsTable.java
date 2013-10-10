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
    
    /**
     * Database location
     * @param databaseLocation
     */
    public FetchRequestsTable(String databaseLocation){
        this.databaseLocation = databaseLocation;
    }

    /**
     * Fetches all fetch requests from database
     * @return
     * @throws DatabaseException
     */
    public ArrayList<FetchRequest> get() throws DatabaseException {
        SQLiteConnection db = new SQLiteConnection(new File(databaseLocation));
        
        ArrayList<FetchRequest> requests = new ArrayList<FetchRequest>();
        try {
            db.open(false);
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
     * Returns FectchStatus with given id
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
