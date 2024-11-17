package src;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Group Project - CS18000 Gold
 *
 * Database JUnits
 *
 * @version November 3, 2024
 */
public class DatabaseTest {
    private User user1;
    private User user2;
    private Post post1;
    private Post post2;
    private Database database;

    @BeforeEach
    public void setUp() {
        // Initialize users
        user1 = new User("user1", "pass1", "User One");
        user2 = new User("user2", "pass2", "User Two");

        // Initialize posts
        post1 = new Post("1", "Hello World!", user1);
        post2 = new Post("2", "Java is awesome!", user2);

        // Initialize database
        database = new Database();
        database.addUser(user1);
        database.addUser(user2);
    }

    @Test
    public void testUserCreation() {
        assertNotNull(user1);
        assertEquals("user1", user1.getUsername());
        assertEquals("User One", user1.getName());
    }

    @Test
    public void testAddPostToUser() {
        database.addPost(post1);
        assertTrue(database.getPosts().containsValue(post1));
    }

    @Test
    public void testCommentOnPost() {
        database.addPost(post1);
        assertNotNull(database.getPostById(post1.getId()), "Post1 was not added to the database!");
        database.addCommentToPost(post1.getId(), "this is a comment", user1);
        assertEquals(1, database.getPosts().get(post1.getId()).getComments().size(),
                "Comment was not added to the post correctly!");
    }


    @Test
    public void testDeleteComment() {
        // Add post1 to the database
        database.addPost(post1);

        // Create a comment and add it to the
        String commentID = database.addCommentToPost(post1.getId(), "hello", user1);

        // Assert the comment was added successfully
        assertEquals(1, database.getPostById(post1.getId()).getComments().size());

        // Delete the comment
        database.deleteCommentFromPost(post1.getId(), commentID, user1);

        // Assert the comment was deleted successfully
        assertEquals(0, database.getPostById(post1.getId()).getComments().size());
    }


    @Test
    public void testHidePost() {
        post1.hidePost();
        assertTrue(post1.isHidden());
    }

    @Test
    public void testDeletePost() {
        database.addPost(post1);
        database.deletePost(post1.getId(), user1);
        assertFalse(database.getPosts().containsKey(post1.getId()));
    }

    @Test
    public void testEnableDisableComments() {
        post1.disableComments();
        assertFalse(post1.areCommentsEnabled());

        post1.enableComments();
        assertTrue(post1.areCommentsEnabled());
    }

    @Test
    public void testMultipleComments() {
        // Add post1 to the database
        database.addPost(post1);

        // Enable comments for the post
        post1.enableComments();

        // Add comments
        database.addCommentToPost(post1.getId(), "First Comment", user1);
        database.addCommentToPost(post1.getId(), "Second Comment", user1);

        // Verify the size of the comments
        assertEquals(2, database.getPostById(post1.getId()).getComments().size());
    }


    @Test
    public void testDownvotePost() {
        post1.downvote(user1.getUsername());
        assertEquals(1, post1.getDownVotes());
    }

    @Test
    public void testUpvotePost() {
        post2.upvote(user2.getUsername());
        assertEquals(1, post2.getUpVotes());
    }

    @Test
    public void testBlockedUsers() {
        user1.blockUser(user2);
        assertTrue(user1.getBlockedUsers().contains(user2));
    }

    @Test
    public void testUserEquals() {
        User user3 = new User("user1", "pass3", "Another User");
        assertTrue(user1.equals(user3));
    }

    @Test
    public void testPostWithNoComments() {
        assertEquals(0, post1.getComments().size());
    }

    @Test
    public void testDisplayPostsWithNoFriends() {
        User lonelyUser = new User("lonelyUser", "lonelyPass", "Lonely User");
        database.addUser(lonelyUser);

        // No friends, no posts
        assertEquals(0, lonelyUser.getFriends().size());
    }

    @Test
    public void testAddCommentWhenDisabled() {
        post1.disableComments();
        assertThrows(IllegalStateException.class, () -> post1.addComment("123", new Comment("Comment", user1, post1.getId())));
    }
}
