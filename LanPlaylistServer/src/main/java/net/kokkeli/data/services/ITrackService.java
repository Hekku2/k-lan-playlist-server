package net.kokkeli.data.services;

import java.util.Collection;

import net.kokkeli.data.Track;
import net.kokkeli.data.db.NotFoundInDatabase;

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
     * @throws NotFoundInDatabase Thrown if track is not in database
     * @throws ServiceException Thrown if there is a problem with the database
     */
    Track get(long id) throws NotFoundInDatabase, ServiceException;

}
