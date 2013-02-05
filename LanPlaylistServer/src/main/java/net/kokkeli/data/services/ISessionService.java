package net.kokkeli.data.services;

import net.kokkeli.data.Session;
import net.kokkeli.data.User;
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

    /**
     * Creates session for user. Session is added stash.
     * @param user User
     * @return Created session
     */
    Session createSession(User user);

    /**
     * Sets error for given session
     * @param authId Auth id of session.
     * @param error Error to set
     */
    void setError(String authId, String error);

    /**
     * Clears error for given session
     * @param authId Auth id of session
     */
    void clearError(String authId);
}
