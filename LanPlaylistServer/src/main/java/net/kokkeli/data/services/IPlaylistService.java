package net.kokkeli.data.services;

import net.kokkeli.data.db.NotFoundInDatabase;
import net.kokkeli.data.db.PlayList;

public interface IPlaylistService {
    PlayList getPlaylist(long currentPlaylist) throws ServiceException, NotFoundInDatabase;

}
