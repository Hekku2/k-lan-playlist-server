package net.kokkeli.data.services;

import java.util.Collection;

import net.kokkeli.data.Track;

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
     */
    Track get(long id);

}
