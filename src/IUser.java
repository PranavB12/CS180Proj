package src;
import java.util.*;

/**
 * Group Project - CS18000 Gold
 *
 * User class interface
 *
 * @author Pranav Bansal, Vivaan Malhotra, Rishi Rao, Mike Lee lab sec 37
 *
 * @version November 19, 2024
 *
 */

public interface IUser {
    String getUsername();
    String getPassword();
    String getName();
    List<User> getFriends();
    List<User> getBlockedUsers();
    List<Post> getPosts();
    void addFriend(User friend);
    void removeFriend(User friend);
    void blockUser(User user);
    void unblockUser(User user);
    void addPost(Post post);
    void setBlockedUsers(List<User> blocked);
    void setDescription(String description);
    String getDescription();
    void setFriends(List<User> friends);
    void setPosts(List<Post> posts);
    boolean equals(Object o);
}
