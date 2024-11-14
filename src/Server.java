package src;
// Test
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server implements IServer, Runnable {
    private final Database database;
    private final int port;
    private final ExecutorService threadPool;

    public Server(int port) {
        this.port = port;
        this.database = new Database();
        this.threadPool = Executors.newCachedThreadPool();
    }

    @Override
    public void startServer() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server is listening on port " + port);
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New client connected");
                threadPool.execute(new ClientHandler(socket, database));
            }
        } catch (IOException e) {
            System.out.println("Server error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        startServer();
    }
}

class ClientHandler implements Runnable {
    private final Socket socket;
    private final Database database;

    public ClientHandler(Socket socket, Database database) {
        this.socket = socket;
        this.database = database;
    }

    @Override
    public void run() {
        try (BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter output = new PrintWriter(socket.getOutputStream(), true)) {

            String request;
            while ((request = input.readLine()) != null) {
                String[] parts = request.split(" ");
                String command = parts[0];
                String response = handleRequest(command, parts);
                output.println(response);
            }
        } catch (IOException e) {
            System.out.println("ClientHandler error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private String handleRequest(String command, String[] parts) {
        switch (command.toUpperCase()) {
            case "ADDUSER":
                if (parts.length == 4) {
                    boolean added = database.createAccount(parts[1], parts[2], parts[3]);
                    return added ? "User added" : "Failed to add user";
                }
                return "Invalid ADDUSER command format";

            case "REMOVEUSER":
                if (parts.length == 2) {
                    User user = database.viewUser(parts[1]);
                    boolean removed = user != null && database.removeUser(user);
                    return removed ? "User removed" : "User not found";
                }
                return "Invalid REMOVEUSER command format";

            case "CREATEPOST":
                if (parts.length >= 3) {
                    User user = database.viewUser(parts[1]);
                    String content = String.join(" ", Arrays.copyOfRange(parts, 2, parts.length));
                    String postId = user != null ? database.createPost(content, user) : null;
                    return postId != null ? "Post created with ID " + postId : "Failed to create post";
                }
                return "Invalid CREATEPOST command format";

            case "DELETEPOST":
                if (parts.length == 2) {
                    boolean deleted = database.deletePost(parts[1]);
                    return deleted ? "Post deleted" : "Post not found";
                }
                return "Invalid DELETEPOST command format";

            case "VIEWUSER":
                if (parts.length == 2) {
                    User user = database.viewUser(parts[1]);
                    return user != null ? user.toString() : "User not found";
                }
                return "Invalid VIEWUSER command format";

            default:
                return "Unknown command";
        }
    }
}
