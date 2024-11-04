package src;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


/**
 * Group Project - CS18000 Gold
 *
 * User JUnits
 *
 * @author Pranav Bansal, Vivaan Malhotra, Rishi Rao, Mike Lee, Vaishnavi Sharma, lab sec 37
 *
 * @version November 3, 2024
 *
 */

public class UserTest {

    private User user;
    private User friend;
    private Picture profilePicture;

    @BeforeEach
    public void setUp() {
        profilePicture = new Picture("https://images.pexels.com/photos/736230/pexels-photo-736230.jpeg?cs=srgb&dl=pexels-jonaskakaroto-736230.jpg&fm=jpg"); // Assuming default constructor for Picture
        user = new User("testUser", "password123", profilePicture, "A test user", "Arthur Morgan");
        friend = new User("friendUser", "password456", "Buster Baxter");
    }


    @Test
    public void testConstructorWithoutProfilePicture() {
        User newUser = new User("simpleUser", "simplePass", "Buster Baxter");
        assertEquals("simpleUser", newUser.getUsername());
        assertEquals("simplePass", newUser.getPassword());
        assertEquals("Buster Baxter", newUser.getName());
        assertNull(newUser.getPicture()); // No profile picture provided
        assertEquals("", newUser.getDescription()); // Default description
        assertTrue(newUser.getFriends().isEmpty());
        assertTrue(newUser.getBlockedUsers().isEmpty());
        assertTrue(newUser.getPosts().isEmpty());
    }

    // Username Tests
    @Test
    public void testSetAndGetUsername() {
        user.setUsername("newUsername");
        assertEquals("newUsername", user.getUsername());
    }

    @Test
    public void testSetUsernameEmptyString() {
        user.setUsername("");
        assertEquals("", user.getUsername());
    }

    // Password Tests
    @Test
    public void testSetAndGetPassword() {
        user.setPassword("newPassword");
        assertEquals("newPassword", user.getPassword());
    }

    @Test
    public void testSetPasswordNull() {
        user.setPassword(null);
        assertNull(user.getPassword());
    }

    // Name Tests
    @Test
    public void testSetAndGetName() {
        user.setName("New Name");
        assertEquals("New Name", user.getName());
    }

    @Test
    public void testSetNameEmptyString() {
        user.setName("");
        assertEquals("", user.getName());
    }

    // Description Tests
    @Test
    public void testSetAndGetDescription() {
        user.setDescription("New description");
        assertEquals("New description", user.getDescription());
    }

    @Test
    public void testSetDescriptionEmptyString() {
        user.setDescription("");
        assertEquals("", user.getDescription());
    }

    // Friend Management Tests
    @Test
    public void testAddFriend() {
        assertTrue(user.getFriends().add(friend));
        assertTrue(user.getFriends().contains(friend));
    }

    @Test
    public void testRemoveFriend() {
        user.getFriends().add(friend);
        assertTrue(user.getFriends().remove(friend));
        assertFalse(user.getFriends().contains(friend));
    }

    @Test
    public void testRemoveNonExistingFriend() {
        assertFalse(user.getFriends().remove(friend));
    }

    // Block Management Tests
    @Test
    public void testBlockUser() {
        assertTrue(user.getBlockedUsers().add(friend));
        assertTrue(user.getBlockedUsers().contains(friend));
    }

    @Test
    public void testUnblockUser() {
        user.getBlockedUsers().add(friend);
        assertTrue(user.getBlockedUsers().remove(friend));
        assertFalse(user.getBlockedUsers().contains(friend));
    }

    @Test
    public void testUnblockNonExistingUser() {
        assertFalse(user.getBlockedUsers().remove(friend));
    }

    // Post Management Tests
    @Test
    public void testAddPost() {
        Post newPost = new Post(); // Assuming Post has a default constructor
        user.getPosts().add(newPost);
        assertTrue(user.getPosts().contains(newPost));
    }

    @Test
    public void testDeletePost() {
        Post newPost = new Post();
        user.getPosts().add(newPost);
        assertTrue(user.getPosts().remove(newPost));
        assertFalse(user.getPosts().contains(newPost));
    }

    @Test
    public void testDeleteNonExistingPost() {
        Post newPost = new Post();
        assertFalse(user.getPosts().remove(newPost));
    }

}
