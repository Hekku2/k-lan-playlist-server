package net.kokkeli.settings;

public class SettingsParseException extends Exception{
    private static final long serialVersionUID = -3279746477114305191L;

    /**
     * Creates new SettingsParseException with given message and inner exception
     * @param message Message
     * @param inner Inner exception
     */
    public SettingsParseException(String message, Exception inner){
        super(message, inner);
    }
    
    /**
     * Creates new SettingsParseException.
     * @param message Message
     */
    public SettingsParseException(String message){
        super(message);
    }
}
