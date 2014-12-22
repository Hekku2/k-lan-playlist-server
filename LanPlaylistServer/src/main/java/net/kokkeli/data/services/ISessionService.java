package net.kokkeli.data.services;

import net.kokkeli.data.Session;
import net.kokkeli.data.User;
import net.kokkeli.data.db.NotFoundInDatabaseException;

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
     * @throws NotFoundInDatabaseException Thrown if session with given authentication is not found.
     */
    Session get(String authId) throws NotFoundInDatabaseException;

    /**
     * Creates session for user. Session is added stash.
     * @param user User
     * @return Created session
     */
    Session createSession(User user);

    /**
     * Sets error for given session. If sessions doesn't exist, doesn't do anything.
     * @param authId Auth id of session.
     * @param error Error to set
     */
    void setError(String authId, String error);

    /**
     * Clears error for given session. If sessions doesn't exist, doesn't do anything.
     * @param authId Auth id of session
     */
    void clearError(String authId);

    /**
     * Sets info for given session. If session doesn't exist, doesn't do anything.
     * @param authId Session id
     * @param info Info for session
     */
    void setInfo(String authId, String info);

    /**
     * Clears info for given session. If session doesn't exist, doesn't do anything.
     * @param authId Auth if of session
     */
    void clearInfo(String authId);
}
