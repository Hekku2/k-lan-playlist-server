package net.kokkeli.data;

public interface ILogger {
    /**
     * Logs given message with given severity
     * @param message Message
     * @param severity Severity
     */
    public void log(String message, int severity);
}
