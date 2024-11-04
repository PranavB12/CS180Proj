package src;

import src.Picture;

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

// IUser Interface
import java.util.List;

public interface IUser {
    String getUsername();
    void setUsername(String username);
    String getPassword();
    void setPassword(String password);
    String getName();
    void setName(String name);
    String getDescription();
    void setDescription(String description);
    List<User> getFriends();
    List<User> getBlockedUsers();
    List<Post> getPosts();
    void setPosts(List<Post> posts);
    void deletePost();
    String toString();
    boolean equals(Object o);
}

