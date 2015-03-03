package net.kokkeli.data.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;

import net.kokkeli.data.dto.LogRow;
import net.kokkeli.data.dto.LogSeverity;

public class LogTable {
    private static final String TABLENAME = "logs";
    private static final String COLUMN_TIMESTAMP = "Timestamp";
    private static final String COLUMN_SEVERITY = "Severity";
    private static final String COLUMN_MESSAGE = "Message";
    private static final String COLUMN_SOURCE = "Source";
    private static final String DATEFORMAT = "yyyy-MM-dd hh:mm:ss";
    private static final SimpleDateFormat formatter = new SimpleDateFormat(DATEFORMAT);

    private static final String GETALL = "SELECT Timestamp, Severity, Message, Source FROM " + TABLENAME;

    private final IConnectionStorage storage;

    /**
     * Creates a new instance of log table
     * 
     * @param queue
     *            Queue used for jobs
     */
    public LogTable(IConnectionStorage storage) {
        this.storage = storage;
    }

    /**
     * Inserts log row to database
     * 
     * @param item
     * @return
     * @throws DatabaseException
     */
    public void insert(final LogRow item) throws DatabaseException {
        try (Connection connection = storage.getConnection()){
            try (Statement statement = connection.createStatement()){
                statement.executeUpdate(createInsertString(item));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Inserting log row failed.", e);
        }
    }

    /**
     * Gets all log lines from database.
     * 
     * @return All log lines from database.
     * @throws DatabaseException
     *             Thrown if there is a problem with the database
     */
    @SuppressWarnings("resource")
    public Collection<LogRow> get() throws DatabaseException {
        try (Connection connection = storage.getConnection()){
            try (Statement statement = connection.createStatement()){
                ArrayList<LogRow> rows = new ArrayList<LogRow>();
                ResultSet rs = statement.executeQuery(GETALL);
                while(rs.next())
                {
                    rows.add(createLogRow(rs));
                }
                return rows;
            }
        } catch (SQLException | ParseException e) {
            throw new DatabaseException("Getting users failed.", e);
        }
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
    
    private static LogRow createLogRow(ResultSet rs) throws SQLException, ParseException{
        LogRow row = new LogRow();
        row.setTimestamp(formatter.parse(rs.getString(COLUMN_TIMESTAMP)));
        row.setSeverity(LogSeverity.getSeverity(rs.getInt(COLUMN_SEVERITY)));
        row.setMessage(rs.getString(COLUMN_MESSAGE));
        row.setSource(rs.getString(COLUMN_SOURCE));
        return row;
    }
}
