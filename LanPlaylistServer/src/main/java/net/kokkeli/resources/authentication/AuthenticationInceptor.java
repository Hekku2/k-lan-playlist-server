package net.kokkeli.resources.authentication;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import net.kokkeli.data.ILogger;
import net.kokkeli.data.Logging;
import net.kokkeli.data.Role;
import net.kokkeli.data.db.NotFoundInDatabase;
import net.kokkeli.data.services.ISessionService;
import net.kokkeli.resources.Access;
import net.kokkeli.server.LanServer;
import net.kokkeli.data.*;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * Authentication checker class
 * @author Hekku2
 */
public class AuthenticationInceptor implements MethodInterceptor{
    private final ILogger logger;
    private final ISessionService sessions;
    
    /**
     * Creates authencation inceptor for catching Access-annotations
     * @param iLogger Logger
     */
    public AuthenticationInceptor(ILogger iLogger, ISessionService sessions){
        this.logger = iLogger;
        this.sessions = sessions;
    }
    
    /**
     * This is invoked before method with Access-annotation is invoked.
     */
    public Object invoke(MethodInvocation invocation) throws Throwable {
        try {
            ILogger logger = new Logging();
            
            logger.log("Checking if all can access...", 0);
            Access access = AuthenticationUtils.extractRoleAnnotation(invocation.getMethod().getAnnotations());
            
            //If no role is needed, continue proceeded without checking authentication
            if (access.value() == Role.NONE){

                return invocation.proceed();
            }

            logger.log("Checking authentication...", 0);
            HttpServletRequest request = AuthenticationUtils.extractRequest(invocation.getArguments());
            Cookie authCookie = AuthenticationUtils.extractLoginCookie(request.getCookies());
            Session session = sessions.get(authCookie.getValue());
            logger.log("User authenticated: " + session.getUser(), 1);
            
            return invocation.proceed();
        } catch (NotFoundInDatabase e) {
            logger.log("Old or invalid authentication." + e.getMessage(), 1);
            return Response.seeOther(UriBuilder.fromUri(LanServer.getBaseURI()).path("/authentication").build()).build();
        } catch (AuthenticationException e) {
            logger.log("There were no authenticaiton data: " + e.getMessage(), 1);
            return Response.seeOther(UriBuilder.fromUri(LanServer.getBaseURI()).path("/authentication").build()).build();
        }

        
    }
}
