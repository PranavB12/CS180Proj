package src;

public class ServerMain {
    public static void main(String[] args) {
        Server server = new Server(5050);
        server.start();
    }
}
