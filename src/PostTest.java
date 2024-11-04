package src;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Group Project - CS18000 Gold
 *
 * Post Class JUnits
 *
 * @author Pranav Bansal, Vivaan Malhotra, Rishi Rao, Mike Lee, Vaishnavi Sharma, lab sec 37
 *
 * @version November 3, 2024
 *
 */

class PostTest {
    private User testUser;
    private Post testPost;

    @BeforeEach
    void setUp() {
        Picture profilePicture = new Picture("https://images.pexels.com/photos/736230/pexels-photo-736230.jpeg?cs=srgb&dl=pexels-jonaskakaroto-736230.jpg&fm=jpg");
        testUser = new User("testUser", "password123", profilePicture, "A test user", "Arthur Morgan");
        testPost = new Post("This is a test post.", testUser, 1);
    }

    @Test
    void testPostCreation() {
        assertNotNull(testPost);
        assertEquals(1, testPost.getId());
        assertEquals("This is a test post.", testPost.getContent());
        assertEquals(testUser, testPost.getAuthor());
        assertFalse(testPost.isHidden());
        assertTrue(testPost.isCommentsEnabled());
        assertEquals(0, testPost.getUpVotes());
        assertEquals(0, testPost.getDownVotes());
        assertTrue(testPost.getComments().isEmpty());
    }

    @Test
    void testUpvote() {
        testPost.upvote();
        assertEquals(1, testPost.getUpVotes());
    }

    @Test
    void testDownvote() {
        testPost.downvote();
        assertEquals(1, testPost.getDownVotes());
    }

    @Test
    void testAddComment() {
        testPost.addComment("This is a comment.");
        assertEquals(1, testPost.getComments().size());
        assertTrue(testPost.getComments().containsValue("This is a comment."));
    }

    @Test
    void testDeleteComment() {
        testPost.addComment("Comment to be deleted.");
        int commentId = 1; // Assuming this is the ID assigned
        testPost.deleteComment(commentId);
        assertEquals(0, testPost.getComments().size());
    }

    @Test
    void testHidePost() {
        testPost.hidePost();
        assertTrue(testPost.isHidden());
    }

    @Test
    void testDeletePost() {
        testPost.deletePost(testUser);
        assertTrue(testPost.isHidden());
        assertEquals(0, testPost.getUpVotes());
        assertEquals(0, testPost.getDownVotes());
        assertTrue(testPost.getComments().isEmpty());
    }

    @Test
    void testEnableComments() {
        testPost.disableComments();
        testPost.enableComments();
        assertTrue(testPost.isCommentsEnabled());
    }

    @Test
    void testDisableComments() {
        testPost.disableComments();
        assertFalse(testPost.isCommentsEnabled());
    }

    @Test
    void testAddLargeNumberOfComments() {
        for (int i = 0; i < 10000; i++) {
            testPost.addComment("Comment " + i);
        }
        assertEquals(10000, testPost.getComments().size());
    }

    @Test
    void testDeleteNonExistentComment() {
        testPost.deleteComment(999); // Assuming 999 does not exist
        assertEquals(0, testPost.getComments().size());
    }

    @Test
    void testConcurrentUpvoteDownvote() throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 100; i++) {
            executor.submit(testPost::upvote);
            executor.submit(testPost::downvote);
        }
        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);
        assertTrue(testPost.getUpVotes() >= 0);
        assertTrue(testPost.getDownVotes() >= 0);
    }


    @Test
    void testPostAfterDeletion() {
        testPost.deletePost(testUser);
        assertTrue(testPost.isHidden());
        assertEquals(0, testPost.getUpVotes());
        assertEquals(0, testPost.getDownVotes());
        assertTrue(testPost.getComments().isEmpty());
    }


}