package src;

import java.io.*;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server implements IServer, Runnable {

    private static final int PORT = 12345;
    private static final String DATABASE_FILE = "/Users/dahlia/IdeaProjects/CS180Proj/src/database.txt";
    Database database = new Database();
    private final ExecutorService threadPool = Executors.newCachedThreadPool();

    public static void main(String[] args) {
        Server server = new Server();

        // Add a shutdown hook to save the database when the server stops
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Server shutting down. Saving database...");
            server.writeDatabaseToFile(DATABASE_FILE);
        }));
        System.out.println("Loading database from file...");
        server.readDatabaseFromFile(DATABASE_FILE);



        Thread serverThread = new Thread(server);
        serverThread.start();
        System.out.println("Server is running on port " + PORT);
    }

    @Override
    public void run() {
        // Load the database state from a file


        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket.getInetAddress());
                threadPool.execute(new ClientHandler(clientSocket, this));
            }
        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
        }
    }

    private static class ClientHandler implements Runnable {
        private final Socket clientSocket;
        private final Server server;

        public ClientHandler(Socket clientSocket, Server server) {
            this.clientSocket = clientSocket;
            this.server = server;
        }

        @Override
        public void run() {
            try (
                    BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)
            ) {
                String request;
                while ((request = in.readLine()) != null) {
                    System.out.println("Received: " + request);
                    String response = handleRequest(request);
                    out.println(response);
                }
            } catch (IOException e) {
                System.err.println("Client handler error: " + e.getMessage());
            }
        }

        public String handleRequest(String request) {
            try {
                // Extract the command
                int firstSpaceIndex = request.indexOf(" ");
                if (firstSpaceIndex == -1) {
                    return "Invalid command format.";
                }
                String command = request.substring(0, firstSpaceIndex).toUpperCase();
                String arguments = request.substring(firstSpaceIndex + 1);

                switch (command) {
                    case "ADD_USER": {
                        // Input: Provide arguments in the format: <username> <password> <name>
                        // Expected Output:
                        //   - On success: "User added".
                        //   - On failure (e.g., duplicate username): "Failed to add user".
                        //   - On incorrect format: "Invalid ADD_USER format. Use: ADD_USER <username> <password>
                        String[] parts = arguments.split(" ", 4);
                        if (parts.length < 4) {
                            return "Invalid ADD_USER format. Use: ADD_USER <username> <password> <name>";
                        }
                        return server.addUser(new User(parts[0], parts[1], parts[2], parts[3])) ? "User added" : "Failed to add user";
                    }
                    case "REMOVE_USER": {
                        // Input: Provide arguments in the format: <username> <password> <name>
                        // Expected Output:
                        //   - On success: "User removed".
                        //   - On failure (e.g., user does not exist): "Failed to remove user".
                        //   - On incorrect format: "Invalid REMOVE_USER format. Use: REMOVE_USER <username> <password> <name>".
                        String[] parts = arguments.split(" ", 3);
                        if (parts.length < 3) {
                            return "Invalid REMOVE_USER format. Use: REMOVE_USER <username> <password> <name>";
                        }
                        return server.removeUser(new User(parts[0], parts[1], parts[2])) ? "User removed" : "Failed to remove user";
                    }
                    case "VALIDATE": {
                        // Input: Provide arguments in the format: <username> <password>
                        // Expected Output:
                        //   - On success: "Valid credentials".
                        //   - On failure: "Invalid credentials".
                        //   - On incorrect format: "Invalid VALIDATE format. Use: VALIDATE <username> <password>".
                        String[] parts = arguments.split(" ", 2);
                        if (parts.length < 2) {
                            return "Invalid VALIDATE format. Use: VALIDATE <username> <password>";
                        }
                        return server.validateCredentials(parts[0], parts[1]) ? "Valid credentials" : "Invalid credentials";
                    }
                    case "ADD_FRIEND": {
                        // Input: Provide arguments in the format: <username> <friendUsername>
                        // Expected Output:
                        //   - On success: "Friend added".
                        //   - On failure (e.g., user or friend not found): "Failed to add friend".
                        //   - On incorrect format: "Invalid ADD_FRIEND format. Use: ADD_FRIEND <username> <friendUsername>".
                        String[] parts = arguments.split(" ", 2);
                        if (parts.length < 2) {
                            return "Invalid ADD_FRIEND format. Use: ADD_FRIEND <username> <friendUsername>";
                        }
                        User user = server.database.findUserByUsername(parts[0]);
                        User friend = server.database.findUserByUsername(parts[1]);
                        if (user == null || friend == null) {
                            return "User or friend not found.";
                        }
                        return server.addFriend(user, friend) ? "Friend added" : "Failed to add friend";
                    }
                    case "REMOVE_FRIEND": {
                        // Input: Provide arguments in the format: <username> <friendUsername>
                        // Expected Output:
                        //   - On success: "Friend removed".
                        //   - On failure (e.g., user or friend not found): "Failed to remove friend".
                        //   - On incorrect format: "Invalid REMOVE_FRIEND format. Use: REMOVE_FRIEND <username> <friendUsername>".
                        String[] parts = arguments.split(" ", 2);
                        if (parts.length < 2) {
                            return "Invalid REMOVE_FRIEND format. Use: REMOVE_FRIEND <username> <friendUsername>";
                        }
                        User user = server.database.findUserByUsername(parts[0]);
                        User friend = server.database.findUserByUsername(parts[1]);
                        if (user == null || friend == null) {
                            return "User or friend not found.";
                        }
                        return server.removeFriend(user, friend) ? "Friend removed" : "Failed to remove friend";
                    }
                    case "BLOCK_USER": {
                        // Input: Provide arguments in the format: <username> <blockedUsername>
                        // Expected Output:
                        //   - On success: "User blocked".
                        //   - On failure (e.g., user or blocked user not found): "Failed to block user".
                        //   - On incorrect format: "Invalid BLOCK_USER format. Use: BLOCK_USER <username> <blockedUsername>".
                        String[] parts = arguments.split(" ", 2);
                        if (parts.length < 2) {
                            return "Invalid BLOCK_USER format. Use: BLOCK_USER <username> <blockedUsername>";
                        }
                        User user = server.database.findUserByUsername(parts[0]);
                        User toBlock = server.database.findUserByUsername(parts[1]);
                        if (user == null || toBlock == null) {
                            return "User or blocked user not found.";
                        }
                        return server.blockUser(user, toBlock) ? "User blocked" : "Failed to block user";
                    }
                    case "CREATE_POST": {
                        // Input: Provide arguments in the format: <author> <content>
                        // Expected Output:
                        //   - On success: "Post created with ID: <postId>".
                        //   - On failure (e.g., author not found): "Failed to create post".
                        //   - On incorrect format: "Invalid CREATE_POST format. Use: CREATE_POST <author> <content>".
                        int contentStartIndex = arguments.indexOf(" ");
                        if (contentStartIndex == -1) {
                            return "Invalid CREATE_POST format. Use: CREATE_POST <author> <content>";
                        }
                        String authorUsername = arguments.substring(0, contentStartIndex).trim();
                        String content = arguments.substring(contentStartIndex + 1).trim();

                        User author = server.database.findUserByUsername(authorUsername);
                        if (author == null) {
                            return "Author not found: " + authorUsername;
                        }
                        String postId = server.createPost(content, author);
                        return postId != null ? "Post created with ID: " + postId : "Failed to create post";
                    }
                    case "DELETE_POST": {
                        // Input: Provide arguments in the format: <postId> <username>
                        // Expected Output:
                        //   - On success: "Post deleted".
                        //   - On failure (e.g., post or user not found): "Failed to delete post".
                        //   - On incorrect format: "Invalid DELETE_POST format. Use: DELETE_POST <postId> <username>".
                        String[] parts = arguments.split(" ", 2);
                        if (parts.length < 2) {
                            return "Invalid DELETE_POST format. Use: DELETE_POST <postId> <username>";
                        }
                        Post post = server.database.getPostById(parts[0]);
                        User requestingUser = server.database.findUserByUsername(parts[1]);
                        if (post == null || requestingUser == null) {
                            return "Post or requesting user not found.";
                        }
                        return server.deletePost(parts[0], requestingUser) ? "Post deleted" : "Failed to delete post";
                    }
                    case "HIDE_POST": {
                        // Input: Provide arguments in the format: <postId> <username>
                        // Expected Output:
                        //   - On success: "Post hidden."
                        //   - On failure (e.g., post not found or user is not the author): "Only the creator of the post can hide it."
                        //   - On incorrect format: "Invalid HIDE_POST format. Use: HIDE_POST <postId> <username>".
                        String[] parts = arguments.split(" ", 2);
                        if (parts.length < 2) {
                            return "Invalid HIDE_POST format. Use: HIDE_POST <postId> <username>";
                        }
                        String postId = parts[0];
                        String username = parts[1];
                        User requestingUser = server.database.findUserByUsername(username);
                        if (requestingUser == null) {
                            return "Requesting user not found.";
                        }
                        Post post = server.database.getPostById(postId);
                        if (post == null) {
                            return "Post not found.";
                        }
                        if (!post.getAuthor().equals(requestingUser)) {
                            return "Only the creator of the post can hide it.";
                        }
                        server.hidePost(postId, requestingUser);
                        return "Post hidden.";
                    }
                    case "UNHIDE_POST": {
                        // Input: Provide arguments in the format: <postId> <username>
                        // Expected Output:
                        //   - On success: "Post with ID <postId> has been unhidden."
                        //   - On failure (e.g., post not found or user is not the author): "Only the author of the post can unhide it."
                        //   - On incorrect format: "Invalid UNHIDE_POST format. Use: UNHIDE_POST <postId> <username>".
                        String[] parts = arguments.split(" ", 2);
                        if (parts.length < 2) {
                            return "Invalid UNHIDE_POST format. Use: UNHIDE_POST <postId> <username>";
                        }

                        String postId = parts[0];
                        String username = parts[1];

                        // Validate user
                        User requestingUser = server.database.findUserByUsername(username);
                        if (requestingUser == null) {
                            return "User not found.";
                        }

                        // Fetch the post
                        Post post = server.database.getPostById(postId);
                        if (post == null) {
                            return "Post not found.";
                        }

                        // Check if the requesting user is the author of the post
                        if (!post.getAuthor().equals(requestingUser)) {
                            return "Only the author of the post can unhide it.";
                        }

                        // Unhide the post
                        //server.unhidePost(postId, requestingUser);
                        return "Post with ID " + postId + " has been unhidden.";
                    }
                    case "ENABLE_COMMENTS": {
                        // Input: Provide arguments in the format: <postId> <username>
                        // Expected Output:
                        //   - On success: "Comments enabled for post."
                        //   - On failure (e.g., user not found): "User not found."
                        //   - On incorrect format: "Invalid ENABLE_COMMENTS format. Use: ENABLE_COMMENTS <postId> <username>".
                        String[] parts = arguments.split(" ", 2);
                        if (parts.length < 2) {
                            return "Invalid ENABLE_COMMENTS format. Use: ENABLE_COMMENTS <postId> <username>";
                        }
                        User user = server.database.findUserByUsername(parts[1]);
                        if (user == null) {
                            return "User not found.";
                        }
                        server.enableCommentsForPost(parts[0], user);
                        return "Comments enabled for post";
                    }
                    case "ADD_COMMENT": {
                        // Input: Provide arguments in the format: <postId> <username> <comment>
                        // Expected Output:
                        //   - On success: "Comment added with ID: <commentId>".
                        //   - On failure (e.g., post not found or hidden, or user not found): "Cannot add comment. Post not found or is hidden."
                        //   - On incorrect format: "Invalid ADD_COMMENT format. Use: ADD_COMMENT <postId> <username> <comment>".
                        String[] parts = arguments.split(" ", 3);
                        if (parts.length < 3) {
                            return "Invalid ADD_COMMENT format. Use: ADD_COMMENT <postId> <username> <comment>";
                        }
                        String postId = parts[0];
                        String username = parts[1];
                        String comment = parts[2];

                        // Find the user and post
                        User commentAuthor = server.database.findUserByUsername(username);
                        if (commentAuthor == null) {
                            return "Comment author not found.";
                        }
                        Post post = server.database.getPostById(postId);
                        if (post == null || post.isHidden()) {
                            return "Cannot add comment. Post not found or is hidden.";
                        }

                        // Add comment and return generated comment ID
                        String commentId = server.addCommentToPost(postId, comment, commentAuthor);
                        if (commentId != null) {
                            return "Comment added with ID: " + commentId;
                        }
                        return "Failed to add comment.";
                    }
                    case "DELETE_COMMENT": {
                        // Input: Provide arguments in the format: <postId> <commentId> <username>
                        // Expected Output:
                        //   - On success: "Comment with ID <commentId> successfully deleted".
                        //   - On failure (e.g., post or comment not found, or user unauthorized): "Failed to delete comment."
                        //   - On incorrect format: "Invalid DELETE_COMMENT format. Use: DELETE_COMMENT <postId> <commentId> <username>".
                        String[] parts = arguments.split(" ", 3);
                        if (parts.length < 3) {
                            return "Invalid DELETE_COMMENT format. Use: DELETE_COMMENT <postId> <commentId> <username>";
                        }
                        String postId = parts[0];
                        String commentId = parts[1];
                        String username = parts[2];

                        // Validate user
                        User user = server.database.findUserByUsername(username);
                        if (user == null) {
                            return "User not found.";
                        }

                        Post post = server.database.getPostById(postId);
                        if (post == null) {
                            return "Post not found.";
                        }

                        // Delete the comment
                        server.deleteCommentFromPost(postId, commentId, user);
                        return "Comment with ID " + commentId + " successfully deleted";
                    }
                    case "DISABLE_COMMENTS": {
                        // Input: Provide arguments in the format: <postId> <username>
                        // Expected Output:
                        //   - On success: "Comments disabled for post."
                        //   - On failure (e.g., user not found): "User not found."
                        //   - On incorrect format: "Invalid DISABLE_COMMENTS format. Use: DISABLE_COMMENTS <postId> <username>".
                        String[] parts = arguments.split(" ", 2);
                        if (parts.length < 2) {
                            return "Invalid DISABLE_COMMENTS format. Use: DISABLE_COMMENTS <postId> <username>";
                        }
                        User user = server.database.findUserByUsername(parts[1]);
                        if (user == null) {
                            return "User not found.";
                        }
                        server.disableCommentsForPost(parts[0], user);
                        return "Comments disabled for post";
                    }
                    case "UPVOTE_POST": {
                        // Input: Provide arguments in the format: <postId> <username>
                        // Expected Output:
                        //   - On success: "Post upvoted."
                        //   - On failure (e.g., post not found, user not found, or post is hidden): "Cannot upvote. Post not found or is hidden."
                        //   - On incorrect format: "Invalid UPVOTE_POST format. Use: UPVOTE_POST <postId> <username>".
                        String[] parts = arguments.split(" ", 2);
                        if (parts.length < 2) {
                            return "Invalid UPVOTE_POST format. Use: UPVOTE_POST <postId> <username>";
                        }
                        Post post = server.database.getPostById(parts[0]);
                        User requestingUser = server.database.findUserByUsername(parts[1]);
                        if (post == null || requestingUser == null || post.isHidden()) {
                            return "Cannot upvote. Post not found or is hidden.";
                        }
                        server.upvotePost(parts[0], requestingUser);
                        return "Post upvoted.";
                    }
                    case "DOWNVOTE_POST": {
                        // Input: Provide arguments in the format: <postId> <username>
                        // Expected Output:
                        //   - On success: "Post downvoted."
                        //   - On failure (e.g., post not found, user not found, or post
                        String[] parts = arguments.split(" ", 2);
                        if (parts.length < 2) {
                            return "Invalid DOWNVOTE_POST format. Use: DOWNVOTE_POST <postId> <username>";
                        }
                        Post post = server.database.getPostById(parts[0]);
                        User requestingUser = server.database.findUserByUsername(parts[1]);
                        if (post == null || requestingUser == null || post.isHidden()) {
                            return "Cannot downvote. Post not found or is hidden.";
                        }
                        server.downvotePost(parts[0], requestingUser);
                        return "Post downvoted.";
                    }
                    default:
                        return "Unknown command: " + command;
                }
            } catch (Exception e) {
                return "Error processing request: " + e.getMessage();
            }
        }
    }

    // Database persistence methods
    public void writeDatabaseToFile(String filename) {
        try {
            database.writeDatabaseToFile(filename);
        } catch (Exception e) {
            System.err.println("Error saving database to file: " + e.getMessage());
        }
    }

    public void readDatabaseFromFile(String filename) {
        try {
            database.readDatabaseFromFile(filename);
        } catch (Exception e) {
            System.err.println("Error loading database from file: " + e.getMessage());
        }
    }

    // Delegate database operations
    public boolean addUser(User user) {
        return database.addUser(user);
    }

    public boolean removeUser(User user) {
        return database.removeUser(user);
    }

    public boolean validateCredentials(String username, String password) {
        return database.validateCredentials(username, password);
    }

    public boolean addFriend(User user, User friend) {
        return database.addFriend(user, friend);
    }

    public boolean removeFriend(User user, User friend) {
        return database.removeFriend(user, friend);
    }

    public boolean blockUser(User user, User toBlock) {
        return database.blockUser(user, toBlock);
    }

    public String createPost(String content, User author) {
        return database.createPost(content, author);
    }

    public boolean deletePost(String postId, User requestingUser) {
        return database.deletePost(postId, requestingUser);
    }

    public void hidePost(String postId, User requestingUser) {
        database.hidePost(postId, requestingUser);
    }

    public void unhidePost(String postId, User requestingUser) {database.unhidePost(postId, requestingUser);}

    public void enableCommentsForPost(String postId, User user) {
        database.enableCommentsForPost(postId, user);
    }

    public void disableCommentsForPost(String postId, User user) {
        database.disableCommentsForPost(postId, user);
    }

    public String addCommentToPost(String postId, String comment, User commentAuthor) {
        return database.addCommentToPost(postId, comment, commentAuthor);
    }

    public void deleteCommentFromPost(String postId, String commentId, User user) {
        try {
            database.deleteCommentFromPost(postId, commentId, user);
        } catch (Exception e) {
            System.err.println("Error deleting comment: " + e.getMessage());
        }
    }

    public void upvotePost(String postId, User requestingUser) {
        database.upvotePost(postId, requestingUser);
    }

    public void downvotePost(String postId, User requestingUser) {
        database.downvotePost(postId, requestingUser);
    }

}
