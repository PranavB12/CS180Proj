package src;

public interface IDatabase {

    boolean addUser(User user);

    boolean removeUser(User user);

    boolean createAccount(String username, String password, String name);

    boolean validateCredentials(String username, String password);

    boolean userExists(String username);

    int createPost(String content, User author);

    boolean deletePost(int postId);

    User viewUser(String username);

    boolean addFriend(User user, User friend);

    boolean removeFriend(User user, User friend);

    boolean blockUser(User user, User toBlock);

    void upvotePost(int postId);

    void downvotePost(int postId);

    void addCommentToPost(int postId, String comment);

    void deleteCommentFromPost(int postId, int commentId);

    void hidePost(int postId);

    void enableCommentsForPost(int postId);

    void disableCommentsForPost(int postId);

    void saveDatabaseToFile(String filename);

    void readDatabaseFromFile(String filename);
}
