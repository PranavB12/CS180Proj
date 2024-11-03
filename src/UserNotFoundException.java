package src;

/**
 * Group Project - CS18000 Gold
 *
 * User Not Found Exception
 *
 * @author Pranav Bansal, Vivaan Malhotra, Rishi Rao, Mike Lee, Vaishnavi Sharma, lab sec 37
 *
 * @version November 3, 2024
 *
 */

public class UserNotFoundException extends RuntimeException implements IUserNotFoundException {

    public UserNotFoundException(String message) {
        super(message);
    }
}
