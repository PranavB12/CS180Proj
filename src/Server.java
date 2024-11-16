package src;

import java.io.*;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server implements IDatabase, Runnable {

    private static final int PORT = 12345;
    private final Database database = new Database();
    private final ExecutorService threadPool = Executors.newCachedThreadPool();

    public static void main(String[] args) {
        Server server = new Server();
        Thread serverThread = new Thread(server);
        serverThread.start();
        System.out.println("Server is running on port " + PORT);
    }

    @Override
    public void run() {
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

        private String handleRequest(String request) {
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
                        String[] parts = arguments.split(" ", 3); // Limit split to 3 parts
                        if (parts.length < 3) {
                            return "Invalid ADD_USER format. Use: ADD_USER <username> <password> <name>";
                        }
                        return server.addUser(new User(parts[0], parts[1], parts[2])) ? "User added" : "Failed to add user";
                    }
                    case "REMOVE_USER": {
                        String[] parts = arguments.split(" ", 3); // Limit split to 3 parts
                        if (parts.length < 3) {
                            return "Invalid REMOVE_USER format. Use: REMOVE_USER <username> <password> <name>";
                        }
                        return server.removeUser(new User(parts[0], parts[1], parts[2])) ? "User removed" : "Failed to remove user";
                    }
                    case "VALIDATE": {
                        String[] parts = arguments.split(" ", 2); // Limit split to 2 parts
                        if (parts.length < 2) {
                            return "Invalid VALIDATE format. Use: VALIDATE <username> <password>";
                        }
                        return server.validateCredentials(parts[0], parts[1]) ? "Valid credentials" : "Invalid credentials";
                    }
                    case "CREATE_POST": {
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
                    case "ADD_COMMENT": {
                        String[] parts = arguments.split(" ", 3);
                        if (parts.length < 3) {
                            return "Invalid ADD_COMMENT format. Use: ADD_COMMENT <postId> <username> <comment>";
                        }
                        String postId = parts[0];
                        String username = parts[1];
                        String comment = parts[2];
                        User commentAuthor = server.database.findUserByUsername(username);
                        if (commentAuthor == null) {
                            return "Comment author not found.";
                        }
                        Post post = server.database.getPostById(postId);
                        if (post == null || post.isHidden()) {
                            return "Cannot add comment. Post not found or is hidden.";
                        }
                        server.addCommentToPost(postId, comment, commentAuthor);
                        return "Comment added.";
                    }
                    case "HIDE_POST": {
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
                    case "DELETE_POST": {
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
                    case "UPVOTE_POST": {
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

    // Methods from IDatabase implemented below
    @Override
    public boolean addUser(User user) {
        return database.addUser(user);
    }

    @Override
    public boolean removeUser(User user) {
        return database.removeUser(user);
    }

    @Override
    public boolean createAccount(String username, String password, String name) {
        return database.createAccount(username, password, name);
    }

    @Override
    public boolean validateCredentials(String username, String password) {
        return database.validateCredentials(username, password);
    }

    @Override
    public boolean userExists(String username) {
        return database.userExists(username);
    }

    @Override
    public String createPost(String content, User author) {
        return database.createPost(content, author);
    }

    @Override
    public boolean deletePost(String postId, User requestingUser) {
        return database.deletePost(postId, requestingUser);
    }

    @Override
    public User viewUser(String username) {
        return database.viewUser(username);
    }

    @Override
    public boolean addFriend(User user, User friend) {
        return database.addFriend(user, friend);
    }

    @Override
    public boolean removeFriend(User user, User friend) {
        return database.removeFriend(user, friend);
    }

    @Override
    public boolean blockUser(User user, User toBlock) {
        return database.blockUser(user, toBlock);
    }

    @Override
    public void upvotePost(String postId, User requestingUser) {
        database.upvotePost(postId, requestingUser);
    }

    @Override
    public void downvotePost(String postId, User requestingUser) {
        database.downvotePost(postId, requestingUser);
    }

    @Override
    public void addCommentToPost(String postId, String comment, User commentAuthor) {
        database.addCommentToPost(postId, comment, commentAuthor);
    }

    @Override
    public void deleteCommentFromPost(String postId, String commentId, User requestingUser) {
        database.deleteCommentFromPost(postId, commentId, requestingUser);
    }

    @Override
    public void hidePost(String postId, User requestingUser) {
        database.hidePost(postId, requestingUser);
    }

    @Override
    public void enableCommentsForPost(String postId, User requestingUser) {
        database.enableCommentsForPost(postId, requestingUser);
    }

    @Override
    public void disableCommentsForPost(String postId, User requestingUser) {
        database.disableCommentsForPost(postId, requestingUser);
    }

    @Override
    public void writeDatabaseToFile(String filename) {database.writeDatabaseToFile(filename); }

    @Override
    public void readDatabaseFromFile(String filename) {
        database.readDatabaseFromFile(filename);
    }
}
