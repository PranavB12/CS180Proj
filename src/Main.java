package src;

public class Main {

    public static void main(String[] args) {
        Database database = new Database();

        // Start the server in a separate thread
        Thread serverThread = new Thread(new Server(database));
        serverThread.start();

        // Simulate two clients
        Client client1 = new Client("localhost", 12345);
        Client client2 = new Client("localhost", 12345);

        new Thread(client1::start).start();
        new Thread(client2::start).start();
    }
}
