package net.kokkeli.resources.authentication;

/**
 * Exception for case when authentication cookie can not be found.
 * @author Hekku2
 *
 */
public class AuthenticationCookieNotFound extends AuthenticationException {
    private static final long serialVersionUID = 1L;

    public AuthenticationCookieNotFound(String message) {
        super(message);
    }

}
