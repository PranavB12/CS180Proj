package src;

import src.Post;
import src.User;

import java.util.*;

/**
* PJ05 - NewsFeedClass
*
* This class manages a collection of Post objects, allowing posts from a user's friends to be displayed in random order, 
* with options to delete specific posts or clear the entire feed.
*
* @author Pranav Bansal, Vivaan Malhotra, Rishi Rao, Mike Lee, Vaishnavi Sharma, lab sec 37
*
* @version November 3, 2024
*
*/


public class NewsFeed {
    private List<Post> posts;

    // Constructor to initialize the posts list
    public NewsFeed() {
        this.posts = new ArrayList<>();
    }

    // Display posts from user's friends, with posts in random order
    public void displayPosts(User user) {
        List<User> friends = user.getFriends();  // Changed to Set<User>

        // Collect posts from each friend
        for (User friend : friends) {
            List<Post> friendPosts = friend.getPosts();
            posts.addAll(friendPosts);
        }

        // Shuffle the posts for random order display
        Collections.shuffle(posts);

        // Display each post
        for (Post post : posts) {
            System.out.println(post);
        }
    }

    // Method to delete a post from the feed
    public void deletePost(Post post) {
        posts.remove(post);
    }

    // Optional: Clear all posts (if needed for resetting the feed)
    public void clearFeed() {
        posts.clear();
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public List<Post> getPosts() {
        return posts;
    }


}
