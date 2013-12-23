package net.kokkeli.player;

import net.kokkeli.data.db.NotFoundInDatabase;
import net.kokkeli.data.services.ServiceException;

/**
 * Interface for player
 * @author Hekku2
 *
 */
public interface IPlayer {
    
    /**
     * Continues playing current 
     * @throws ServiceException 
     */
    void play() throws ServiceException;
    
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

    /**
     * Returns true if player has playlist selected for playing
     * @return If true, playlist is selected for playing
     */
    boolean playlistPlaying();

    /**
     * Selects new playlist for playing
     * @param id If of the playlist
     * @throws NotFoundInDatabase Thrown if playlist is not found in database
     * @throws ServiceException Thrown if there is a problem with the database
     */
    void selectPlaylist(long id) throws NotFoundInDatabase, ServiceException;
    
    /**
     * Returnc true, if player can begin playback. (There is anything in queue or playlist is selected)
     * @return true, if play can be used.
     */
    boolean readyForPlay();
}
