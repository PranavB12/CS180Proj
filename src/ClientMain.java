package src;

public class ClientMain {
    public static void main(String[] args) {
        Client client = new Client("localhost", 5050);
        client.start();
    }
}
