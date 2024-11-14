package src;

public class ServerMain {
    public static void main(String[] args) {
        Server server = new Server(5050);
        Thread serverThread = new Thread(server);
        serverThread.start();
    }
}
