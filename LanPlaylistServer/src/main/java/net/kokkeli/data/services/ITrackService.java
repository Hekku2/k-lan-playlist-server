package net.kokkeli.data.services;

import java.util.Collection;

import net.kokkeli.data.db.NotFoundInDatabaseException;
import net.kokkeli.data.dto.Track;

/**
 * Interface defining track service
 * @author Hekku2
 *
 */
public interface ITrackService {

    /**
     * Returns list of all tracks and verifies.
     * @return
     * @throws ServiceException
     */
    Collection<Track> getAndVerifyTracks() throws ServiceException;

    /**
     * Returns single track with given id
     * @param id Id of track
     * @return Track matching the id
     * @throws NotFoundInDatabaseException Thrown if track is not in database
     * @throws ServiceException Thrown if there is a problem with the database
     */
    Track get(long id) throws NotFoundInDatabaseException, ServiceException;

    /**
     * Updates track
     * @param track Track to update
     */
    void update(Track track) throws NotFoundInDatabaseException, ServiceException;
}
