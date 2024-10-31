package src;

import java.util.List;

// IDatabase Interface
public interface IDatabase {

    boolean addUser(User user);
    boolean removeUser(User user);
    boolean validateCredentials(String username, String password);
    boolean userExists(String username);

    boolean addFriend(User user, String friendUsername);
    boolean removeFriend(User user, String friendUsername);
    boolean blockUser(User user, String blockedUsername);
    boolean unblockUser(User user, String blockedUsername);
    List<String> getFriendsList(User user);
    List<String> getBlockedList(User user);


    int addPost(Post post);
    boolean deletePost(int postID);
    boolean enableComments(int postID);
    boolean disableComments(int postID);
    List<Integer> getUserPosts(User user);



}