package net.kokkeli.resources;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import net.kokkeli.data.Role;

/**
 * Annotation for access control.
 * 
 * All resources needing access control should be marked with this annotation.
 * 
 * NOTE: Method must have HttpServletRequest as parameter, or access check redirects to login.
 * @author Hekku2
 * @version 14.11.2012
 */
@Retention(RetentionPolicy.RUNTIME) @Target(ElementType.METHOD)
public @interface Access {
    Role value();
    AuthenticationErrorHandling errorHandling() default AuthenticationErrorHandling.DEFAULT;
}
