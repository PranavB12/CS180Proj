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
            String[] parts = request.split(" ");
            try {
                switch (parts[0].toUpperCase()) {
                    case "ADD_USER":
                        return server.addUser(new User(parts[1], parts[2], parts[3])) ? "User added" : "Failed to add user";
                    case "REMOVE_USER":
                        return server.removeUser(new User(parts[1], parts[2], parts[3])) ? "User removed" : "Failed to remove user";
                    case "VALIDATE":
                        return server.validateCredentials(parts[1], parts[2]) ? "Valid credentials" : "Invalid credentials";
                    case "CREATE_POST":
                        return server.createPost(parts[1], server.database.findUserByUsername(parts[2]));
                    case "VIEW_USER":
                        User user = server.viewUser(parts[1]);
                        return user != null ? "User found: " + user.getUsername() : "User not found";
                    default:
                        return "Unknown command";
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
    public void addCommentToPost(String postId, String comment, User commentAuthor) {
        database.addCommentToPost(postId, comment, commentAuthor);
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
    public void writeDatabaseToFile(String filename) {database.writeDatabaseToFile(filename); }

    @Override
    public void readDatabaseFromFile(String filename) {
        database.readDatabaseFromFile(filename);
    }
}
