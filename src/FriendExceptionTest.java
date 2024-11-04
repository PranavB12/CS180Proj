package src;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 * Group Project - CS18000 Gold
 *
 * Exception Junit
 *
 * @author Pranav Bansal, Vivaan Malhotra, Rishi Rao, Mike Lee, Vaishnavi Sharma, lab sec 37
 *
 * @version November 3, 2024
 *
 */

public class FriendExceptionTest {

    @Test
    public void testFriendExceptionMessage() {
        String expectedMessage = "This is a friend error";
        FriendException exception = new FriendException(expectedMessage);

        // Verify that the message is correctly passed to the superclass
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void testFriendExceptionIsARuntimeException() {
        FriendException exception = new FriendException("Error");

        // Verify that FriendException is a RuntimeException
        assertTrue(exception instanceof RuntimeException);
    }
}