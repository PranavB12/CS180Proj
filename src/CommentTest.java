package src;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Group Project - CS18000 Gold
 *
 * JUNIT tests for Comment class
 *
 * @author Pranav Bansal, Vivaan Malhotra, Rishi Rao, Mike Lee lab sec 37
 *
 * @version November 16, 2024
 *
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
        assertThrows(IllegalArgumentException.class, () -> new Comment(null, author, postID));
        assertThrows(IllegalArgumentException.class, () -> new Comment("Comment", null, postID));
        assertThrows(IllegalArgumentException.class, () -> new Comment("Comment", author, null));
    }

    @Test
    void testConstructorWithCustomCommentID() {
        String customID = "custom-id-123";
        Comment customComment = new Comment("Custom comment", author, postID, customID);
        assertEquals(customID, customComment.getID());
    }

    @Test
    void testUpVote() {
        comment.upVote();
        assertEquals(1, comment.getUpVotes());
        comment.upVote();
        assertEquals(2, comment.getUpVotes());
    }

    @Test
    void testDownVote() {
        comment.downVote();
        assertEquals(1, comment.getDownVotes());
        comment.downVote();
        assertEquals(2, comment.getDownVotes());
    }

    @Test
    void testUndoUpVote() {
        comment.upVote();
        comment.upVote();
        comment.undoUpVote();
        assertEquals(1, comment.getUpVotes());
        comment.undoUpVote();
        assertEquals(0, comment.getUpVotes());
        comment.undoUpVote(); // Should not go below zero
        assertEquals(0, comment.getUpVotes());
    }

    @Test
    void testUndoDownVote() {
        comment.downVote();
        comment.downVote();
        comment.undoDownVote();
        assertEquals(1, comment.getDownVotes());
        comment.undoDownVote();
        assertEquals(0, comment.getDownVotes());
        comment.undoDownVote(); // Should not go below zero
        assertEquals(0, comment.getDownVotes());
    }

    @Test
    void testToString() {
        String expectedString = "COMMENT|" + comment.getID() + "|" + author.getUsername() + "|This is a test comment.";
        assertEquals(expectedString, comment.toString());
    }
}

