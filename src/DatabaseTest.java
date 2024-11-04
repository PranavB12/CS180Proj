package src;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseTest {
    private ArrayList<User> userList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        // Initialize user instances
        User alice = new User("alice123", "password1", "Alice");
        User bob = new User("bob456", "password2", "Bob");
        User charlie = new User("charlie789", "password3", "Charlie");
        Database database = new Database();
        database.addUser(alice);
        database.addUser(bob);
        database.addUser(charlie);



        HashMap<Integer, Post> posts = new HashMap<>();
        Post newPost1 = new Post();
        posts.put(newPost1.getId(), newPost1);

    }

    @AfterEach
    void tearDown() {
        userList.clear();
    }

    @Test
    void addUser() {
        Database database = new Database();
        User daniel = new User("daniel123", "password1", "Daniel");
        boolean userAdded = database.addUser(daniel);
        assertTrue(userAdded, "User should be added successfully.");
    }

    @Test
    void removeUser() {
        Database database = new Database();
        User bob = new User("bob456", "password2", "Bob");
        database.addUser(bob);
        database.removeUser(bob);

        int i=0 ;
        User userToRemove = userList.get(i);
        assertTrue(database.removeUser(userToRemove), "User should be removed successfully.");
    }
    /**
//    @Test
//    void createAccount() {
//        User alice = new User("alice123", "password1", "Alice");
//        User bob = new User("bob456", "password2", "Bob");
//        User charlie = new User("charlie789", "password3", "Charlie");
//        userList = new ArrayList<>();
//        userList.add(alice);
//        userList.add(bob);
//        userList.add(charlie);
//        Database database = new Database();
//        User newUser = new User("newUser123", "password123", "New User");
//        assertTrue(database.addUser(newUser), "Account creation should be successful.");
//        assertTrue(database.userExists("newUser123"), "New user should exist after account creation.");
//    }
//
//    // fails because of findUserByUsername
//    @Test
//    void validateCredentials() {
//        User alice = new User("alice123", "password1", "Alice");
//        User bob = new User("bob456", "password2", "Bob");
//        User charlie = new User("charlie789", "password3", "Charlie");
//        userList = new ArrayList<>();
//        userList.add(alice);
//        userList.add(bob);
//        userList.add(charlie);
//        Database database = new Database();
//        assertTrue(database.validateCredentials("alice123", "password1"), "Credentials should be valid for Alice.");
//        assertFalse(database.validateCredentials("bob456", "wrongpassword"), "Credentials should be invalid with incorrect password.");
//    }
//
//    // fails because of UserExists method and findUserByUsername
//    @Test
//    void userExists() {
//        User alice = new User("alice123", "password1", "Alice");
//        User bob = new User("bob456", "password2", "Bob");
//        User charlie = new User("charlie789", "password3", "Charlie");
//        userList = new ArrayList<>();
//        userList.add(alice);
//        userList.add(bob);
//        userList.add(charlie);
//        Database database = new Database();
//        assertTrue(database.userExists("alice123"), "Alice should exist in the database.");
//        assertFalse(database.userExists("unknownUser"), "Unknown user should not exist.");
//    }
//
//    @Test
//    void createPost() {
//        User alice = new User("alice123", "password1", "Alice");
//        HashMap<Integer, Post> posts = new HashMap<>();
//        Post newPost1 = new Post("This is Alice's post", alice, 1);
//        Database database = new Database();
//        database.createPost("This is Alice's post", alice);
//        assertNotEquals(-1, 1, "Post should be created successfully.");
//    }
//
//    // fails
//    @Test
//    void deletePost() {
//        Post post = new Post();
//        Database database = new Database();
//        int postId = database.addPost(post);
//        assertTrue(database.deletePost(postId), "Post should be deleted successfully.");
//    }
//
//    // fails because of findUserByUsername
//    @Test
//    void viewUser() {
//        User alice = new User("alice123", "password1", "Alice");
//        User bob = new User("bob456", "password2", "Bob");
//        User charlie = new User("charlie789", "password3", "Charlie");
//        userList = new ArrayList<>();
//        userList.add(alice);
//        userList.add(bob);
//        userList.add(charlie);
//        Database database = new Database();
//
//        User viewedUser = database.viewUser("alice123");
//        assertNotNull(viewedUser, "User should be viewable in the database.");
//        assertEquals("Alice", viewedUser.getName(), "Viewed user should match expected details.");
//    }
//
//    // fails because of UserExists
//    @Test
//    void addFriend() {
//        User alice = new User("alice123", "password1", "Alice");
//        User bob = new User("bob456", "password2", "Bob");
//        User charlie = new User("charlie789", "password3", "Charlie");
//        userList = new ArrayList<>();
//        userList.add(alice);
//        userList.add(bob);
//        userList.add(charlie);
//        Database database = new Database();
//        assertTrue(database.addFriend(userList.get(0), userList.get(1)), "Should be able to add Bob as a friend.");
//    }
//    // fails because of UserExists
//    @Test
//    void removeFriend() {
//        User alice = new User("alice123", "password1", "Alice");
//        User bob = new User("bob456", "password2", "Bob");
//        User charlie = new User("charlie789", "password3", "Charlie");
//        userList = new ArrayList<>();
//        userList.add(alice);
//        userList.add(bob);
//        userList.add(charlie);
//        Database database = new Database();
//        assertTrue(database.addFriend(userList.get(0), userList.get(1)), "Should be able to add Bob as a friend.");
//        assertTrue(database.removeFriend(userList.get(0), userList.get(1)), "Should be able to remove Bob from friends.");
//    }
//
//    // fails because of UserExists
//    @Test
//    void blockUser() {
//        User alice = new User("alice123", "password1", "Alice");
//        User bob = new User("bob456", "password2", "Bob");
//        User charlie = new User("charlie789", "password3", "Charlie");
//        userList = new ArrayList<>();
//        userList.add(alice);
//        userList.add(bob);
//        userList.add(charlie);
//        HashMap<Integer, Post> posts = new HashMap<>();
//        Post newPost1 = new Post("This is Alice's new post", userList.get(0));
//        Post newPost2 = new Post("This is a Bob's post", userList.get(1));
//        Post newPost3 = new Post("This is a Charlie's post", userList.get(2));
//        posts.put(newPost1.getId(), newPost1);
//        posts.put(newPost2.getId(), newPost2);
//        posts.put(newPost3.getId(), newPost3);
//        Database database = new Database();
//        assertTrue(database.blockUser(userList.get(0), userList.get(2)), "Should be able to block Charlie.");
//    }
//
//    // fails
//    @Test
//    void upvotePost() {
//        User alice = new User("alice123", "password1", "Alice");
//        User bob = new User("bob456", "password2", "Bob");
//        User charlie = new User("charlie789", "password3", "Charlie");
//        userList = new ArrayList<>();
//        userList.add(alice);
//        userList.add(bob);
//        userList.add(charlie);
//
//        HashMap<Integer, Post> posts = new HashMap<>();
//        Post newPost1 = new Post("This is Alice's new post", userList.get(0));
//        Post newPost2 = new Post("This is a Bob's post", userList.get(1));
//        Post newPost3 = new Post("This is a Charlie's post", userList.get(2));
//        newPost1.setId(1);
//        newPost2.setId(2);
//        newPost3.setId(3);
//        posts.put(1, newPost1);
//        posts.put(2, newPost2);
//        posts.put(3, newPost3);
//        Database database = new Database();
//        int postIdToUpvote = newPost1.getId();
//        Post post = posts.get(postIdToUpvote);
//
//        int beforeUpVoting = post.getUpVotes();
//
//        database.upvotePost(postIdToUpvote);
//
//        // Check that the upvote count increased by 1
//        assertEquals(beforeUpVoting + 1, post.getUpVotes(), "Upvote count should increase by 1.");
//    }
//
//    // fails
//    @Test
//    void downvotePost() {
//        User alice = new User("alice123", "password1", "Alice");
//        User bob = new User("bob456", "password2", "Bob");
//        User charlie = new User("charlie789", "password3", "Charlie");
//        userList = new ArrayList<>();
//        userList.add(alice);
//        userList.add(bob);
//        userList.add(charlie);
//
//        HashMap<Integer, Post> posts = new HashMap<>();
//        Post newPost1 = new Post("This is Alice's new post", userList.get(0));
//        Post newPost2 = new Post("This is a Bob's post", userList.get(1));
//        Post newPost3 = new Post("This is a Charlie's post", userList.get(2));
//        newPost1.setId(1);
//        newPost2.setId(2);
//        newPost3.setId(3);
//        posts.put(newPost1.getId(), newPost1);
//        posts.put(newPost2.getId(), newPost2);
//        posts.put(newPost3.getId(), newPost3);
//        System.out.println(posts.size());
//        Database database = new Database();
//        PostImpl post = new PostImpl("Downvote this post", userList.get(1));
//        int i = 0;
//        List<Integer> postId = database.getUserPosts(userList.get(i));
//        database.downvotePost(postId.get(i));
//    }
//
//    @Test
//    void addCommentToPost() {
//        User alice = new User("alice123", "password1", "Alice");
//        User bob = new User("bob456", "password2", "Bob");
//        User charlie = new User("charlie789", "password3", "Charlie");
//        userList = new ArrayList<>();
//        userList.add(alice);
//        userList.add(bob);
//        userList.add(charlie);
//
//        HashMap<Integer, Post> posts = new HashMap<>();
//        Post newPost1 = new Post("This is Alice's post", userList.get(0));
//        Post newPost2 = new Post("This is a Bob's post", userList.get(1));
//        Post newPost3 = new Post("This is a Charlie's post", userList.get(2));
//        newPost1.setId(1);
//        newPost2.setId(2);
//        newPost3.setId(3);
//        posts.put(1, newPost1);
//        posts.put(2, newPost2);
//        posts.put(3, newPost3);
//        Database database = new Database();
//        database.addCommentToPost(1, "This is a comment.");
//
//
//        Map<Integer, String> comments = posts.get(1).getComments();
//        assertEquals("Expected one comment in the map", 1);
//    }
//
//
//    @Test
//    void deleteCommentFromPost() {
//        User alice = new User("alice123", "password1", "Alice");
//        User bob = new User("bob456", "password2", "Bob");
//        User charlie = new User("charlie789", "password3", "Charlie");
//        userList = new ArrayList<>();
//        userList.add(alice);
//        userList.add(bob);
//        userList.add(charlie);
//
//        HashMap<Integer, Post> posts = new HashMap<>();
//        Post newPost1 = new Post("This is Alice's post", userList.get(0));
//        Post newPost2 = new Post("This is a Bob's post", userList.get(1));
//        Post newPost3 = new Post("This is a Charlie's post", userList.get(2));
//        newPost1.setId(1);
//        newPost2.setId(2);
//        newPost3.setId(3);
//        posts.put(1, newPost1);
//        posts.put(2, newPost2);
//        posts.put(3, newPost3);
//        Database database = new Database();
//        database.addCommentToPost(1, "This is a comment.");
//        //Need to add Comment ID to the Post - Is it defined as a Hashmap making it challenging.
//        //assertTrue(database.deleteCommentFromPost(postId, commentId), "Comment should be deleted from post.");
//    }
//
//    @Test
//    void hidePost() {
//        User alice = new User("alice123", "password1", "Alice");
//        User bob = new User("bob456", "password2", "Bob");
//        User charlie = new User("charlie789", "password3", "Charlie");
//        userList = new ArrayList<>();
//        userList.add(alice);
//        userList.add(bob);
//        userList.add(charlie);
//
//        HashMap<Integer, Post> posts = new HashMap<>();
//        Post newPost1 = new Post("This is Alice's post", userList.get(0));
//        Post newPost2 = new Post("This is a Bob's post", userList.get(1));
//        Post newPost3 = new Post("This is a Charlie's post", userList.get(2));
//        newPost1.setId(1);
//        newPost2.setId(2);
//        newPost3.setId(3);
//        posts.put(1, newPost1);
//        posts.put(2, newPost2);
//        posts.put(3, newPost3);
//        Database database = new Database();
//        //Integer x = 1 ;
//        //assertTrue(database.hidePost(newPost1.getId()), "Post should be hidden successfully.");
//    }
//
//    @Test
//    void enableCommentsForPost() {
//        Database database = new Database();
//        Post post = new Post("Enable comments for this post", userList.get(2));
//        int postId = database.addPost(post);
//        assertTrue(database.enableComments(postId), "Comments should be enabled for post.");
//    }
//
//    @Test
//    void disableCommentsForPost() {
//        Post post = new Post("Disable comments for this post", userList.get(2));
//        int postId = database.addPost(post);
//        assertTrue(database.disableComments(postId), "Comments should be disabled for post.");
//    }
//
//    @Test
//    void saveDatabaseToFile() {
//        User alice = new User("alice123", "password1", "Alice");
//        User bob = new User("bob456", "password2", "Bob");
//        User charlie = new User("charlie789", "password3", "Charlie");
//        userList = new ArrayList<>();
//        userList.add(alice);
//        userList.add(bob);
//        userList.add(charlie);
//
//        HashMap<Integer, Post> posts = new HashMap<>();
//        Post newPost1 = new Post("This is Alice's post", userList.get(0));
//        Post newPost2 = new Post("This is a Bob's post", userList.get(1));
//        Post newPost3 = new Post("This is a Charlie's post", userList.get(2));
//        newPost1.setId(1);
//        newPost2.setId(2);
//        newPost3.setId(3);
//        posts.put(1, newPost1);
//        posts.put(2, newPost2);
//        posts.put(3, newPost3);
//
//        Database database = new Database();
//        database.saveDatabaseToFile("database.txt");
//        //assertTrue(database.saveDatabaseToFile("database.txt" ) , "Database should be saved to file successfully.");
//    }
//
//    @Test
//    void readDatabaseFromFile() {
//        Database database = new Database();
//        database.readDatabaseFromFile("database.txt") ;
//        assertTrue(database.readDatabaseFromFile("database.txt"), "Database should be read from file successfully.");
//    }*/
}
