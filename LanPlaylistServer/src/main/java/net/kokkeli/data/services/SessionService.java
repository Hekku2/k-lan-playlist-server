package net.kokkeli.data.services;

import com.google.inject.Inject;

import net.kokkeli.data.ILogger;
import net.kokkeli.data.Role;
import net.kokkeli.data.Session;
import net.kokkeli.data.User;
import net.kokkeli.data.db.NotFoundInDatabase;

/**
 * Servuce for sessiondata
 * @author Hekku2
 *
 */
public class SessionService implements ISessionService{
    private ILogger logger;
    
    /**
     * Creates user service with given logger
     * @param logger Logger.
     */
    @Inject
    public SessionService(ILogger logger){
        this.logger = logger;
    }
    
    /**
     * Returns Session for authentication id.
     * @param authId Authentication id of session
     * @throws NotFoundInDatabase thrown if there is no session with given authentication.
     */
    public Session get(String authId) throws NotFoundInDatabase{
        //TODO Make proper implementation.
        if (!authId.equals("Ok"))
            throw new NotFoundInDatabase("Auth not found in database.");
        User user = new User("mockMan", Role.ADMIN);
        user.setId(42);
        
        logger.log("Session found for user: " + user.getId(), 1);
        return new Session(user);
    }
}
