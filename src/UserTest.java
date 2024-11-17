package src;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Group Project - CS18000 Gold
 *
 * User JUnit Tests
 *
 * @version November 3, 2024
 */
public class UserTest {

    private User user;
    private User friend;
    private Post post;

    @BeforeEach
    public void setUp() {
        user = new User("testUser", "password123", "Arthur Morgan", "A test user");
        friend = new User("friendUser", "password456", "Buster Baxter");
        post = new Post("1", "This is a post", user); // Assuming Post constructor requires ID, content, and author
    }

    // Constructor Tests
    @Test
    public void testConstructorWithoutDescription() {
        User newUser = new User("simpleUser", "simplePass", "Buster Baxter");
        assertEquals("simpleUser", newUser.getUsername());
        assertEquals("simplePass", newUser.getPassword());
        assertEquals("Buster Baxter", newUser.getName());
        assertEquals("", newUser.getDescription()); // Default description
        assertTrue(newUser.getFriends().isEmpty());
        assertTrue(newUser.getBlockedUsers().isEmpty());
        assertTrue(newUser.getPosts().isEmpty());
    }

    // Password Tests
    @Test
    public void testGetPassword() {
        assertEquals("password123", user.getPassword());
    }

    // Description Tests
    @Test
    public void testSetAndGetDescription() {
        user.setDescription("New description");
        assertEquals("New description", user.getDescription());
    }

    // Friend Management Tests
    @Test
    public void testAddFriend() {
        user.addFriend(friend);
        assertTrue(user.getFriends().contains(friend));
    }

    @Test
    public void testRemoveFriend() {
        user.addFriend(friend);
        user.removeFriend(friend);
        assertFalse(user.getFriends().contains(friend));
    }

    @Test
    public void testAddDuplicateFriend() {
        user.addFriend(friend);
        user.addFriend(friend); // Try to add the same friend again
        assertEquals(1, user.getFriends().size()); // Should still have only one friend
    }

    // Block Management Tests
    @Test
    public void testBlockUser() {
        user.blockUser(friend);
        assertTrue(user.getBlockedUsers().contains(friend));
        assertFalse(user.getFriends().contains(friend)); // Ensure they are removed from friends
    }

    @Test
    public void testUnblockUser() {
        user.blockUser(friend);
        user.unblockUser(friend);
        assertFalse(user.getBlockedUsers().contains(friend));
    }

    // Post Management Tests
    @Test
    public void testAddPost() {
        user.addPost(post);
        assertTrue(user.getPosts().contains(post));
    }

    @Test
    public void testRemovePost() {
        // Add a post to the user
        user.addPost(post);

        // Remove the post using the deletePost method
        boolean removed = user.deletePost(post);

        // Assert the post was removed
        assertTrue(removed, "The post was not removed from the list.");
        assertFalse(user.getPosts().contains(post), "The post is still present in the list after removal.");
    }




    @Test
    public void testAddDuplicatePost() {
        user.addPost(post);
        user.addPost(post); // Try to add the same post again
        assertEquals(1, user.getPosts().size()); // Should still have only one post
    }

    // Equals Method Tests
    @Test
    public void testUserEqualsSameUsername() {
        User user2 = new User("testUser", "differentPass", "Different Name");
        assertTrue(user.equals(user2)); // Equal based on username
    }

    @Test
    public void testUserEqualsDifferentUsername() {
        User user2 = new User("differentUser", "password123", "Arthur Morgan");
        assertFalse(user.equals(user2));
    }

    // Edge Cases
    @Test
    public void testUserEqualsNull() {
        assertFalse(user.equals(null));
    }

    @Test
    public void testUserEqualsDifferentObject() {
        assertFalse(user.equals("String Object")); // Comparing to a non-User object
    }

    @Test
    public void testDefaultConstructorValues() {
        User newUser = new User("userWithoutDescription", "password", "No Description User");
        assertEquals("", newUser.getDescription()); // Default description is an empty string
        assertTrue(newUser.getFriends().isEmpty());
        assertTrue(newUser.getBlockedUsers().isEmpty());
        assertTrue(newUser.getPosts().isEmpty());
    }
}
