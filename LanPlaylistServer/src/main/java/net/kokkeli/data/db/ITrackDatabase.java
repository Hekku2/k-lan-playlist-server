package net.kokkeli.data.db;

import net.kokkeli.data.Track;

public interface ITrackDatabase extends IDatabase<Track>{

    /**
     * Checks if track exists in id
     * @param track Track
     * @return True, if track exists
     * @throws DatabaseException Thrown if there is a problem with the database
     */
    boolean exists(Track track) throws DatabaseException;

}
