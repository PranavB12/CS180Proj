
package src;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Map;


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
    private Post post1;
    private Post post2;
    private Picture picture1;
    private Picture picture2;
    private NewsFeed newsFeed;

    @BeforeEach
    public void setUp() {
        // Initialize users and pictures
        //Database db = new Database(List<User> users, Map<String, Post> posts, Map<String, Comment> comments);
        picture1 = new Picture("http://example.com/pic1.jpg");
        picture2 = new Picture("http://example.com/pic2.jpg");
        user1 = new User("user1", "pass1", "User One", "This is user1");
        user2 = new User("user2", "pass2",  "User Two", "This is user2");

        // Initialize posts
        //post1 = new Post("Hello World!", user1, 1);
        //post2 = new Post("Java is awesome!", user2, 2);

        // Initialize NewsFeed
        newsFeed = new NewsFeed();

        // Set up friendships
        user1.getFriends().add(user2);
        user2.getFriends().add(user1);
    }

    @org.junit.Test
    public void testGetUsers() {
        Database db = new Database();
        User user9 = new User("user9", "pass1", "User Nine", "This is user9");
        boolean result = db.addUser(user9);
        System.out.println(db.getUsers().size());
        assertEquals(db.getUsers().size(), 1);
    }

    @org.junit.Test
    public void testGetPosts() {
        Database db = new Database();
        User user9 = new User("user9", "pass1", "User Nine", "This is user9");
        boolean result = db.addUser(user9);
        String postId = db.createPost("New post 9", user9);
        assertEquals(db.getPosts().size(), 1);
    }

    @org.junit.Test
    public void testAddUser() {
        Database db = new Database();
        User user9 = new User("user9", "pass1", "User Nine", "This is user9");
        boolean result = db.addUser(user9);
        assertNotNull(user9);
        assertEquals(db.getUsers().size(), 1);
        assertEquals("user9", user9.getUsername());
    }

    @org.junit.Test
    public void testAddUserFailure() {
        Database db = new Database();
        boolean result = db.addUser(user1);
        assertFalse(result, "Adding a duplicate user should return false.");
        boolean result2 = db.addUser(null);
        assertFalse(result, "Adding a null user should return false.");
    }

    @org.junit.Test
    public void testCreatePost() {
        Database db = new Database();
        User user9 = new User("user9", "pass1", "User Nine", "This is user9");
        boolean result = db.addUser(user9);
        String postId = db.createPost("New post 9", user9);
        Assertions.assertTrue(postId != null, "A valid post should include a valid id.");
    }

    @org.junit.Test
    public void testAddPostToUser() {
        Database db = new Database();
        User user9 = new User("user9", "pass1", "User Nine", "This is user9");
        boolean result = db.addUser(user9);
        String postId = db.createPost("New post 9", user9);
        Assertions.assertTrue(db.getPosts().containsKey(postId), "Post should be added to the database.");
    }

    @org.junit.Test
    public void testCommentOnPost() {
        // String postId, String comment , User commentAuthor
        Database db = new Database();
        User user10 = new User("user10", "pass1", "User Ten", "This is user10");
        boolean result = db.addUser(user10);
        String postId = db.createPost("New post 10", user10);
        Comment com = new Comment("This is User10 comment", user10, postId);
        //System.out.println(com);
        Post post1 = db.getPosts().get(postId);
        post1.addComment(com.getID(), com);
        assertTrue(post1.getComments().containsKey(com.getID()));
    }

    @org.junit.Test
    public void testDeleteComment() {
        Database db = new Database();
        User user10 = new User("user10", "pass1", "User Ten", "This is user10");
        boolean result = db.addUser(user10);
        String postId = db.createPost("New post 10", user10);
        Comment com = new Comment("This is User10 comment", user10, postId);
        Post post1 = db.getPosts().get(postId);
        post1.addComment(com.getID(), com);
        assertEquals(1, post1.getComments().size());
        post1.deleteComment(com.getID(), user10);
        assertEquals(0, post1.getComments().size());
    }

    @org.junit.Test
    public void testHidePost() {
        Database db = new Database();
        User user10 = new User("user10", "pass1", "User Ten", "This is user10");
        boolean result = db.addUser(user10);
        String postId = db.createPost("New post 10", user10);
        Post post1 = db.getPosts().get(postId);
        post1.hidePost();
        assertTrue(post1.isHidden());
    }

    @org.junit.Test
    public void testDeletePost() {
        Database db = new Database();
        User user10 = new User("user10", "pass1", "User Ten", "This is user10");
        boolean result = db.addUser(user10);
        String postId = db.createPost("New post 10", user10);
        Post post1 = db.getPosts().get(postId);
        user10.getPosts().add(post1);
        db.deletePost(post1.getId(), user10);
        assertFalse(user10.getPosts().contains(post1));
    }

    // newsfeed not a part of database.java
//    @org.junit.Test
//    public void testNewsFeedDisplayPosts() {
//        user1.getPosts().add(post1);
//        user2.getPosts().add(post2);
//        newsFeed.displayPosts(user1); // User1's feed should show posts from user2
//
//        // Ideally, you would check for output, but this is a basic check
//        assertFalse(newsFeed.getPosts().isEmpty());
//    }
//
//    @org.junit.Test
//    public void testNewsFeedDeletePost() {
//        user1.getPosts().add(post1);
//        newsFeed.deletePost(post1);
//        assertFalse(newsFeed.getPosts().contains(post1));
//    }


    @org.junit.Test
    public void testEnableDisableComments() {
        Database db = new Database();
        User user10 = new User("user10", "pass1", "User Ten", "This is user10");
        boolean result = db.addUser(user10);
        String postId = db.createPost("New post 10", user10);
        Comment com = new Comment("This is User10 comment", user10, postId);
        Post post1 = db.getPosts().get(postId);
        post1.addComment(com.getID(), com);

        post1.disableComments();
        assertFalse(post1.areCommentsEnabled());

        post1.enableComments();
        assertTrue(post1.areCommentsEnabled());
    }

    @org.junit.Test
    public void testMultipleComments() {
        Database db = new Database();
        User user10 = new User("user10", "pass1", "User Ten", "This is user10");
        boolean result = db.addUser(user10);
        String postId = db.createPost("New post 10", user10);
        Comment com1 = new Comment("This is User10 first comment", user10, postId);
        Comment com2 = new Comment("This is User10 second comment", user10, postId);
        Post post1 = db.getPosts().get(postId);
        post1.addComment(com1.getID(), com1);
        post1.addComment(com2.getID(), com2);

        assertEquals(2, post1.getComments().size());
    }

    @org.junit.Test
    public void testDownvotePost() {
        Database db = new Database();
        User user10 = new User("user10", "pass1", "User Ten", "This is user10");
        User user12 = new User("user12", "pass1", "User Twelve", "This is user12");
        boolean result = db.addUser(user10);
        boolean result2 = db.addUser(user12);
        String postId = db.createPost("New post 10", user10);
        Post post1 = db.getPosts().get(postId);
        assertEquals(0, post1.getDownVotes());
        post1.downvote("user10");
        assertEquals(1, post1.getDownVotes());
        // tests that another user aside from user10 can downvote the post
        post1.downvote("user12");
        assertEquals(2, post1.getDownVotes());
    }

    // test case for Post class
    // can additionally test for upvoters
    @org.junit.Test
    public void testUpvotePost() {
        Database db = new Database();
        User user10 = new User("user10", "pass1", "User Ten", "This is user10");
        User user12 = new User("user12", "pass1", "User Twelve", "This is user12");
        boolean result = db.addUser(user10);
        boolean result2 = db.addUser(user12);
        String postId = db.createPost("New post 10", user10);
        Post post1 = db.getPosts().get(postId);
        assertEquals(0, post1.getUpVotes());
        post1.upvote("user10");
        assertEquals(1, post1.getUpVotes());
        // tests that another user aside from user10 can upvote the post
        post1.upvote("user12");
        assertEquals(2, post1.getUpVotes());
    }

    @org.junit.Test
    public void testBlockedUsers() {
        Database db = new Database();
        User user10 = new User("user10", "pass1", "User Ten", "This is user10");
        User user12 = new User("user12", "pass1", "User Twelve", "This is user12");
        user10.blockUser(user12);
        assertTrue(user10.getBlockedUsers().contains(user12));
    }

    // revisit this test case
    @org.junit.Test
    public void testUserEquals() {
        Database db = new Database();
        User user1 = new User("user", "pass", "User", "This is user");
        User user3 = new User("user", "pass", "User", "This is user");
        db.addUser(user1);
        db.addUser(user3);
        assertTrue(user1.equals(user3));
    }

    // New test cases for comments, pictures, and news feed
    @org.junit.Test
    public void testPostWithNoComments() {
        Database db = new Database();
        User user10 = new User("user10", "pass1", "User Ten", "This is user10");
        boolean result = db.addUser(user10);
        String postId = db.createPost("New post 10", user10);
        Post post1 = db.getPosts().get(postId);
        assertEquals(0, post1.getComments().size());
    }

    @org.junit.Test
    public void testPictureUrl() {
        Picture picture1 = new Picture("http://example.com/pic1.jpg");
        Picture picture2 = new Picture("http://example.com/pic2.jpg");
        assertEquals("http://example.com/pic1.jpg", picture1.getUrl());
        assertEquals("http://example.com/pic2.jpg", picture2.getUrl());
    }

    // these tests should be in NewsFeedTest class
//    @org.junit.Test
//    public void testNewsFeedWithMultiplePosts() {
//        user1.getPosts().add(post1);
//        user2.getPosts().add(post2);
//        newsFeed.displayPosts(user1); // User1's feed should show posts from user2
//
//        // Ideally, you would check for output, but this is a basic check
//        assertFalse(newsFeed.getPosts().isEmpty());
//    }
//
//    @Test
//    public void testDisplayPostsWithNoFriends() {
//        User lonelyUser = new User("lonelyUser", "lonelyPass", "Lonely User");
//        newsFeed.displayPosts(lonelyUser); // No friends, no posts
//
//        // Validate that the news feed is empty
//        assertTrue(newsFeed.getPosts().isEmpty());
//    }

    @org.junit.Test
    public void testAddCommentWhenDisabled() {
        Database db = new Database();
        User user10 = new User("user10", "pass1", "User Ten", "This is user10");
        boolean result = db.addUser(user10);
        String postId = db.createPost("New post 10", user10);
        Post post1 = db.getPosts().get(postId);
        Comment com = new Comment("This is User10 comment", user10, postId);
        post1.addComment(com.getID(), com);
        post1.disableComments();
        assertFalse(post1.areCommentsEnabled());
        Comment newCom = new Comment("Comment after disabling comments on post.", user10, postId);
        // handling exception when comment is added after comments are disabled
        Exception exception = assertThrows(IllegalStateException.class, () -> {
            post1.addComment(newCom.getID(), newCom);
        });
        assertEquals("Comments are disabled for this post.", exception.getMessage());
        assertEquals(1, post1.getComments().size());
    }
}
