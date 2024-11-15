package src;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Group Project - CS18000 Gold
 *
 * Database JUnits
 *
 * @author Pranav Bansal, Vivaan Malhotra, Rishi Rao, Mike Lee, Vaishnavi Sharma, lab sec 37
 *
 * @version November 3, 2024
 *
 */

public class DatabaseTest {
    private User user1;
    private User user2;
    private Post post1;
    private Post post2;
    private Picture picture1;
    private Picture picture2;
    private NewsFeed newsFeed;

    @BeforeEach
    public void setUp() {
        // Initialize users and pictures
        picture1 = new Picture("http://example.com/pic1.jpg");
        picture2 = new Picture("http://example.com/pic2.jpg");
        user1 = new User("user1", "pass1", picture1, "This is user1", "User One");
        user2 = new User("user2", "pass2", picture2, "This is user2", "User Two");

        // Initialize posts
       // post1 = new Post("Hello World!", user1, 1);
        //post2 = new Post("Java is awesome!", user2, 2);


        // Initialize NewsFeed
        newsFeed = new NewsFeed();

        // Set up friendships
        user1.getFriends().add(user2);
        user2.getFriends().add(user1);
    }

    @Test
    public void testUserCreation() {
        assertNotNull(user1);
        assertEquals("user1", user1.getUsername());
        assertEquals("User One", user1.getName());
    }

    @Test
    public void testPostCreation() {
        assertNotNull(post1);
        assertEquals("Hello World!", post1.getContent());
        assertEquals(user1, post1.getAuthor());
    }

    @Test
    public void testAddPostToUser() {
        user1.getPosts().add(post1);
        assertTrue(user1.getPosts().contains(post1));
    }

    @Test
    public void testCommentOnPost() {
        post1.addComment(com.getID(), "This is a comment.");
        assertEquals(1, post1.getComments().size());
        assertTrue(post1.getComments().containsValue("This is a comment."));
    }

    @Test
    public void testDeleteComment() {
        post1.addComment(com.getID(), "Comment to be deleted");
        assertEquals(1, post1.getComments().size());

        post1.deleteComment(String.valueOf(1));
        assertEquals(0, post1.getComments().size());
    }

    @Test
    public void testHidePost() {
        post1.hidePost();
        assertTrue(post1.isHidden());
    }

    @Test
    public void testDeletePost() {
        user1.getPosts().add(post1);
        post1.deletePost(user1);
        assertFalse(user1.getPosts().contains(post1));
        assertTrue(post1.isHidden());
    }

    @Test
    public void testNewsFeedDisplayPosts() {
        user1.getPosts().add(post1);
        user2.getPosts().add(post2);
        newsFeed.displayPosts(user1); // User1's feed should show posts from user2

        // Ideally, you would check for output, but this is a basic check
        assertFalse(newsFeed.getPosts().isEmpty());
    }

    @Test
    public void testNewsFeedDeletePost() {
        user1.getPosts().add(post1);
        newsFeed.deletePost(post1);
        assertFalse(newsFeed.getPosts().contains(post1));
    }

    @Test
    public void testEnableDisableComments() {
        post1.disableComments();
        assertFalse(post1.isCommentsEnabled());

        post1.enableComments();
        assertTrue(post1.isCommentsEnabled());
    }

    @Test
    public void testMultipleComments() {
        post1.enableComments();
        post1.addComment(com.getID(), "First Comment");
        post1.addComment(com.getID(), "Second Comment");

        assertEquals(2, post1.getComments().size());
    }

    @Test
    public void testDownvotePost() {
        post1.downvote();
        assertEquals(1, post1.getDownVotes());
    }

    @Test
    public void testUpvotePost() {
        post2.upvote();
        assertEquals(1, post2.getUpVotes());
    }

    @Test
    public void testBlockedUsers() {
        user1.getBlockedUsers().add(user2);
        assertTrue(user1.getBlockedUsers().contains(user2));
    }

    @Test
    public void testUserEquals() {
        User user3 = new User("user1", "pass3", "Another User");
        assertTrue(user1.equals(user3));
    }

    // New test cases for comments, pictures, and news feed

    @Test
    public void testPostWithNoComments() {
        assertEquals(0, post1.getComments().size());
    }

    @Test
    public void testPictureUrl() {
        assertEquals("http://example.com/pic1.jpg", picture1.getUrl());
        assertEquals("http://example.com/pic2.jpg", picture2.getUrl());
    }

    @Test
    public void testNewsFeedWithMultiplePosts() {
        user1.getPosts().add(post1);
        user2.getPosts().add(post2);
        newsFeed.displayPosts(user1); // User1 should see User2's post

        // Validate that the newsFeed has the posts
        assertTrue(newsFeed.getPosts().contains(post1) || newsFeed.getPosts().contains(post2));
    }

    @Test
    public void testDisplayPostsWithNoFriends() {
        User lonelyUser = new User("lonelyUser", "lonelyPass", "Lonely User");
        newsFeed.displayPosts(lonelyUser); // No friends, no posts

        // Validate that the news feed is empty
        assertTrue(newsFeed.getPosts().isEmpty());
    }

    @Test
    public void testAddCommentWhenDisabled() {
        post1.disableComments();
        post1.addComment(com.getID(), "Comment on disabled post"); // Attempt to add comment
        assertEquals(0, post1.getComments().size()); // Should not be able to add
    }
}
