package net.kokkeli.data;

public enum LogSeverity {
    TRACE(0),
    DEBUG(1),
    ERROR(2);
    
    private final int severity;
    
    /**
     * Creates role with given id.
     * @param id
     */
    private LogSeverity(int severity){
        this.severity = severity;
    }
    
    /**
     * Id of Role
     * @return
     */
    public int getSeverity(){
        return severity;
    }
}
