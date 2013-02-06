package net.kokkeli.resources.authentication;

import java.lang.annotation.Annotation;
import javax.servlet.http.Cookie;

import junit.framework.Assert;
import net.kokkeli.data.Role;
import net.kokkeli.resources.Access;
import net.kokkeli.resources.Field;
import net.kokkeli.server.ServerException;

import org.junit.Test;

/**
 * Authentication utils test class
 * @author Hekku2
 */
public class TestAuthenticationUtils {
    private static String AUTH = "auth";
    
    @Test
    public void testExtractingCookieLoginThrowsException() {
        try {
            AuthenticationUtils.extractLoginCookie(null);
            Assert.fail();
        } catch (AuthenticationCookieNotFound e) {
            Assert.assertEquals("There were no cookies.", e.getMessage());
        }
        
        try {
            AuthenticationUtils.extractLoginCookie(new Cookie[0]);
            Assert.fail();
        } catch (AuthenticationCookieNotFound e) {
            Assert.assertEquals("Authentication cookie not found from cookies.", e.getMessage());
        }
    }
    
    @Test
    public void testExtractingLoginCookieFindsCookie() throws AuthenticationCookieNotFound{
        Cookie[] cookies = new Cookie[]{
                new Cookie("", ""),
                new Cookie(AUTH, "test")
                };
        Cookie cook = AuthenticationUtils.extractLoginCookie(cookies);
        Assert.assertEquals(AUTH, cook.getName());
        Assert.assertEquals("test", cook.getValue());
    }
    
    @Test
    public void testExtractingAccessAnnotationThrowsException(){
        try {
            AuthenticationUtils.extractRoleAnnotation(null);
            Assert.fail("Extracting annotation should have thrown exception.");
        } catch (ServerException e) {
            Assert.assertEquals("Array is null.", e.getMessage());
        }
        
        try {
            AuthenticationUtils.extractRoleAnnotation(new Annotation[4]);
            Assert.fail("Extracting annotation should have thrown exception.");
        } catch (ServerException e) {
            Assert.assertEquals("No role annotation found in resource.", e.getMessage());
        }
    }
    
    @Test
    public void testExtractingAccessAnnotationWorks() throws ServerException{
        Annotation[] testAnnotations = new Annotation[]{
                new Field() {
                    
                    @Override
                    public Class<? extends Annotation> annotationType() {
                        return Field.class;
                    }
                }, 
                new Access() {
                    @Override
                    public Class<? extends Annotation> annotationType() {
                        return Access.class;
                    }
                    
                    @Override
                    public Role value() {
                        return Role.ADMIN;
                    }
                }
        };
        
        Access access = AuthenticationUtils.extractRoleAnnotation(testAnnotations);
        Assert.assertTrue("Access annotation did not contain proper Role.", Role.ADMIN == access.value());
    }
}
