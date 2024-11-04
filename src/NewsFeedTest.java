package src;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

public class NewsFeedTest {

    private NewsFeed newsFeed;
    private User user;
    private User friend;

    @BeforeEach
    public void setUp() {
        newsFeed = new NewsFeed();
        user = new User("testUser", "password", "Test User");
        friend = new User("friendUser", "password", "Friend User");
    }

    @Test
    public void testDisplayPostsNoFriends() {
        // Test displayPosts with no friends
        newsFeed.displayPosts(user);
        // Expectation: No posts displayed (verify via console or mocked output)
    }

    @Test
    public void testDisplayPostsFriendsNoPosts() {
        // Test displayPosts when friends have no posts
        user.getFriends().add(friend);
        newsFeed.displayPosts(user);
        // Expectation: No posts displayed (verify via console or mocked output)
    }

    @Test
    public void testDisplayPostsMultipleFriends() {
        // Test displayPosts with multiple friends who each have posts
        User friend1 = new User("friend1", "pass", "Friend One");
        User friend2 = new User("friend2", "pass", "Friend Two");

        friend1.getPosts().add(new Post("Post from Friend One", friend1, 1));
        friend2.getPosts().add(new Post("Post from Friend Two", friend2, 2));

        user.getFriends().add(friend1);
        user.getFriends().add(friend2);

        newsFeed.displayPosts(user);
        // Expectation: Both posts displayed in random order
    }

    @Test
    public void testDisplayPostsHiddenPosts() {
        // Test displayPosts with a mix of hidden and visible posts
        Post visiblePost = new Post("Visible Post", friend, 1);
        Post hiddenPost = new Post("Hidden Post", friend, 2);
        hiddenPost.hidePost();

        friend.getPosts().add(visiblePost);
        friend.getPosts().add(hiddenPost);
        user.getFriends().add(friend);

        newsFeed.displayPosts(user);
        // Expectation: Only "Visible Post" is displayed
    }

    @Test
    public void testDeleteExistingPost() {
        // Test deleting an existing post
        Post postToDelete = new Post("Post to delete", user, 1);
        user.getPosts().add(postToDelete);
        newsFeed.setPosts(new ArrayList<>(user.getPosts()));

        newsFeed.deletePost(postToDelete);
        assertFalse(newsFeed.getPosts().contains(postToDelete), "Post should be deleted from the feed");
    }

    @Test
    public void testDeleteNonExistentPost() {
        // Test deleting a non-existent post
        Post existingPost = new Post("Existing post", user, 1);
        Post nonExistentPost = new Post("Non-existent post", user, 2);

        newsFeed.setPosts(new ArrayList<>(List.of(existingPost)));
        newsFeed.deletePost(nonExistentPost);

        assertEquals(1, newsFeed.getPosts().size(), "Feed size should remain unchanged");
    }

    @Test
    public void testClearFeedWithPosts() {
        // Test clearing feed with posts present
        Post post1 = new Post("Post 1", user, 1);
        Post post2 = new Post("Post 2", user, 2);

        newsFeed.setPosts(new ArrayList<>(List.of(post1, post2)));
        newsFeed.clearFeed();

        assertTrue(newsFeed.getPosts().isEmpty(), "Feed should be empty after clearing");
    }

    @Test
    public void testDisplayPostsCommentsEnabledAndDisabled() {
        // Test displayPosts with posts that have comments enabled and disabled
        Post commentEnabledPost = new Post("Post with comments enabled", user, 1);
        commentEnabledPost.addComment("First Comment");
        Post commentDisabledPost = new Post("Post with comments disabled", user, 2);
        commentDisabledPost.disableComments();

        user.getPosts().add(commentEnabledPost);
        user.getPosts().add(commentDisabledPost);
        newsFeed.setPosts(new ArrayList<>(user.getPosts()));

        newsFeed.displayPosts(user);
        // Expectation: "comments off" message for the disabled post
    }

    @Test
    public void testDisplayFeedAfterClear() {
        // Test displayPosts after clearing the feed
        Post post = new Post("Sample post", user, 1);
        newsFeed.setPosts(new ArrayList<>(List.of(post)));

        newsFeed.clearFeed();
        newsFeed.displayPosts(user);
        // Expectation: No posts displayed
    }

    @Test
    public void testDisplayLargeNumberOfPosts() {
        // Test displayPosts with a large number of posts
        for (int i = 0; i < 1000; i++) {
            user.getPosts().add(new Post("Post " + i, user, i));
        }

        newsFeed.setPosts(user.getPosts());
        newsFeed.displayPosts(user);
        // Expectation: All 1000 posts displayed without errors
    }
}
