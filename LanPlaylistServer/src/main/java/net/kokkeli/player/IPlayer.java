package net.kokkeli.player;

/**
 * Interface for player
 * @author Hekku2
 *
 */
public interface IPlayer {
    
    /**
     * Continues playing current 
     */
    void play();
    
    /**
     * Pauses playing current track
     */
    void pause();
    
    /**
     * Title of currently playing track
     * @return Title of currently playing track
     */
    String getTitle();

    /**
     * Currently playing playlist id.
     * @return Currently playing playlist id.
     */
    long getCurrentPlaylistId() throws NotPlaylistPlayingException;
    
    /**
     * Adds file to playing queue
     * @param file
     */
    void addToQueue(String file);
}
