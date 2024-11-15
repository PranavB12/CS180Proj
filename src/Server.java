package src;

import java.io.*;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server implements Runnable {

    private static final int PORT = 12345;
    private final Database database;
    private final ExecutorService threadPool;

    public Server(Database database) {
        this.database = database;
        this.threadPool = Executors.newCachedThreadPool();
    }

    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server is running on port " + PORT);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket.getInetAddress());
                threadPool.execute(new ClientHandler(clientSocket, database));
            }
        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
        }
    }

    private static class ClientHandler implements Runnable {
        private final Socket clientSocket;
        private final Database database;

        public ClientHandler(Socket clientSocket, Database database) {
            this.clientSocket = clientSocket;
            this.database = database;
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
                    // Echo back to the client
                    String response = handleRequest(request);
                    out.println(response);
                }
            } catch (IOException e) {
                System.err.println("Client error: " + e.getMessage());
            }
        }

        private String handleRequest(String request) {
            // Simplified request handling logic
            String[] parts = request.split(" ");
            switch (parts[0]) {
                case "ADD_USER":
                    return database.addUser(new User(parts[1], parts[2], parts[3])) ? "User added" : "Failed to add user";
                case "VALIDATE":
                    return database.validateCredentials(parts[1], parts[2]) ? "Valid credentials" : "Invalid credentials";
                case "CREATE_POST":
                    return database.createPost(parts[1], database.findUserByUsername(parts[2]));
                default:
                    return "Unknown command";
            }
        }
    }
}
