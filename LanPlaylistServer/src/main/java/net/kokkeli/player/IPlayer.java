package net.kokkeli.player;

import net.kokkeli.data.db.NotFoundInDatabaseException;
import net.kokkeli.data.services.ServiceException;

/**
 * TODO Refactor this interface to include less calls (just player status)
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
     * @throws ServiceException 
     */
    void pause() throws ServiceException;
    
    /**
     * Title of currently playing track
     * @return Title of currently playing track
     * @throws ServiceException 
     */
    String getTitle() throws ServiceException;

    /**
     * Currently playing playlist id.
     * @return Currently playing playlist id.
     * @throws ServiceException 
     */
    long getCurrentPlaylistId() throws NotPlaylistPlayingException, ServiceException;
    
    /**
     * Adds file to playing queue
     * @param file
     * @throws ServiceException 
     */
    void addToQueue(String file) throws ServiceException;

    /**
     * Returns true if player has playlist selected for playing
     * @return If true, playlist is selected for playing
     * @throws ServiceException 
     */
    boolean playlistPlaying() throws ServiceException;

    /**
     * Selects new playlist for playing
     * @param id If of the playlist
     * @throws NotFoundInDatabaseException Thrown if playlist is not found in database
     * @throws ServiceException Thrown if there is a problem with the database
     */
    void selectPlaylist(long id) throws NotFoundInDatabaseException, ServiceException;
    
    /**
     * Returnc true, if player can begin playback. (There is anything in queue or playlist is selected)
     * @return true, if play can be used.
     * @throws ServiceException 
     */
    boolean readyForPlay() throws ServiceException;
}
