package net.kokkeli.data.services;

import java.util.Collection;

import net.kokkeli.data.db.NotFoundInDatabase;
import net.kokkeli.data.db.PlayList;

public interface IPlaylistService {
    PlayList getPlaylist(long currentPlaylist) throws ServiceException, NotFoundInDatabase;

    /**
     * Returns colection of ids and name of playlists.
     * @return Collection of playlist containin only name and Id.
     * @throws ServiceException Thrown if there is something wrong with service.
     */
    Collection<PlayList> getIdNames() throws ServiceException;
}
