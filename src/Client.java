package src;

import java.io.*;
import java.net.*;

public class Client {

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
            String input;
            while ((input = userInput.readLine()) != null) {
                out.println(input);
                String response = in.readLine();
                System.out.println("Server: " + response);
            }

        } catch (IOException e) {
            System.err.println("Client error: " + e.getMessage());
        }
    }
}
