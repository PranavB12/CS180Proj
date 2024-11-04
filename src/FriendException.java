package src;

/**
 * Group Project - CS18000 Gold
 *
 * Friend Exception
 *
 * @author Pranav Bansal, Vivaan Malhotra, Rishi Rao, Mike Lee, Vaishnavi Sharma, lab sec 37
 *
 * @version November 3, 2024
 *
 */


public class FriendException extends RuntimeException implements IFriendException {
    public FriendException(String message) {
        super(message);
    }

}
