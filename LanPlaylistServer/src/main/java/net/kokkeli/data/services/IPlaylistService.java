package net.kokkeli.data.services;

import java.util.Collection;

import net.kokkeli.data.PlayList;
import net.kokkeli.data.db.NotFoundInDatabase;

public interface IPlaylistService {
    PlayList getPlaylist(long currentPlaylist) throws ServiceException, NotFoundInDatabase;

    /**
     * Returns colection of ids and name of playlists.
     * @return Collection of playlist containin only name and Id.
     * @throws ServiceException Thrown if there is something wrong with service.
     */
    Collection<PlayList> getIdNames() throws ServiceException;

    /**
     * Updates playlist. Adds missing tracks to database, removes removed tracks from database and updates changed tracks.
     * @param playlist Playlist to update
     * @throws NotFoundInDatabase Thrown if there is no playlist with given id.
     * @throws ServiceException Thrown if there is something wrong with service.
     */
    void update(PlayList playlist) throws NotFoundInDatabase, ServiceException;
    
    /**
     * Adds playlist returns id of added playlist.
     * @param playlist Playlist
     * @return Id of added playlist
     * @throws ServiceException Thrown if there is something wrong with service
     */
    PlayList add(PlayList playlist) throws ServiceException;
}
