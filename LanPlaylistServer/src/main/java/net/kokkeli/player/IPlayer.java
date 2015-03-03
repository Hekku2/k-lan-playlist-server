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
     * Adds file to playing queue
     * @param file
     * @throws ServiceException 
     */
    void addToQueue(String file) throws ServiceException;

    /**
     * Selects new playlist for playing
     * @param id If of the playlist
     * @throws NotFoundInDatabaseException Thrown if playlist is not found in database
     * @throws ServiceException Thrown if there is a problem with the database
     */
    void selectPlaylist(long id) throws NotFoundInDatabaseException, ServiceException;
    
    /**
     * Returns status of player
     * @return Player status
     * @throws ServiceException Thrown if there is a problem with the service
     */
    PlayerStatus status() throws ServiceException;
}
