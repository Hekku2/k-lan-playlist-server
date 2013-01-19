package net.kokkeli.resources.authentication;

import java.lang.annotation.Annotation;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import net.kokkeli.resources.Access;
import net.kokkeli.server.ServerException;

/**
 * Utility-class containing helping methods for authentication.
 * @author Hekku2
 * @version 16.11.2012
 */
public final class AuthenticationUtils {
    
    /**
     * Extracts authentication cookie from cookies
     * @param cookies Array of cookies
     * @return Authentication cookie
     * @throws AuthenticationCookieNotFound Thrown if there is no authentication cookie.
     */
    public static Cookie extractLoginCookie(Cookie[] cookies) throws AuthenticationCookieNotFound {
        if (cookies == null) throw new AuthenticationCookieNotFound("There were no cookies.");
        
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("auth")) return cookie;
        }
        throw new AuthenticationCookieNotFound("Authentication cookie not found from cookies.");
    }
    
    /**
     * Extracts Access-annotation from annotations
     * @param annotations Array of annotations
     * @return 
     * @throws ServerException 
     */
    public static Access extractRoleAnnotation(Annotation[] annotations) throws ServerException{
        if (annotations == null) throw new ServerException("Array is null.");
        
        for (Annotation annotation : annotations) {
            if (annotation != null && annotation.annotationType().isAssignableFrom(Access.class)){
                return (Access)annotation;
            }
        }
        throw new ServerException("No role annotation found in resource.");
    }
    
    /**
     * Exctracts HttpServletRequest from objects 
     * @param parameters Array of objects
     * @return Found request.
     * @throws AuthenticationException Thrown if request is not found.
     */
    public static HttpServletRequest extractRequest(Object[] parameters) throws AuthenticationException{
        if (parameters == null) throw new AuthenticationException("HttpServletRequest not found from parameters.");
        
        for (Object object : parameters) {
            try {
                return (HttpServletRequest) object;
            } catch (Exception e) {
                
            }
        }
        throw new AuthenticationException("HttpServletRequest not found from parameters.");
    }
    
    /**
     * TODO Extract username from httpServletRequest.
     * @param req
     * @return
     */
    public static String extractUsername(HttpServletRequest req){
        return "mockUser1";
    }
    
    /**
     * Hidden constructor.
     */
    private AuthenticationUtils(){}
}
