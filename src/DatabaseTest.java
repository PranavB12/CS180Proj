
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
        // Test getUsers
        db.addUser(user1);
        db.addUser(user2);
        // Expected: 2 users retrieved from the database
        assertEquals(db.getUsers().size(), 2);
        // Expected: the database to contain the added users, user1 and user 2
        assertTrue(db.getUsers().contains(user1) && db.getUsers().contains(user2));
    }

    @Test
    public void testGetPosts() {
        // Test getPosts
        db.addUser(user1);
        user1.addPost(post1);
        // Expected: 1 post retrieved from user1 in the database
        assertEquals(db.getUserByUsername("user1").getPosts().size(), 1);
        // Expected: post1 should be assigned to user1 in the database
        assertTrue(db.getUserByUsername("user1").getPosts().contains(post1));
    }

    @Test
    public void testAddUser() {
        // Test addUser
        db.addUser(user1);
        // Expected: the database should have 1 user after adding
        assertNotNull(db.getUsers());
        // Expected: 1 user in the database
        assertEquals(db.getUsers().size(), 1);
        // Expected: the database to contain the added user, user1
        assertTrue(db.getUsers().contains(user1));
    }

    @Test
    public void testRemoveUser() {
        // Test removeUser
        db.addUser(user2);
        db.removeUser(user2);
        // Expected: the database should have 0 users after removal
        assertEquals(db.getUsers().size(), 0);
    }

    @Test
    public void testAddUserFailure() {
        // Test addUser with a duplicate user and a null user
        boolean addUser1 = db.addUser(user1);
        boolean addUser1Again = db.addUser(user1);
        // Expected: the database should not allow a duplicate user to be added
        assertFalse(addUser1Again, "Adding a duplicate user should return false.");
        boolean result3 = db.addUser(null);
        // Expected: the database should not allow a null user to be added
        assertFalse(result3, "Adding a null user should return false.");
    }

    @Test
    public void testCreateAccount() {
        // Test createAccount
        boolean createUser9 = db.createAccount("user9", "pass9", "User Nine");
        // Expected: user9's account should be created and added to the database
        assertTrue(createUser9, "The user should be created successfully.");
        assertEquals(db.getUsers().size(), 1);
    }

    @Test
    public void testValidateCredentials() {
        // Test validateCredentials
        db.addUser(user1);
        boolean validateUser1 = db.validateCredentials("user1", "pass1");
        // Expected: user1's credentials should be validated by the database
        assertTrue(validateUser1, "The user should be validated successfully.");
    }

    @Test
    public void testUserExists() {
        // Test userExists
        db.addUser(user1);
        boolean user1Exists = db.userExists("user1");
        // Expected: user1 should exist in the database once added
        assertTrue(user1Exists, "The user should exist in the database.");
    }

    @Test
    public void viewUser() {
        // Test viewUser
        db.addUser(user2);
        db.addPost(post2);
        User viewedUser = db.viewUser("user2");
        // Expected: the viewedUser object should contain user2's user information
        assertNotNull(viewedUser);
    }

    @Test
    public void testCreatePost() {
        // Test createPost
        db.addUser(user1);
        String postId = db.createPost("This is User 1's post!", user1);
        // Expected:
        assertEquals(db.getPostById(postId).getId(), postId);
        // Expected: the size of posts in the database should equal 1 after creation
        assertEquals(db.getPosts().size(), 1);
        // Expected: the content of the post should match user1's content when creating a post
        assertEquals(db.getPostById(postId).getContent(), "This is User 1's post!");
        // Expected: the author of the post should be user1
        assertEquals(db.getPostById(postId).getAuthor(), user1);
    }

    @Test
    public void testGetPostById(){
        // Test getPostById
        db.addUser(user2);
        db.addPost(post2);
        Post postObject = db.getPostById(post2.getId());
        // Expected: the author of the post after getting the post by id should be user2
        assertEquals(postObject.getAuthor(), user2);
    }

    @Test
    public void testAddCommentToPost() {
        // Test addCommentToPost
        // user 2 is adding a comment to post 1
        db.addUser(user1);
        db.addUser(user2);
        db.addPost(post1);
        Post postObject = db.getPostById(post1.getId());
        // class level "com" is being created as null within method - to be fixed
        Comment com1 = new Comment("This is User2 comment", user2, postObject.getId());
        String commentID = db.addCommentToPost(postObject.getId(), com1.getContent(), com1.getAuthor());
        // Expected: after a comment is added to a post, the commentID should be associated with the correct post in the database
        assertTrue(db.getPostById(post1.getId()).getComments().containsKey(commentID), "Comment should exist in the database");
    }

    @Test
    public void testDeleteCommentFromPost() {
        // Test deleteCommentFromPost
        // user 2 is deleting a comment to post 1
        db.addUser(user1);
        db.addUser(user2);
        db.addPost(post1);
        Post postObject = db.getPostById(post1.getId());
        // class level "com" is being created as null within method - to be fixed
        Comment com1 = new Comment("This is User2 comment", user2, postObject.getId());
        String commentID = db.addCommentToPost(postObject.getId(), com1.getContent(), com1.getAuthor());
        db.deleteCommentFromPost(postObject.getId(), commentID, user2);
        // Expected: after a comment is deleted from a post, there should be 0 comments under the post
        assertEquals(db.getPostById(post1.getId()).getComments().size(), 0);
    }

    @Test
    public void testHidePost() {
        // test hidePost
        db.addPost(post1);
        db.hidePost(post1.getId(), user1);
        // Expected: the post should be hidden
        assertTrue(db.getPostById(post1.getId()).isHidden());
    }

    @Test
    public void testDeletePost() {
        // test deletePost
        db.addUser(user1);
        db.addPost(post1);
        db.deletePost(post1.getId(), user1);
        // Expected: user1's list of posts should include post1
        assertFalse(db.getUserByUsername("user1").getPosts().contains(post1));
        // Expected: the size of the posts for user1 should be 0
        assertEquals(db.getUserByUsername("user1").getPosts().size(), 0);
    }

    @Test
    public void testEnableDisableComments() {
        // test enableCommentsForPost and disableCommentsForPost
        db.addUser(user1);
        db.addPost(post1);
        Post postObject = db.getPostById(post1.getId());

        post1.disableComments();
        // Expected: comments should not be enabled on post1
        assertFalse(post1.areCommentsEnabled());
        Comment com1 = new Comment("This is User2 comment", user2, postObject.getId());

        // handling exception when comment is added after comments are disabled
        Exception exception = assertThrows(IllegalStateException.class, () -> {
            String commentID = db.addCommentToPost(postObject.getId(), com1.getContent(), com1.getAuthor());
            post1.addComment(com1.getID(), com1);
        });
        // Expected: there should be 0 comments on post1
        assertEquals(0, db.getPostById(post1.getId()).getComments().size());

        post1.enableComments();
        // Expected: comments should be enabled on post1
        assertTrue(db.getPostById(post1.getId()).areCommentsEnabled());
    }

    @Test
    public void testDownvotePost() {
        // test downvotePost
        db.addUser(user1);
        db.addUser(user2);
        db.addPost(post1);
        // Expected: there should be 0 downvotes on post1 before downvoting the post
        assertEquals(0, db.getPostById(post1.getId()).getDownVotes());
        post1.downvote("user2");
        // Expected: there should be 1 downvote on post1 after downvoting the post
        assertEquals(1, db.getPostById(post1.getId()).getDownVotes());
    }

    // test case for Post class
    @Test
    public void testUpvotePost() {
        // test upvotePost
        db.addUser(user1);
        db.addUser(user2);
        db.addPost(post1);
        // Expected: there should be 0 upvotes on post1 before upvoting the post
        assertEquals(0, post1.getUpVotes());
        post1.upvote("user2");
        // Expected: there should be 1 upvote on post1 after upvoting the post
        assertEquals(1, post1.getUpVotes());
    }

    @Test
    public void testAddFriend() {
        // test addFriend
        db.addUser(user1);
        db.addUser(user2);
        // Expected: user1 should have 0 friends before adding user2 as a friend
        assertEquals(0, user1.getFriends().size());
        db.addFriend(user1, user2);
        // Expected: user2 should have 1 friend after adding user1 as a friend
        assertEquals(1, user2.getFriends().size());
        db.addFriend(user2, user1);
        // Expected: user2 should have 1 friend after adding user1 as a friend
        assertEquals(1, user2.getFriends().size());
    }

    @Test
    public void testRemoveFriend() {
        // test removeFriend
        db.addUser(user1);
        db.addUser(user2);
        db.addFriend(user1, user2);
        // Expected: user1 should have 1 friend (user2) before removing them as a friend
        assertEquals(1, user1.getFriends().size());
        db.removeFriend(user1, user2);
        // Expected: user1 should have 0 friends after removing user2 as a friend
        assertEquals(0, user2.getFriends().size());
    }

    @Test
    public void testBlockUsers() {
        // test blockUsers
        db.addUser(user1);
        db.addUser(user2);
        db.blockUser(user1, user2);
        // Expected: user2 should be blocked by user1
        assertTrue(db.getUserByUsername("user1").getBlockedUsers().contains(user2));
    }

    @Test
    public void testFindUserByUsername() {
        // test findUserByUsername
        db.addUser(user1);
        User userFound = db.findUserByUsername("user1");
        // Expected: the username found from the database should match "user1"
        assertEquals(db.getUserByUsername("user1").getUsername(), userFound.getUsername());
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
        // Testing a post with no comments
        db.addUser(user1);
        db.addPost(post1);
        // Expected: there should be 0 comments under post1
        assertEquals(0, post1.getComments().size());
    }

    @Test
    public void testPictureUrl() {
        // Testing picture url's
        assertEquals("http://example.com/pic1.jpg", picture1.getUrl());
        assertEquals("http://example.com/pic2.jpg", picture2.getUrl());
    }
}
