package net.kokkeli.data.db;

import java.util.Collection;

public interface IPlaylistDatabase extends IDatabase<PlayList>{

    /**
     * Returns collection of playlists. Doesn't fetch tracks.
     * @return Collection of playlists
     * @throws DatabaseException Thrown if there is a problem with the database.
     */
    Collection<PlayList> getOnlyIdAndName() throws DatabaseException;

    /**
     * Updates playlist.
     * Note: If playlist is empty, all tracks are removed.
     * @param playlist Playlist to update
     * @throws DatabaseException Thrown if thre is problem in database.
     * @throws NotFoundInDatabase Thrown if playlist with given Id did not exist.
     */
    void update(PlayList playlist) throws DatabaseException, NotFoundInDatabase;

}
