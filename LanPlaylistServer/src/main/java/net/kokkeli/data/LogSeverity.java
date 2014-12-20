package net.kokkeli.data;

public enum LogSeverity {
    TRACE(0),
    DEBUG(1),
    INFO(2),
    ERROR(3);
    
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
    
    /**
     * Creates log severity from id
     * @param id
     * @return
     * @throws IllegalArgumentException Thrown if value can't be parsed to log severity
     */
    public static LogSeverity getSeverity(int value){
        
        for (LogSeverity severity : LogSeverity.values()) {
            if (severity.getSeverity() == value){
                return severity;
            }
        }
        throw new IllegalArgumentException("There was no severity with value: " + value);
    }
}
