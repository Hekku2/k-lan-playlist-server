package net.kokkeli.data.services;

import java.util.Collection;

import net.kokkeli.data.PlayListItem;
import net.kokkeli.data.db.NotFoundInDatabase;

public interface IPlaylistService {
    Collection<PlayListItem> getPlaylist(long currentPlaylist) throws ServiceException, NotFoundInDatabase;

}
