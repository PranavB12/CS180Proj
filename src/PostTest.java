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
 * @version November 19, 2024
 *
 */

class PostTest {
    private User testUser;
    private Post testPost;

    @BeforeEach
    void setUp() {
        testUser = new User("testUser", "password123", "Test User", "A test user");
        testPost = new Post("1", "This is a test post.", testUser);
    }

    @Test
    void testPostCreation() {
        assertNotNull(testPost);
        assertEquals("1", testPost.getId());
        assertEquals("This is a test post.", testPost.getContent());
        assertEquals(testUser, testPost.getAuthor());
        assertFalse(testPost.isHidden());
        assertTrue(testPost.areCommentsEnabled());
        assertEquals(0, testPost.getUpVotes());
        assertEquals(0, testPost.getDownVotes());
        assertTrue(testPost.getComments().isEmpty());
    }

    @Test
    void testUpvote() {
        testPost.upvote("user1");
        assertEquals(1, testPost.getUpVotes());
    }

    @Test
    void testDownvote() {
        testPost.downvote("user1");
        assertEquals(1, testPost.getDownVotes());
    }

    @Test
    void testAddComment() {
        Comment comment = new Comment("This is a comment.", testUser, testPost.getId());
        testPost.addComment(comment.getID(), comment);
        assertEquals(1, testPost.getComments().size());
        assertTrue(testPost.getComments().containsKey(comment.getID()));
    }

    @Test
    void testDeleteComment() {
        Comment comment = new Comment("Comment to be deleted.", testUser, testPost.getId());
        testPost.addComment(comment.getID(), comment);
        assertEquals(1, testPost.getComments().size());

        testPost.deleteComment(comment.getID(), testUser);
        assertTrue(testPost.getComments().isEmpty());
    }

    @Test
    void testHidePost() {
        testPost.hidePost();
        assertTrue(testPost.isHidden());
    }

    @Test
    void testUnhidePost() {
        testPost.hidePost();
        assertTrue(testPost.isHidden());

        testPost.unhidePost();
        assertFalse(testPost.isHidden());
    }

    @Test
    void testEnableComments() {
        testPost.disableComments();
        assertFalse(testPost.areCommentsEnabled());

        testPost.enableComments();
        assertTrue(testPost.areCommentsEnabled());
    }

    @Test
    void testDisableComments() {
        testPost.disableComments();
        assertFalse(testPost.areCommentsEnabled());
    }

    @Test
    void testAddLargeNumberOfComments() {
        for (int i = 0; i < 10000; i++) {
            Comment comment = new Comment("Comment " + i, testUser, testPost.getId());
            testPost.addComment(comment.getID(), comment);
        }
        assertEquals(10000, testPost.getComments().size());
    }

    @Test
    void testDuplicateUpvote() {
        testPost.upvote("user1");
        assertThrows(IllegalStateException.class, () -> testPost.upvote("user1"));
    }

    @Test
    void testDuplicateDownvote() {
        testPost.downvote("user1");
        assertThrows(IllegalStateException.class, () -> testPost.downvote("user1"));
    }

    @Test
    void testRemoveVoteThenUpvote() {
        testPost.downvote("user1");
        assertEquals(1, testPost.getDownVotes());

        testPost.upvote("user1");
        assertEquals(1, testPost.getUpVotes());
        assertEquals(0, testPost.getDownVotes());
    }
}
