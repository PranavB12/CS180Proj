package src;

/**
 * Group Project - CS18000 Gold
 *
 * Authentication Exception
 *
 * @author Pranav Bansal, Vivaan Malhotra, Rishi Rao, Mike Lee, Vaishnavi Sharma, lab sec 37
 *
 * @version November 3, 2024
 *
 */

public class AuthenticationException extends RuntimeException implements IAuthenticationException {
    public AuthenticationException(String message) {
        super(message);
    }

}
