package src;

import java.util.List;

public class test {

    public static void main(String[] args) {
        Database database = new Database() {};

        // Create some sample users
        User alice = new User("alice123", "password1", "Alice");
        User bob = new User("bob456", "password2", "Bob");
        User charlie = new User("charlie789", "password3", "Charlie");

        // Add users to the database
        database.addUser(alice);
        database.addUser(bob);
        database.addUser(charlie);

        // Validate user credentials
        database.validateCredentials("alice123", "password1");

        // Add friends between users
        database.addFriend(alice, bob);
        database.addFriend(alice, charlie);

        // Create posts by users
        int postId1 = database.createPost("Hello, this is Alice's first post!", alice);
        int postId2 = database.createPost("Bob here, enjoying this new app!", bob);
        int postId3 = database.createPost("Charlie posting a quick update!", charlie);

        // Add comments to a post
        database.addCommentToPost(postId1, "Nice post, Alice!");
        database.addCommentToPost(postId2, "Welcome, Bob!");

        // Upvote and downvote posts
        database.upvotePost(postId1);
        database.downvotePost(postId2);

        // View user information and their posts
        database.viewUser("alice123");
        database.viewUser("bob456");

        // Save the database to a file
        database.saveDatabaseToFile("database.txt");

        // Load the database from a file (for testing purposes)
        database.readDatabaseFromFile("database.txt");

        // Display user and post information after loading
        System.out.println("\nData loaded from file:");
        database.viewUser("alice123");
        database.viewUser("bob456");
    }
}
