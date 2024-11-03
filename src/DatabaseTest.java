package src;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseTest {
    private Database database;
    private ArrayList<User> userList;

    @BeforeEach
    void setUp() {
        // Initialize user instances
        User alice = new User("alice123", "password1", "Alice");
        User bob = new User("bob456", "password2", "Bob");
        User charlie = new User("charlie789", "password3", "Charlie");
        userList = new ArrayList<>();
        userList.add(alice);
        userList.add(bob);
        userList.add(charlie);

        DatabaseImpl databaseImpl = new DatabaseImpl(userList, null);

    }

    @AfterEach
    void tearDown() {
        database = null;
        userList.clear();
    }

    @Test
    void addUser() {
        DatabaseImpl database = new DatabaseImpl(userList, null);
        User vaishi = new User("vaishi123", "password1", "Vaishi");
        assertTrue(database.addUser(vaishi), "User should be added successfully.");
        assertTrue(database.userExists("vaishi123"), "User should exist in database.");
    }

    @Test
    void removeUser() {
        User alice = new User("alice123", "password1", "Alice");
        User bob = new User("bob456", "password2", "Bob");
        User charlie = new User("charlie789", "password3", "Charlie");
        userList = new ArrayList<>();
        userList.add(alice);
        userList.add(bob);
        userList.add(charlie);

        DatabaseImpl database = new DatabaseImpl(userList, null);

        System.out.println("Size: " + userList.size());
        System.out.println("User 1 " + userList.getFirst().getUsername());
        int i=0 ;
        System.out.println("User 1 " + userList.get(i).getUsername());
        User userToRemove = userList.get(i);
        assertTrue(database.removeUser(userToRemove), "User should be removed successfully.");
        assertFalse(database.userExists(userToRemove.getUsername()), "Removed user should not exist.");
    }

    @Test
    void createAccount() {
        DatabaseImpl database = new DatabaseImpl(userList, null);
        User newUser = new User("newUser123", "password123", "New User");
        assertTrue(database.addUser(newUser), "Account creation should be successful.");
        assertTrue(database.userExists("newUser123"), "New user should exist after account creation.");
    }

    // fails because of findUserByUsername
    @Test
    void validateCredentials() {
        DatabaseImpl database = new DatabaseImpl(userList, null);
        assertTrue(database.validateCredentials("alice123", "password1"), "Credentials should be valid for Alice.");
        assertFalse(database.validateCredentials("bob456", "wrongpassword"), "Credentials should be invalid with incorrect password.");
    }

    // fails
    @Test
    void userExists() {
        DatabaseImpl database = new DatabaseImpl(userList, null);
        assertTrue(database.userExists("alice123"), "Alice should exist in the database.");
        assertFalse(database.userExists("unknownUser"), "Unknown user should not exist.");
    }

    @Test
    void createPost() {
        HashMap<Integer, Post> posts = new HashMap<>();
        Post newPost1 = new PostImpl("This is Alice's new post", userList.get(0));
        Post newPost2 = new PostImpl("This is a Bob's post", userList.get(1));
        Post newPost3 = new PostImpl("This is a Charlie's post", userList.get(2));
        posts.put(newPost1.getId(), newPost1);
        posts.put(newPost2.getId(), newPost2);
        posts.put(newPost3.getId(), newPost3);

        DatabaseImpl database = new DatabaseImpl(userList, posts);
        Post newPost4 = new PostImpl("This is my post", userList.get(2));
        PostImpl newPost = new PostImpl("This is a new post", userList.getFirst());
        int postId = database.addPost(newPost);
        assertNotEquals(-1, postId, "Post should be created successfully.");
    }

    // fails
    @Test
    void deletePost() {
        PostImpl post = new PostImpl("Delete this post", userList.get(1));
        DatabaseImpl database = new DatabaseImpl(userList, posts);
        int postId = database.addPost(post);
        assertTrue(database.deletePost(postId), "Post should be deleted successfully.");
    }

    @Test
    void viewUser() {
        User viewedUser = database.viewUser("alice123");
        assertNotNull(viewedUser, "User should be viewable in the database.");
        assertEquals("Alice", viewedUser.getName(), "Viewed user should match expected details.");
    }

    @Test
    void addFriend() {
        assertTrue(database.addFriend(userList.get(0), "bob456"), "Should be able to add Bob as a friend.");
    }

    @Test
    void removeFriend() {
        assertTrue(database.addFriend(userList.get(0), "bob456"), "Should be able to add Bob as a friend.");
        assertTrue(database.removeFriend(userList.get(0), "bob456"), "Should be able to remove Bob from friends.");
    }

    @Test
    void blockUser() {
        assertTrue(database.blockUser(userList.get(0), "charlie789"), "Should be able to block Charlie.");
    }

    @Test
    void upvotePost() {
        User alice = new User("alice123", "password1", "Alice");
        User bob = new User("bob456", "password2", "Bob");
        User charlie = new User("charlie789", "password3", "Charlie");
        userList = new ArrayList<>();
        userList.add(alice);
        userList.add(bob);
        userList.add(charlie);

        HashMap<Integer, Post> posts = new HashMap<>();
        Post newPost1 = new PostImpl("This is Alice's new post", userList.get(0));
        Post newPost2 = new PostImpl("This is a Bob's post", userList.get(1));
        Post newPost3 = new PostImpl("This is a Charlie's post", userList.get(2));
        posts.put(newPost1.getId(), newPost1);
        posts.put(newPost2.getId(), newPost2);
        posts.put(newPost3.getId(), newPost3);

        DatabaseImpl databaseImpl = new DatabaseImpl(userList, posts);
        int postIdToUpvote = newPost1.getId();
        Post post = posts.get(postIdToUpvote);
        int beforeUpVoting = post.getUpVotes();
        System.out.println("Before voting: " + beforeUpVoting);
        databaseImpl.upvotePost(postIdToUpvote);

        // Check that the upvote count increased by 1
        assertEquals(beforeUpVoting + 1, post.getUpVotes(), "Upvote count should increase by 1.");
    }
    /**
    @Test
    void downvotePost() {
        PostImpl post = new PostImpl("Downvote this post", userList.get(1));
        int postId = database.addPost(post);
        assertTrue(database.downvotePost(postId), "Post should be downvoted successfully.");
    }

    @Test
    void addCommentToPost() {
        PostImpl post = new PostImpl("Comment on this post", userList.get(1));
        int postId = database.addPost(post);
        assertTrue(database.addCommentToPost(postId, "This is a comment", userList.get(0)), "Comment should be added to post.");
    }

    @Test
    void deleteCommentFromPost() {
        PostImpl post = new PostImpl("Post with comments", userList.get(1));
        int postId = database.addPost(post);
        int commentId = database.addCommentToPost(postId, "This is a comment", userList.get(0));
        assertTrue(database.deleteCommentFromPost(postId, commentId), "Comment should be deleted from post.");
    }

    @Test
    void hidePost() {
        PostImpl post = new PostImpl("Hide this post", userList.get(1));
        int postId = database.addPost(post);
        assertTrue(database.hidePost(postId), "Post should be hidden successfully.");
    }

    @Test
    void enableCommentsForPost() {
        PostImpl post = new PostImpl("Enable comments for this post", userList.get(2));
        int postId = database.addPost(post);
        assertTrue(database.enableComments(postId), "Comments should be enabled for post.");
    }

    @Test
    void disableCommentsForPost() {
        PostImpl post = new PostImpl("Disable comments for this post", userList.get(2));
        int postId = database.addPost(post);
        assertTrue(database.disableComments(postId), "Comments should be disabled for post.");
    }

    @Test
    void saveDatabaseToFile() {
        assertTrue(database.saveDatabaseToFile("database.txt"), "Database should be saved to file successfully.");
    }

    @Test
    void readDatabaseFromFile() {
        assertTrue(database.readDatabaseFromFile("database.txt"), "Database should be read from file successfully.");
    }*/
}
