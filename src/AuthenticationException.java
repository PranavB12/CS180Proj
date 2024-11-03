package src;

public class AuthenticationException extends RuntimeException implements IAuthenticationException {
    public AuthenticationException(String message) {
        super(message);
    }

}
