package src;

import java.io.*;
import java.net.*;

/**
 * Group Project - CS18000 Gold
 *
 * Client class
 *
 * @author Pranav Bansal, Vivaan Malhotra, Rishi Rao, Mike Lee, Vaishnavi Sharma, lab sec 37
 *
 * @version November 19, 2024
 *
 */

public class Client implements IClient {

    private final String host;
    private final int port;

    public Client(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void start() {
        try (Socket socket = new Socket(host, port);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in))) {

            System.out.println("Connected to server at " + host + ":" + port);
            System.out.println("Type commands to interact with the server (e.g., ADD_USER <username> <password> " +
                    "<name>):");

            String input;
            while ((input = userInput.readLine()) != null) {
                out.println(input); // Send user input to the server
                String response = in.readLine(); // Receive response from the server
                System.out.println("Server: " + response);
            }

        } catch (IOException e) {
            System.err.println("Client error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        String host = "localhost"; // Server host
        int port = 12345;          // Server port

        Client client = new Client(host, port);
        client.start();
    }
}
