package src;

/**
 * Group Project - CS18000 Gold
 *
 * additional interfaces
 *
 * @author Pranav Bansal, Vivaan Malhotra, Rishi Rao, Mike Lee, Vaishnavi Sharma, lab sec 37
 *
 * @version November 3, 2024
 *
 */

public interface IDatabase {


    boolean addUser(User user);
    boolean removeUser(User user);
    boolean createAccount(String username, String password, String name);
    boolean validateCredentials(String username, String password);
    boolean userExists(String username);
    String createPost(String content, User author);
    boolean deletePost(String postId);
    User viewUser(String username);
    boolean addFriend(User user, User friend);
    boolean removeFriend(User user, User friend);
    boolean blockUser(User user, User toBlock);
    void upvotePost(String postId);
    void downvotePost(String postId);
    void addCommentToPost(String postId, String comment);
    void deleteCommentFromPost(String postId, String commentId);
    void hidePost(String postId);
    void enableCommentsForPost(String postId);
    void disableCommentsForPost(String postId);
    void saveDatabaseToFile(String filename);
    void readDatabaseFromFile(String filename);
}
