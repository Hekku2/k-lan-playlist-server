package net.kokkeli.data.db;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import com.almworks.sqlite4java.SQLiteConnection;
import com.almworks.sqlite4java.SQLiteConstants;
import com.almworks.sqlite4java.SQLiteException;
import com.almworks.sqlite4java.SQLiteJob;
import com.almworks.sqlite4java.SQLiteQueue;
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

    private final SQLiteQueue queue;

    /**
     * Creates a new instance of log table
     * 
     * @param queue
     *            Queue used for jobs
     */
    public LogTable(SQLiteQueue queue) {
        this.queue = queue;
    }

    /**
     * Inserts log row to database
     * 
     * @param item
     * @return
     * @throws DatabaseException
     */
    public void insert(final LogRow item) throws DatabaseException {
        queue.execute(new SQLiteJob<Long>() {
            @Override
            protected Long job(SQLiteConnection connection) throws SQLiteException {
                SQLiteStatement st = connection.prepare(createInsertString(item));

                try {
                    st.stepThrough();
                } finally {
                    st.dispose();
                }

                return connection.getLastInsertId();
            }
        });
    }

    /**
     * Gets all log lines from database.
     * 
     * @return All log lines from database.
     * @throws DatabaseException
     *             Thrown if there is a problem with the database
     */
    public Collection<LogRow> get() throws DatabaseException {
        ArrayList<LogRow> logs = queue.execute(new SQLiteJob<ArrayList<LogRow>>() {
            @Override
            protected ArrayList<LogRow> job(SQLiteConnection connection) throws SQLiteException {
                ArrayList<LogRow> logs = new ArrayList<LogRow>();
                connection.setBusyTimeout(2000);
                SQLiteStatement st = connection.prepare(GETALL);
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
                    throw new SQLiteException(SQLiteConstants.SQLITE_MISMATCH,
                            "There was an invalid timestamp format in some column.");
                } finally {
                    st.dispose();
                }

                return logs;
            }
        }).complete();

        return logs;
    }

    /**
     * Creates insert statement for Playlist
     * 
     * @param PlayList
     *            playlist
     * @return Insert statement
     */
    private static String createInsertString(LogRow item) {
        return String.format("INSERT INTO %s (%s, %s, %s, %s) VALUES ('%s', %s, '%s', '%s')", TABLENAME,
                COLUMN_TIMESTAMP, COLUMN_SEVERITY, COLUMN_MESSAGE, COLUMN_SOURCE,
                formatter.format(item.getTimestamp()), item.getSeverity().getSeverity(), item.getMessage(),
                item.getSource());
    }
}
