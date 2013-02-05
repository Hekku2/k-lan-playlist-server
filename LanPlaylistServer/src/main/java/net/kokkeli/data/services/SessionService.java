package net.kokkeli.data.services;

import java.util.HashMap;
import java.util.UUID;

import com.google.inject.Inject;

import net.kokkeli.data.ILogger;
import net.kokkeli.data.Session;
import net.kokkeli.data.User;
import net.kokkeli.data.db.NotFoundInDatabase;

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
     * @throws NotFoundInDatabase thrown if there is no session with given authentication.
     */
    public Session get(String authId) throws NotFoundInDatabase{
        if (!sessions.containsKey(authId))
            throw new NotFoundInDatabase("Auth not found in database.");
        
        Session session = sessions.get(authId);
        logger.log("Session found for user: " + session.getUser().getId(), 1);
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
        //TODO Proper exception handling.
        sessions.get(authId).setError(error);
    }

    @Override
    public void clearError(String authId) {
        // TODO Proper exception handling
        sessions.get(authId).setError(null);
    }
}
