package src;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Group Project - CS18000 Gold
 *
 * User Not Found Exception JUnits
 *
 * @author Pranav Bansal, Vivaan Malhotra, Rishi Rao, Mike Lee, Vaishnavi Sharma, lab sec 37
 *
 * @version November 3, 2024
 *
 */

class UserNotFoundExceptionTest {

    @Test
    void testUserNotFoundExceptionMessage() {
        String expectedMessage = "User not found";
        UserNotFoundException exception = new UserNotFoundException(expectedMessage);

        assertNotNull(exception);
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void testUserNotFoundExceptionWithoutMessage() {
        UserNotFoundException exception = new UserNotFoundException(null);

        assertNotNull(exception);
        assertNull(exception.getMessage());
    }

    @Test
    void testUserNotFoundExceptionInTryCatch() {
        String expectedMessage = "User not found in database";

        try {
            throw new UserNotFoundException(expectedMessage);
        } catch (UserNotFoundException e) {
            assertEquals(expectedMessage, e.getMessage());
        }
    }

    @Test
    void testUserNotFoundExceptionInheritance() {
        UserNotFoundException exception = new UserNotFoundException("Testing inheritance");

        assertTrue(exception instanceof RuntimeException);
        assertTrue(exception instanceof IUserNotFoundException);
    }
}
