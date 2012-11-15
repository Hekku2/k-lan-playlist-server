package net.kokkeli.resources.authentication;

import java.lang.annotation.Annotation;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import net.kokkeli.data.Logging;
import net.kokkeli.data.Role;
import net.kokkeli.resources.Access;
import net.kokkeli.server.LanServer;
import net.kokkeli.server.ServerException;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * Authentication checker class
 * @author Hekku2
 *
 */
public class AuthenticationInceptor implements MethodInterceptor{
    
    /**
     * This is invoked before method with Access-annotation is invoked.
     */
    public Object invoke(MethodInvocation invocation) throws Throwable {
        try {
            Logging.Log("Checking if all can access...", 0);
            Access access = extractAnnotation(invocation.getMethod().getAnnotations());
            
            //If no role is needed, continue proceeded without checking authentication
            if (access.value() == Role.NONE){
                return invocation.proceed();
            }
            
            Logging.Log("Checking authentication...", 0);
            HttpServletRequest request = extractRole(invocation.getArguments());
            Cookie authCookie = extractLoginCookie(request.getCookies());
            
            //TODO Proper checking for auth validity...
            
            
            Logging.Log("User authenticated: " + authCookie.getValue() + ", " + access.value(), 1);
            return invocation.proceed();
        } catch (AuthenticationException e) {
            Logging.Log("User was not authenticated. " + e.getMessage(), 0);
            return Response.seeOther(UriBuilder.fromUri(LanServer.getBaseURI()).path("/authentication").build()).build();
        }

        
    }
    
    /**
     * Extracts Access-annotation from annotations
     * @param annotations Array of annotations
     * @return 
     * @throws ServerException 
     */
    private static Access extractAnnotation(Annotation[] annotations) throws ServerException{
        if (annotations == null) throw new ServerException("No role annotation found in resource");
        
        for (Annotation annotation : annotations) {
            if (annotation.annotationType().isAssignableFrom(Access.class)){
                return (Access)annotation;
            }
        }
        throw new ServerException("No role annotation found in resource");
    }
    
    /**
     * Exctracts HttpServletRequest from objects 
     * @param parameters Array of objects
     * @return Found request.
     * @throws AuthenticationException Thrown if request is not found.
     */
    private static HttpServletRequest extractRole(Object[] parameters) throws AuthenticationException{
        if (parameters == null) throw new AuthenticationException("HttpServletRequest not found from parameters.");
        
        for (Object object : parameters) {
            try {
                return (HttpServletRequest) object;
            } catch (Exception e) {
                
            }
        }
        Logging.Log("No HttpServletRequest found.", 1);
        throw new AuthenticationException("HttpServletRequest not found from parameters.");
    }
    
    /**
     * Extracts authentication cookie from cookies
     * @param cookies Array of cookies
     * @return Authentication cookie
     * @throws AuthenticationCookieNotFound Thrown if there is no authentication cookie.
     */
    private static Cookie extractLoginCookie(Cookie[] cookies) throws AuthenticationCookieNotFound {
        if (cookies == null) throw new AuthenticationCookieNotFound("Authentication cookie not found from cookies.");
        
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("auth")) return cookie;
        }
        throw new AuthenticationCookieNotFound("Authentication cookie not found from cookies.");
    }
}
