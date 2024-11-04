package src;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 * Group Project - CS18000 Gold
 *
 * Interface JUnits
 *
 * @author Pranav Bansal, Vivaan Malhotra, Rishi Rao, Mike Lee, Vaishnavi Sharma, lab sec 37
 *
 * @version November 3, 2024
 *
 */

public class AuthenticationExceptionTest {

    @Test
    public void testAuthenticationExceptionMessage() {
        String expectedMessage = "This is an authentication error";
        AuthenticationException exception = new AuthenticationException(expectedMessage);

        // Verify that the message is correctly passed to the superclass
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void testAuthenticationExceptionIsARuntimeException() {
        AuthenticationException exception = new AuthenticationException("Error");

        // Verify that AuthenticationException is a RuntimeException
        assertTrue(exception instanceof RuntimeException);
    }
}