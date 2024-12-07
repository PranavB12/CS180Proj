package src;


import java.util.*;

/**
 * Group Project - CS18000 Gold
 *
 * Newsfeed class interface
 *
 * @author Pranav Bansal, Vivaan Malhotra, Rishi Rao, Mike Lee lab sec 37
 *
 * @version November 19, 2024
 *
 */

public interface INewsFeed {
    void displayPosts(User user);
    void deletePost(Post post);
    void clearFeed();
    void setPosts(List<Post> posts);
    List<Post> getPosts();
}
