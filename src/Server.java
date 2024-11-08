package src;

// READ ME!!! --> THIS IS JUST A BASIC OUTLINE

// PRANAV'S UPDATE WILL CHANGE MAJORITY OF THE CODE HERE

// Basic foundation is setup

public class Server implements Runnable {
    private ServerSocket serverSocket;
    private Database database; // Reference to the database
    private List<ClientHandler> clients; // List of connected clients

    public Server(int port) {
        serverSocket = new ServerSocket(port);
        clients = new ArrayList<>();
        database = new Database(); // or inject via constructor
    }

    @Override
    public void run() {
        while (true) {
            Socket clientSocket = serverSocket.accept();
            ClientHandler clientHandler = new ClientHandler(clientSocket, database);
            clients.add(clientHandler);
            new Thread(clientHandler).start(); // Handle each client in a separate thread
        }
    }

    // Method to broadcast messages to all clients
    public synchronized void broadcast(String message) {
        for (ClientHandler client : clients) {
            client.sendMessage(message);
        }
    }
}
