package net.kokkeli.data;

import java.util.Date;

/**
 * Log row
 * @author Hekku2
 *
 */
public class LogRow {
    private String message;
    private LogSeverity severity;
    private String source;
    private Date timestamp;
    private long id;
    
    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }
    /**
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }
    /**
     * @return the severity
     */
    public LogSeverity getSeverity() {
        return severity;
    }
    /**
     * @param severity the severity to set
     */
    public void setSeverity(LogSeverity severity) {
        this.severity = severity;
    }
    /**
     * @return the source
     */
    public String getSource() {
        return source;
    }
    /**
     * @param source the source to set
     */
    public void setSource(String source) {
        this.source = source;
    }
    /**
     * @return the timestamp
     */
    public Date getTimestamp() {
        return timestamp;
    }
    /**
     * @param timestamp the timestamp to set
     */
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
    /**
     * @return the id
     */
    public long getId() {
        return id;
    }
    /**
     * @param id the id to set
     */
    public void setId(long id) {
        this.id = id;
    }
}
