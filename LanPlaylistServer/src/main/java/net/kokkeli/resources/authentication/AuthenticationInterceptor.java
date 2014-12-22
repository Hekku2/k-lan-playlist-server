package net.kokkeli.resources.authentication;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;

import net.kokkeli.ISettings;
import net.kokkeli.data.ILogger;
import net.kokkeli.data.LogSeverity;
import net.kokkeli.data.Role;
import net.kokkeli.data.db.NotFoundInDatabaseException;
import net.kokkeli.data.services.ISessionService;
import net.kokkeli.resources.Access;
import net.kokkeli.resources.AuthenticationErrorHandling;
import net.kokkeli.data.*;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import com.google.inject.Inject;

/**
 * Authentication checker class
 * @author Hekku2
 */
public class AuthenticationInterceptor implements MethodInterceptor{
    
    /**
     * ILogger. This is protected for injecting.
     */
    @Inject
    protected ILogger logger;
    
    /**
     * ISessionService. This is proteced for injecting.
     */
    @Inject
    protected ISessionService sessions;
    
    /**
     * ISettings. This is protected for injecting
     */
    @Inject
    protected ISettings settings;
    
    /**
     * This is invoked before method with Access-annotation is invoked.
     */
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        logger.log("Checking if all can access...", LogSeverity.TRACE);
        Access access = AuthenticationUtils.extractRoleAnnotation(invocation.getMethod().getAnnotations());
        try {
            //If no role is needed, continue proceeded without checking authentication
            if (access.value() == Role.NONE || (access.value() == Role.ANYNOMOUS && !settings.getRequireAuthentication())){
                return invocation.proceed();
            }

            logger.log("Checking authentication...", LogSeverity.TRACE);
            HttpServletRequest request = AuthenticationUtils.extractRequest(invocation.getArguments());
            Cookie authCookie = AuthenticationUtils.extractLoginCookie(request.getCookies());
            Session session = sessions.get(authCookie.getValue());
            
            if (session.getUser().getRole().getId() < access.value().getId()){
                return Response.seeOther(settings.getURI("index")).build();
            }
            
            logger.log("User authenticated: " + session.getUser().getUserName(), LogSeverity.TRACE);
            
            return invocation.proceed();
        } catch (NotFoundInDatabaseException e) {
            logger.log("Old or invalid authentication." + e.getMessage(), LogSeverity.DEBUG);
            return createProperResponse(access.errorHandling());
        } catch (AuthenticationException e) {
            logger.log("There were no authentication data: " + e.getMessage(), LogSeverity.DEBUG);
            return createProperResponse(access.errorHandling());
        }
    }
    
    private Response createProperResponse(AuthenticationErrorHandling errorHandling){
        switch (errorHandling) {
        case DEFAULT:
            return defaultRedirect();
        case RETURN_CODE:
            return unauthorizedCode();
        default:
            return defaultRedirect();
        }
    }
    
    private static Response unauthorizedCode(){
        return Response.status(Status.FORBIDDEN).entity("Unauthorized!").build();
    }
    
    private Response defaultRedirect(){
        return Response.seeOther(UriBuilder.fromUri(settings.getBaseURI()).path("/authentication").build()).build();
    }
}
