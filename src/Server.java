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
                //
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
                        System.out.println(content);
                        System.out.println(authorUsername);
                        return postId != null ? "Post created with ID: " + postId : "Failed to create post";
                    }
                    case "VIEW_USER": {
                        User user = server.viewUser(arguments);
                        return user != null ? "User found: " + user.getUsername() : "User not found";
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
    public boolean deletePost(String postId) {
        return database.deletePost(postId);
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
    public void upvotePost(String postId) {
        database.upvotePost(postId);
    }

    @Override
    public void downvotePost(String postId) {
        database.downvotePost(postId);
    }

    @Override
    public void addCommentToPost(String postId, String comment) {
        database.addCommentToPost(postId, comment);
    }

    @Override
    public void deleteCommentFromPost(String postId, String commentId) {
        database.deleteCommentFromPost(postId, commentId);
    }

    @Override
    public void hidePost(String postId) {
        database.hidePost(postId);
    }

    @Override
    public void enableCommentsForPost(String postId) {
        database.enableCommentsForPost(postId);
    }

    @Override
    public void disableCommentsForPost(String postId) {
        database.disableCommentsForPost(postId);
    }

    @Override
    public void saveDatabaseToFile(String filename) {
        database.saveDatabaseToFile(filename);
    }

    @Override
    public void readDatabaseFromFile(String filename) {
        database.readDatabaseFromFile(filename);
    }
}
