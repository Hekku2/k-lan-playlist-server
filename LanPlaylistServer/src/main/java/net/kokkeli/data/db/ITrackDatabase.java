package net.kokkeli.data.db;

import com.sun.jersey.api.NotFoundException;

import net.kokkeli.data.Track;

public interface ITrackDatabase extends IDatabase<Track>{

    /**
     * Checks if track exists in id
     * @param track Track
     * @return True, if track exists
     * @throws DatabaseException Thrown if there is a problem with the database
     */
    boolean exists(Track track) throws DatabaseException;

    /**
     * Updates given track
     * @param track Track to update
     * @throws DatabaseException Thrown if there is a problem with the database
     * @throws NotFoundException Throw if item is not found
     * @throws NotFoundInDatabase 
     */
    void update(Track track) throws DatabaseException, NotFoundInDatabase;
}
