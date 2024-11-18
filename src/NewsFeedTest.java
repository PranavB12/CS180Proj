package src;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Group Project - CS18000 Gold
 *
 * Test cases for the NewsFeed class
 *
 * @author Pranav Bansal, Vivaan Malhotra, Rishi Rao, Mike Lee, Vaishnavi Sharma, lab sec 37
 *
 * @version November 3, 2024
 *
 */
public class NewsFeedTest {

    private NewsFeed newsFeed;
    private User user;
    private User friend;
    private User user1;
    private User user2;
    private Post post1;
    private Post post2;

    @BeforeEach
    public void setUp() {
        newsFeed = new NewsFeed();
        user = new User("testUser", "password", "Test User");
        friend = new User("friendUser", "password", "Friend User");
        user1 = new User("user1", "pass1", "User One");
        user2 = new User("user2", "pass2", "User Two");
        post1 = new Post("1", "Post 1 Content", user1);
        post2 = new Post("2", "Post 2 Content", user2);
    }

    @Test
    public void testDisplayPostsNoFriends() {
        // Test displayPosts with no friends
        newsFeed.displayPosts(user);
        // Expectation: No posts displayed
        assertTrue(newsFeed.getPosts().isEmpty(), "News feed should be empty when user has no friends.");
    }

    @Test
    public void testDisplayPostsFriendsNoPosts() {
        // Test displayPosts when friends have no posts
        user.addFriend(friend);
        newsFeed.displayPosts(user);
        // Expectation: No posts displayed
        assertTrue(newsFeed.getPosts().isEmpty(), "News feed should be empty when friends have no posts.");
    }

    @Test
    public void testDisplayPostsMultipleFriends() {
        // Test displayPosts with multiple friends who each have posts
        Post postFromFriend1 = new Post("3", "Post from Friend One", friend);
        Post postFromFriend2 = new Post("4", "Post from Friend Two", user2);

        friend.addPost(postFromFriend1);
        user2.addPost(postFromFriend2);

        user.addFriend(friend);
        user.addFriend(user2);

        newsFeed.displayPosts(user);
        // Expectation: Posts from both friends should be displayed
        assertEquals(2, newsFeed.getPosts().size(), "News feed should contain posts from both friends.");
    }

    @Test
    public void testDeleteExistingPost() {
        // Test deleting an existing post
        user.addPost(post1);
        newsFeed.setPosts(new ArrayList<>(user.getPosts()));

        newsFeed.deletePost(post1);
        assertFalse(newsFeed.getPosts().contains(post1), "Post should be deleted from the feed.");
    }

    @Test
    public void testDeleteNonExistentPost() {
        // Test deleting a non-existent post
        Post nonExistentPost = new Post("6", "Non-existent post", user);

        newsFeed.setPosts(new ArrayList<>(List.of(post1)));
        newsFeed.deletePost(nonExistentPost);

        assertEquals(1, newsFeed.getPosts().size(), "Feed size should remain unchanged.");
    }

    @Test
    public void testClearFeedWithPosts() {
        // Test clearing feed with posts present
        user.addPost(post1);
        user.addPost(post2);

        newsFeed.setPosts(new ArrayList<>(user.getPosts()));
        newsFeed.clearFeed();

        assertTrue(newsFeed.getPosts().isEmpty(), "Feed should be empty after clearing.");
    }

    @Test
    public void testDisplayPostsWithHiddenPosts() {
        // Test displayPosts with a mix of hidden and visible posts
        Post visiblePost = new Post("7", "Visible Post", friend);
        Post hiddenPost = new Post("8", "Hidden Post", friend);
        hiddenPost.hidePost();

        friend.addPost(visiblePost);
        friend.addPost(hiddenPost);
        user.addFriend(friend);

        newsFeed.displayPosts(user);

        // Filter out hidden posts from the feed manually
        long visiblePostCount = newsFeed.getPosts().stream().filter(post -> !post.isHidden()).count();

        // Expectation: Only visible post is counted
        assertEquals(1, visiblePostCount, "News feed should only contain visible posts.");
    }


    @Test
    public void testDisplayFeedAfterClear() {
        // Test displayPosts after clearing the feed
        user.addPost(post1);
        newsFeed.setPosts(new ArrayList<>(user.getPosts()));

        newsFeed.clearFeed();
        newsFeed.displayPosts(user);
        // Expectation: No posts displayed
        assertTrue(newsFeed.getPosts().isEmpty(), "Feed should be empty after clearing.");
    }

    @Test
    public void testDisplayLargeNumberOfPosts() {
        // Test displayPosts with a large number of posts
        for (int i = 0; i < 1000; i++) {
            user.addPost(new Post(String.valueOf(i), "Post " + i, user));
        }

        newsFeed.setPosts(user.getPosts());
        newsFeed.displayPosts(user);
        // Expectation: All 1000 posts displayed without errors
        assertEquals(1000, newsFeed.getPosts().size(), "News feed should contain all 1000 posts.");
    }
}
