package net.kokkeli.data.db;

import java.util.Collection;

public interface IPlaylistDatabase extends IDatabase<PlayList>{

    /**
     * Returns collection of playlists. Doesn't fetch tracks.
     * @return Collection of playlists
     * @throws DatabaseException Thrown if there is a problem with the database.
     */
    Collection<PlayList> getOnlyIdAndName() throws DatabaseException;

}
