package src;

import java.util.*;

public class Server {
    private Database database;

    public Server() {
        // Initialize the database and load data
        database = new Database();
        database.readDatabaseFromFile("database.txt");
        System.out.println("Server is running and ready to accept client requests...");
    }

    // Handle user login
    public boolean loginUser(String username, String password) {
        return database.validateCredentials(username, password);
    }

    // Handle user registration
    public boolean registerUser(String username, String password) {
        User newUser = new User(username, password, username, "New User");
        return database.addUser(newUser);
    }

    // Handle post creation
    public String createPost(String content, String username) {
        User author = database.getUserByUsername(username);


        if (author != null) {
            System.out.println(username);
            return database.createPost(content, author);
        } else {
            System.out.println(username+database.getUsers().size());
        }

        return null;
    }

    // Handle upvoting a post
    public boolean upvotePost(String postId, String username) {
        User user = database.getUserByUsername(username);
        if (user != null) {
            database.upvotePost(postId, user);
            return true; // Successful upvote
        }
        return false; // Upvote failed
    }

    // Handle downvoting a post
    public boolean downvotePost(String postId, String username) {
        User user = database.getUserByUsername(username);
        if (user != null) {
            database.downvotePost(postId, user);
            return true; // Successful downvote
        }
        return false; // Downvote failed
    }

    // Fetch posts for a user
    public java.util.List<String> getPostsForUser(String username) {
        return database.displayPosts(username);
    }

    // Handle comment creation
    public String addCommentToPost(String postId, String content, String username) {
        User commenter = database.getUserByUsername(username);
        if (commenter != null) {
            return database.addCommentToPost(postId, content, commenter);
        }
        return null;
    }

    // Save data to file
    public void saveData() {
        database.writeDatabaseToFile("database.txt");
    }
    public void readData() {
        database.readDatabaseFromFile("database.txt");
    }

    // Main method to start the server
    public static void main(String[] args) {
        Server server = new Server();


        // Add a shutdown hook to ensure data is saved when the server stops
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Saving data and shutting down server...");
            //server.saveData();
        }));

        // Keep the server running (simulate a long-running service)
        while (true) {
            try {
                Thread.sleep(1000); // Prevent CPU overuse
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}
