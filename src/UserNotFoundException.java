package src;

public class UserNotFoundException extends RuntimeException implements IUserNotFoundException {

    public UserNotFoundException(String message) {
        super(message);
    }
}
