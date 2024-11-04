package src;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Group Project - CS18000 Gold
 *
 * New Feed Test
 *
 * @author Pranav Bansal, Vivaan Malhotra, Rishi Rao, Mike Lee, Vaishnavi Sharma, lab sec 37
 *
 * @version November 3, 2024
 *
 */

class NewsFeedTest {
    private NewsFeed newsFeed;
    private User user;
    private User friend;
    private Post post1;
    private Post post2;

    @BeforeEach
    void setUp() {
        newsFeed = new NewsFeed();
        user = new User("testUser", "password123", "Clyde Rover");
        friend = new User("friendUser", "password456", "Kevin Nyguen");

        // Create some sample posts
        post1 = new Post("content", friend, 1345);
        post2 = new Post();

        // Set up friends and posts
        Set<User> friends = new HashSet<>();
        friends.add(friend);
//        user.setFriends(friends);

        // Adding posts to the friend
        List<Post> friendPosts = new ArrayList<>();
        friendPosts.add(post1);
        friendPosts.add(post2);
        friend.setPosts(friendPosts);
    }

    @Test
    void testDisplayPosts() {
        newsFeed.displayPosts(user);

        // Verify that the posts are displayed
        // In a real scenario, we would capture the output
        // For demonstration, we can check if posts are collected correctly
        assertTrue(newsFeed.getPosts().contains(post1));
        assertTrue(newsFeed.getPosts().contains(post2));
    }

    @Test
    void testDeletePost() {
        newsFeed.displayPosts(user);
        assertTrue(newsFeed.getPosts().contains(post1)); // Ensure post is there
        newsFeed.deletePost(post1);
        assertFalse(newsFeed.getPosts().contains(post1)); // Ensure post is deleted
    }

    @Test
    void testClearFeed() {
        newsFeed.displayPosts(user);
        assertFalse(newsFeed.getPosts().isEmpty()); // Ensure feed is not empty
        newsFeed.clearFeed();
        assertTrue(newsFeed.getPosts().isEmpty()); // Ensure feed is cleared
    }
}