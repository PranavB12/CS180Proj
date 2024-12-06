package src;


import java.util.*;


public interface INewsFeed {
    void displayPosts(User user);
    void deletePost(Post post);
    void clearFeed();
    void setPosts(List<Post> posts);
    List<Post> getPosts();
}
