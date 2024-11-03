package src;


public class PostException extends RuntimeException implements IPostException {
    public PostException(String message) {
        super(message);
    }
}
