package src;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Group Project - CS18000 Gold
 *
 * Updated JUnit tests for Comment class
 *
 * @author Team
 *
 * @version December 6, 2024
 */

class CommentTest {
    private User author;
    private String postID;
    private Comment comment;

    @BeforeEach
    void setUp() {
        author = new User("testUser", "testEmail@test.com", "testPassword");
        postID = "post123";
        comment = new Comment("This is a test comment.", author, postID);
    }

    @Test
    void testConstructorWithValidArguments() {
        assertNotNull(comment.getID(), "ID should not be null");
        assertEquals("This is a test comment.", comment.getContent());
        assertEquals(author, comment.getAuthor());
        assertEquals(postID, comment.getPostID());
        assertEquals(0, comment.getUpVotes());
        assertEquals(0, comment.getDownVotes());
    }

    @Test
    void testConstructorWithNullArguments() {
        assertThrows(IllegalArgumentException.class,
                () -> new Comment(null, author, postID),
                "Constructor should throw exception if comment is null");

        assertThrows(IllegalArgumentException.class,
                () -> new Comment("Comment", null, postID),
                "Constructor should throw exception if author is null");

        assertThrows(IllegalArgumentException.class,
                () -> new Comment("Comment", author, null),
                "Constructor should throw exception if postID is null");
    }

    @Test
    void testUpvoteAndDownvoteWithUsernames() {
        comment.upvote("user1");
        assertEquals(1, comment.getUpVotes());
        comment.upvote("user2");
        assertEquals(2, comment.getUpVotes());

        assertThrows(IllegalStateException.class, () -> comment.upvote("user1"),
                "Should throw exception if user tries to upvote again");

        comment.downvote("user1");
        assertEquals(1, comment.getUpVotes(), "Upvotes should decrease when user changes to downvote");
        assertEquals(1, comment.getDownVotes());

        assertThrows(IllegalStateException.class, () -> comment.downvote("user1"),
                "Should throw exception if user tries to downvote again");
    }

    @Test
    void testToString() {
        String expectedString = "COMMENT|" + comment.getID() + "|" + author.getUsername() + "|This is a test comment.";
        assertEquals(expectedString, comment.toString(), "toString output should match expected format");
    }

    @Test
    void testSetUpvotesAndSetDownvotes() {
        comment.setUpvotes(5);
        comment.setDownvotes(3);
        assertEquals(5, comment.getUpVotes());
        assertEquals(3, comment.getDownVotes());
    }
}
