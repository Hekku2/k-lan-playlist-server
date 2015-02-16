package net.kokkeli;

public class SettingsLoadException extends Exception{
    private static final long serialVersionUID = 1L;

    /**
     * Creates new SettingsLoadException with given message and inner exception
     * @param message Message
     * @param inner Inner exception
     */
    public SettingsLoadException(String message, Exception inner){
        super(message, inner);
    }
    
    /**
     * Creates new SettingsLoadException.
     * @param message Message
     */
    public SettingsLoadException(String message){
        super(message);
    }
}
