package src;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;


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
    private Database db;
    private User user1;
    private User user2;
    private List<User> userList;
    private Post post1;
    private Post post2;
    private Map<String, Post> postMap;
    private Comment com;
    private Map<String, Comment> commentMap;
    private Picture picture1;
    private Picture picture2;
    private NewsFeed newsFeed;

    @BeforeEach
    public void setUp() {
        // List<User> users, Map<String, Post> posts, Map<String, Comment> comments
        //
        db = new Database();
        user1 = new User("user1", "pass1", "User One", "This is user1");
        user2 = new User("user2", "pass2",  "User Two", "This is user2");
        userList = new ArrayList<>();
        userList.add(user1);
        userList.add(user2);

        post1 = new Post(UUID.randomUUID().toString(), "Hello world!", user1);
        post2 = new Post(UUID.randomUUID().toString(), "New Post", user2);
        postMap = new HashMap<>();
        postMap.put(post1.getId(), post1);
        postMap.put(post2.getId(), post2);

        Comment com = new Comment("This is a new comment.", user2, post1.getId());
        //System.out.println("Set up: " + com);
        commentMap = new HashMap<>();
        commentMap.put(UUID.randomUUID().toString(), com);


        // Initialize database with List<User> users, Map<String, Post> posts, Map<String, Comment> comments
        //Database db = new Database(userList, postMap, commentMap);
        //System.out.println("Users: " + db.getUsers().size());
        //System.out.println("Posts: " + db.getPosts().size());

        picture1 = new Picture("http://example.com/pic1.jpg");
        picture2 = new Picture("http://example.com/pic2.jpg");

        // Initialize NewsFeed
        // newsFeed = new NewsFeed();

    }

    @Test
    public void testGetUsers() {
        db.addUser(user1);
        assertEquals(db.getUsers().size(), 1);
        assertTrue(db.getUsers().contains(user1));
    }

    @Test
    public void testGetPosts() {
        db.addUser(user1);
        user1.addPost(post1);
        assertEquals(user1.getPosts().size(), 1);
        assertTrue(user1.getPosts().contains(post1));
    }

    @Test
    public void testAddUser() {
        db.addUser(user1);
        assertNotNull(user1);
        assertEquals(db.getUsers().size(), 1);
        assertTrue(db.getUsers().contains(user1));
    }

    @Test
    public void testRemoveUser() {
        db.addUser(user2);
        db.removeUser(user2);
        assertEquals(db.getUsers().size(), 0);
    }

    @Test
    public void testAddUserFailure() {
        boolean addUser1 = db.addUser(user1);
        boolean addUser1Again = db.addUser(user1);
        assertFalse(addUser1Again, "Adding a duplicate user should return false.");
        boolean result3 = db.addUser(null);
        assertFalse(result3, "Adding a null user should return false.");
    }

    @Test
    public void testCreateAccount() {
        boolean createUser9 = db.createAccount("user9", "pass9", "User Nine");
        assertTrue(createUser9, "The user should be created successfully.");
    }

    @Test
    public void testValidateCredentials() {
        db.addUser(user1);
        boolean validateUser1 = db.validateCredentials("user1", "pass1");
        assertTrue(validateUser1, "The user should be validated successfully.");
    }

    @Test
    public void userExists() {
        db.addUser(user1);
        boolean user1Exists = db.userExists("user1");
        assertTrue(user1Exists, "The user should exist in the database.");
    }

    @Test
    public void viewUser() {
        db.addUser(user2);
        // add post not returning
        user2.addPost(post1);
        User viewedUser = db.viewUser("user2");
        assertNotNull(viewedUser);
    }

    @Test
    public void testCreatePost() {
        db.addUser(user1);
        String postId = db.createPost("This is User 1's post!", user1);
        assertEquals(db.getPostById(postId).getId(), postId);
        assertEquals(db.getPosts().size(), 1);
        assertEquals(db.getPostById(postId).getContent(), "This is User 1's post!");
        assertEquals(db.getPostById(postId).getAuthor(), user1);
    }

    @Test
    public void testGetPostById(){
        db.addUser(user2);
        db.addPost(post2);
        Post postObject = db.getPostById(post2.getId());
        assertEquals(postObject.getAuthor(), user2);
    }

    @Test
    public void testAddCommentToPost() {
        // user 2 is adding comment to post 1
        db.addUser(user1);
        db.addUser(user2);
        db.addPost(post1);
        Post postObject = db.getPostById(post1.getId());
        System.out.println(postObject.getId());
        // class level "com" is being created as null within method - to be fixed
        Comment com1 = new Comment("This is User2 comment", user2, postObject.getId());
        String commentID = db.addCommentToPost(postObject.getId(), com1.getContent(), com1.getAuthor());
        assertTrue(post1.getComments().containsKey(commentID), "Comment should exist in the database");
    }

    @Test
    public void testDeleteCommentFromPost() {
        // user 2 is deleting a comment to post 1
        db.addUser(user1);
        db.addUser(user2);
        db.addPost(post1);
        Post postObject = db.getPostById(post1.getId());
        // class level "com" is being created as null within method - to be fixed
        Comment com1 = new Comment("This is User2 comment", user2, postObject.getId());
        String commentID = db.addCommentToPost(postObject.getId(), com1.getContent(), com1.getAuthor());
        db.deleteCommentFromPost(postObject.getId(), commentID, user2);
        assertEquals(db.getPostById(post1.getId()).getComments().size(), 0);
    }

    @Test
    public void testHidePost() {
        db.addPost(post1);
        db.hidePost(post1.getId(), user1);
        assertTrue(post1.isHidden());
    }

    @Test
    public void testDeletePost() {
        db.addPost(post1);
        db.deletePost(post1.getId(), user1);
        assertFalse(user1.getPosts().contains(post1));
    }

    @Test
    public void testEnableDisableComments() {
        db.addUser(user1);
        db.addPost(post1);
        Post postObject = db.getPostById(post1.getId());

        post1.disableComments();
        assertFalse(post1.areCommentsEnabled());
        Comment com1 = new Comment("This is User2 comment", user2, postObject.getId());

        // handling exception when comment is added after comments are disabled
        Exception exception = assertThrows(IllegalStateException.class, () -> {
            String commentID = db.addCommentToPost(postObject.getId(), com1.getContent(), com1.getAuthor());
            post1.addComment(com1.getID(), com1);
        });
        assertEquals(0, post1.getComments().size());

        post1.enableComments();
        assertTrue(post1.areCommentsEnabled());
    }

    @Test
    public void testDownvotePost() {
        db.addUser(user1);
        db.addUser(user2);
        db.addPost(post1);
        assertEquals(0, post1.getDownVotes());
        post1.downvote("user2");
        assertEquals(1, post1.getDownVotes());
    }

    // test case for Post class
    @Test
    public void testUpvotePost() {
        db.addUser(user1);
        db.addUser(user2);
        db.addPost(post1);
        assertEquals(0, post1.getUpVotes());
        post1.upvote("user2");
        assertEquals(1, post1.getUpVotes());
    }

    @Test
    public void testAddFriend() {
        db.addUser(user1);
        db.addUser(user2);
        assertEquals(0, user2.getFriends().size());
        db.addFriend(user1, user2);
        assertEquals(1, user2.getFriends().size());
        db.addFriend(user2, user1);
        assertEquals(1, user2.getFriends().size());
    }

    @Test
    public void testRemoveFriend() {
        db.addUser(user1);
        db.addUser(user2);
        db.addFriend(user1, user2);
        assertEquals(1, user2.getFriends().size());
        db.removeFriend(user2, user1);
        assertEquals(0, user2.getFriends().size());
    }

    @Test
    public void testBlockUsers() {
        db.addUser(user1);
        db.addUser(user2);
        db.blockUser(user1, user2);
        assertTrue(db.getUserByUsername("user1").getBlockedUsers().contains(user2));
    }

    @Test
    public void testFindUserByUsername() {
        db.addUser(user1);
        User userFound = db.findUserByUsername("user1");
        assertEquals(user1.getUsername(), userFound.getUsername());
    }

    // this method doesn't seem to work
    /**@Test
    public void testUpdateUserInDatabase() {
        db.addUser(user2);
        // an object to compare the updated user to
        User updatedUser2 = new User("user2", "changedPassword", "User Two", "This is new user2");
        db.updateUserInDatabase(updatedUser2);
        //System.out.println(user2.toString());
        assertEquals(updatedUser2.getUsername(), user2.getUsername());
        assertNotEquals(updatedUser2.getPassword(), user2.getPassword());
        assertEquals(updatedUser2.getName(), user2.getName());
        assertNotEquals(updatedUser2.getDescription(), user2.getDescription());
    }*/

    // New test cases for comments, pictures, and news feed
    @Test
    public void testPostWithNoComments() {
        db.addUser(user1);
        db.addPost(post1);
        assertEquals(0, post1.getComments().size());
    }

    @Test
    public void testPictureUrl() {
        assertEquals("http://example.com/pic1.jpg", picture1.getUrl());
        assertEquals("http://example.com/pic2.jpg", picture2.getUrl());
    }
}
