package src;
import java.util.*;
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
