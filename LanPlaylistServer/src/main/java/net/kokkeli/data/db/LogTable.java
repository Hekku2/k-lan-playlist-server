package net.kokkeli.data.db;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import com.almworks.sqlite4java.SQLiteConnection;
import com.almworks.sqlite4java.SQLiteException;
import com.almworks.sqlite4java.SQLiteStatement;

import net.kokkeli.data.LogRow;
import net.kokkeli.data.LogSeverity;

public class LogTable {
    private static final String TABLENAME = "logs";
    private static final String COLUMN_TIMESTAMP = "Timestamp";
    private static final String COLUMN_SEVERITY = "Severity";
    private static final String COLUMN_MESSAGE = "Message";
    private static final String COLUMN_SOURCE = "Source";
    private static final String DATEFORMAT = "yyyy-MM-dd hh:mm:ss";
    private static final SimpleDateFormat formatter = new SimpleDateFormat(DATEFORMAT);
    
    private static final String GETALL = "SELECT Timestamp, Severity, Message, Source FROM " + TABLENAME;
    
    private final String databaseLocation;
    
    /**
     * Creates a new instance of log table
     * @param databaseLocation
     */
    public LogTable(String databaseLocation) {
        this.databaseLocation = databaseLocation;
    }
    
    /**
     * Inserts log row to database
     * @param item
     * @return
     * @throws DatabaseException
     */
    public LogRow insert(LogRow item) throws DatabaseException {
        SQLiteConnection db = new SQLiteConnection(new File(databaseLocation));
        try {
            db.open(false);
            SQLiteStatement st = db.prepare(createInsertString(item));
            try {
                st.stepThrough();
            } finally {
                st.dispose();
            }

            db.dispose();
        } catch (SQLiteException e) {
            throw new DatabaseException("There was problem with database", e);
        }
        return item;
    }

    /**
     * Gets all log lines from database.
     * @return All log lines from database.
     * @throws DatabaseException Thrown if there is a problem with the database
     */
    public Collection<LogRow> get() throws DatabaseException {
        SQLiteConnection db = new SQLiteConnection(new File(databaseLocation));
        ArrayList<LogRow> logs = new ArrayList<LogRow>();

        try {
            db.open(false);
            SQLiteStatement st = db.prepare(GETALL);
            try {
                while (st.step()) {
                    LogRow row = new LogRow();
                    row.setTimestamp(formatter.parse(st.columnString(0)));
                    row.setMessage(st.columnString(2));
                    row.setSource(st.columnString(3));
                    row.setSeverity(LogSeverity.getSeverity(st.columnInt(1)));
                    
                    logs.add(row);
                }
            } catch (ParseException e) {
                throw new DatabaseException("There was an invalid timestamp format in some column.", e);
            } finally {
                st.dispose();
            }
            db.dispose();
        } catch (SQLiteException e) {
            throw new DatabaseException("There was problem with database", e);
        }
        return logs;
    }
    
    /**
     * Creates insert statement for Playlist
     * @param PlayList playlist
     * @return Insert statement
     */
    private static String createInsertString(LogRow item){
        return String.format("INSERT INTO %s (%s, %s, %s, %s) VALUES ('%s', %s, '%s', '%s')",
                TABLENAME,
                COLUMN_TIMESTAMP,
                COLUMN_SEVERITY,
                COLUMN_MESSAGE,
                COLUMN_SOURCE,
                formatter.format(item.getTimestamp()),
                item.getSeverity().getSeverity(),
                item.getMessage(),
                item.getSource());
    }
}
