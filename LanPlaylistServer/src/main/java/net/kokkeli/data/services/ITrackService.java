package net.kokkeli.data.services;

import java.util.Collection;

import net.kokkeli.data.Track;

public interface ITrackService {

    /**
     * Returns list of all tracks and verifies.
     * @return
     * @throws ServiceException
     */
    Collection<Track> getAndVerifyTracks() throws ServiceException;

}
