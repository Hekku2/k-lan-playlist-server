package net.kokkeli.resources.authentication;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import net.kokkeli.data.ILogger;
import net.kokkeli.data.Logging;
import net.kokkeli.data.Role;
import net.kokkeli.resources.Access;
import net.kokkeli.server.LanServer;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * Authentication checker class
 * @author Hekku2
 */
public class AuthenticationInceptor implements MethodInterceptor{
    private final ILogger logger;
    
    /**
     * Creates authencation inceptor for catching Access-annotations
     * @param iLogger Logger
     */
    public AuthenticationInceptor(ILogger iLogger){
        this.logger = iLogger;
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

            if (!authCookie.getValue().equals("Ok")){
                logger.log("Cookie value: " + authCookie.getValue(), 1);
                return Response.seeOther(UriBuilder.fromUri(LanServer.getBaseURI()).path("/authentication").build()).build();
            }
            //TODO Proper checking for auth validity...
            
            logger.log("User authenticated: " + authCookie.getValue() + ", " + access.value(), 1);
            return invocation.proceed();
        } catch (AuthenticationException e) {
            logger.log("User was not authenticated. " + e.getMessage(), 0);
            return Response.seeOther(UriBuilder.fromUri(LanServer.getBaseURI()).path("/authentication").build()).build();
        }

        
    }
}
