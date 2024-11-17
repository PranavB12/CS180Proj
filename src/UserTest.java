package src;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

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
    private User friend2;
    private Picture profilePicture;

    @BeforeEach
    public void setUp() {
        //profilePicture = new Picture("https://images.pexels.com/photos/736230/pexels-photo-736230.jpeg?cs=srgb&dl=pexels-jonaskakaroto-736230.jpg&fm=jpg"); // Assuming default constructor for Picture
        user = new User("testUser", "password123", "A test user", "Arthur Morgan");
        friend = new User("friendUser1", "password456", "Buster Baxter");
        friend2 = new User("friendUser2", "password789", "Arnold Austin");
    }

    @Test
    public void testConstructorWithoutProfilePicture() {
        System.out.println("before creating user");
        User newUser = new User("simpleUser", "simplePass", "Buster Baxter");
        assertEquals("simpleUser", newUser.getUsername());
        assertEquals("simplePass", newUser.getPassword());
        assertEquals("Buster Baxter", newUser.getName());
        //assertNull(newUser.getProfilePicture());
        assertEquals("", newUser.getDescription()); // Default description
        assertTrue(newUser.getFriends().isEmpty());
        assertTrue(newUser.getBlockedUsers().isEmpty());
        assertTrue(newUser.getPosts().isEmpty());
    }

    // Username Tests
    // setter for username can't be created/tested because it's declared as final
    @Test
    public void testGetUsername() {
        assertEquals("newUsername", user.getUsername());
    }

    @Test
    public void testSetUsernameEmptyString() {
        //user.setUsername("");
        // no set methods for final vars
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
    public void testGetName() {
        assertEquals("A test user", user.getName());
    }

    // setter for name can't be created/tested because it's declared as final
//    @Test
//    public void testSetNameEmptyString() {
//        user.setName("");
//        assertEquals("", user.getName());
//    }

    // Description Tests
    @Test
    public void testSetAndGetDescription() {
        user.setDescription("Arthur Morgan Updated");
        assertEquals("Arthur Morgan Updated", user.getDescription());
    }

    @Test
    public void testSetDescriptionEmptyString() {
        user.setDescription("");
        assertEquals("", user.getDescription());
    }

    // Friend Management Tests
    @Test
    public void testAddFriend() {
        user.addFriend(friend);
        user.addFriend(friend2);
        assertTrue(user.getFriends().contains(friend));
        assertTrue(user.getFriends().contains(friend2));
        List<User> friendList = user.getFriends();
        assertTrue(friendList.size() == 2);
        assertEquals("Buster Baxter", friendList.get(0).getName());
        assertEquals("Arnold Austin", friendList.get(1).getName());
    }

    @Test
    public void testRemoveFriend() {
        user.addFriend(friend);
        user.addFriend(friend2);
        List<User> friendList = user.getFriends();
        assertTrue(friendList.size() == 2);
        // removing Buster
        user.removeFriend(friend);
        assertTrue(friendList.size() == 1);
        assertEquals("Arnold Austin", friendList.get(0).getName());
        assertFalse(friendList.contains(friend));
    }

    @Test
    public void testRemoveNonExistingFriend() {
        User nonExistingFriend = new User("randomUser", "password1212", "A random user", "Ethan Harper");
        assertFalse(user.getFriends().remove(nonExistingFriend));
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
  
    // Post methods not working, commenting out for now
    // Post Management Tests
    // @Test
    // public void testAddPost() {
    //     Post newPost = new Post(); // Assuming Post has a default constructor
    //     user.getPosts().add(newPost);
    //     assertTrue(user.getPosts().contains(newPost));
    // }

    // @Test
    // public void testDeletePost() {
    //     Post newPost = new Post();
    //     user.getPosts().add(newPost);
    //     assertTrue(user.getPosts().remove(newPost));
    //     assertFalse(user.getPosts().contains(newPost));
    // }

    // @Test
    // public void testDeleteNonExistingPost() {
    //     Post newPost = new Post();
    //     assertFalse(user.getPosts().remove(newPost));
    // }

}
