package net.kokkeli.data.services;

import java.util.HashMap;
import java.util.UUID;

import com.google.inject.Inject;

import net.kokkeli.data.ILogger;
import net.kokkeli.data.LogSeverity;
import net.kokkeli.data.Session;
import net.kokkeli.data.User;
import net.kokkeli.data.db.NotFoundInDatabaseException;

/**
 * Service for sessiondata
 * @author Hekku2
 *
 */
public class SessionService implements ISessionService{
    private ILogger logger;
    private HashMap<String, Session> sessions;
    
    /**
     * Creates user service with given logger
     * @param logger Logger.
     */
    @Inject
    public SessionService(ILogger logger){
        this.logger = logger;
        
        sessions = new HashMap<String, Session>();
    }
    
    /**
     * Returns Session for authentication id.
     * @param authId Authentication id of session
     * @throws NotFoundInDatabaseException thrown if there is no session with given authentication.
     */
    @Override
    public Session get(String authId) throws NotFoundInDatabaseException{
        if (!sessions.containsKey(authId))
            throw new NotFoundInDatabaseException("Auth not found in database.");
        
        Session session = sessions.get(authId);
        logger.log("Session found for user: " + session.getUser().getId(), LogSeverity.TRACE);
        return session;
    }

    @Override
    public Session createSession(User user) {
        Session session = new Session(user);
        
        UUID random;
        //Random until there is no similar key in sesisons. Should not happen. :D
        do{
            random = UUID.randomUUID();
        } while(sessions.containsKey(random.toString()));
            session.setAuthId(random.toString());
            
        sessions.put(session.getAuthId(), session);
        return session;
    }

    @Override
    public void setError(String authId, String error) {
        Session session = sessions.get(authId);
        if (session != null){
            session.setError(error);
        }
    }

    @Override
    public void clearError(String authId){
        setError(authId, null);
    }

    @Override
    public void setInfo(String authId, String info){
        Session session = sessions.get(authId);
        if (session != null){
            session.setInfo(info);
        }
    }

    @Override
    public void clearInfo(String authId) {
        setInfo(authId, null);
    }
}
