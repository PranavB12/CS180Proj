package src;

/**
 * Group Project - CS18000 Gold
 *
 * Post Exception
 *
 * @author Pranav Bansal, Vivaan Malhotra, Rishi Rao, Mike Lee, Vaishnavi Sharma, lab sec 37
 *
 * @version November 3, 2024
 *
 */

public class PostException extends RuntimeException implements IPostException {
    public PostException(String message) {
        super(message);
    }
}
