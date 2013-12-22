package net.kokkeli.data.db;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

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
    
    private final String databaseLocation;
    
    public LogTable(String databaseLocation) {
        this.databaseLocation = databaseLocation;
    }
    
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

    public Collection<LogRow> get() {
        ArrayList<LogRow> mockList = new ArrayList<LogRow>();
        for (int i = 0; i < 200; i++) {
            LogRow row = new LogRow();
            row.setMessage("Kaikki räjähti " + i);
            row.setSeverity(LogSeverity.ERROR);
            row.setSource("Barskaali");
            Date d = new Date();
            row.setTimestamp(d);
            mockList.add(row);
        }
        return mockList;
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
