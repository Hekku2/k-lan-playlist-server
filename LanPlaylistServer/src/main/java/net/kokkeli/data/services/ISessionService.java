package net.kokkeli.data.services;

import net.kokkeli.data.Session;
import net.kokkeli.data.db.NotFoundInDatabase;

/**
 * Interface for sessionservice
 * @author Hekku2
 *
 */
public interface ISessionService {

    /**
     * Returns session with given authentication
     * @param authId Authentication id
     * @return Session
     * @throws NotFoundInDatabase Thrown if session with given authentication is not found.
     */
    Session get(String authId) throws NotFoundInDatabase;
}
