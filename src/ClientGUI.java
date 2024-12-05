package src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;

public class ClientGUI extends JFrame {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 12345;
    private String currentUser;

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    // GUI components
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPanel feedPanel; // Updated feed panel for dynamic layout
    private JTextArea createPostArea;
    private JButton loginButton;
    private JButton registerButton;
    private JButton createPostButton;
    private JButton logoutButton; // New logout button

    public ClientGUI() {
        try {
            // Establish connection to the server
            socket = new Socket(SERVER_HOST, SERVER_PORT);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Unable to connect to the server. Please start the server first.", "Connection Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        setTitle("News Feed App - Client");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window

        // Panel for login/registration
        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new GridLayout(3, 2));
        loginPanel.add(new JLabel("Username:"));
        usernameField = new JTextField();
        loginPanel.add(usernameField);
        loginPanel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        loginPanel.add(passwordField);

        loginButton = new JButton("Login");
        loginPanel.add(loginButton);
        registerButton = new JButton("Register");
        loginPanel.add(registerButton);

        // Panel for newsfeed and user actions (initially hidden)
        feedPanel = new JPanel();
        feedPanel.setLayout(new BorderLayout());

        JPanel feedContentPanel = new JPanel();
        feedContentPanel.setLayout(new BoxLayout(feedContentPanel, BoxLayout.Y_AXIS)); // Dynamic layout for posts
        JScrollPane feedScroll = new JScrollPane(feedContentPanel);
        feedPanel.add(feedScroll, BorderLayout.CENTER);

        // Create post section
        JPanel createPostPanel = new JPanel();
        createPostPanel.setLayout(new BorderLayout());
        createPostArea = new JTextArea(3, 20);
        createPostPanel.add(new JLabel("Create Post:"), BorderLayout.NORTH);
        createPostPanel.add(new JScrollPane(createPostArea), BorderLayout.CENTER);
        createPostButton = new JButton("Post");
        createPostPanel.add(createPostButton, BorderLayout.SOUTH);

        // Add logout button
        logoutButton = new JButton("Logout");
        createPostPanel.add(logoutButton, BorderLayout.EAST);

        feedPanel.add(createPostPanel, BorderLayout.SOUTH);

        // Add panels to frame
        getContentPane().add(loginPanel, BorderLayout.NORTH);
        getContentPane().add(feedPanel, BorderLayout.CENTER);

        // Set up event listeners
        loginButton.addActionListener(new LoginButtonListener());
        registerButton.addActionListener(new RegisterButtonListener());
        createPostButton.addActionListener(new CreatePostButtonListener());
        logoutButton.addActionListener(new LogoutButtonListener());

        // Initially, show only the login panel, hide the feed panel
        feedPanel.setVisible(false);

        // Add a WindowListener to close the socket and resources before exiting
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    socket.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                System.exit(0);
            }
        });
    }

    // Sends a request to the server and returns the response
    private synchronized String handleRequest(String request) {
        try {
            out.println(request);
            return in.readLine();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error communicating with the server.", "Communication Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    // ActionListener for login
    private class LoginButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Get the username and password input from the user
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            // Send request to server for validation
            String response = handleRequest("VALIDATE " + username + " " + password);

            // Check if the server responds with "Valid credentials"
            if ("Valid credentials".equals(response)) {
                // Set the current user and display the feed panel
                currentUser = username;

                // Call showNewsFeed() to display the feed after a successful login
                showNewsFeed();

                // Make the feed panel visible
                feedPanel.setVisible(true);

                // Hide the login UI components after successful login
                toggleLoginUI(false);
            } else {
                // Show an error message if credentials are invalid
                JOptionPane.showMessageDialog(ClientGUI.this, "Invalid credentials.");
            }
        }
    }


    // ActionListener for registration
    private class RegisterButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            String response = handleRequest("ADD_USER " + username + " " + password + " Some Name");

            if ("User added".equals(response)) {
                JOptionPane.showMessageDialog(ClientGUI.this, "User registered successfully.");
            } else {
                JOptionPane.showMessageDialog(ClientGUI.this, "Failed to register user.");
            }
        }
    }

    // ActionListener for creating a post
    private class CreatePostButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String postContent = createPostArea.getText();
            if (!postContent.isEmpty()) {
                String response = handleRequest("CREATE_POST " + currentUser + " " + postContent);
                if (response.startsWith("Post created with ID")) {
                    JOptionPane.showMessageDialog(ClientGUI.this, response);
                    showNewsFeed(); // Refresh the feed
                } else {
                    JOptionPane.showMessageDialog(ClientGUI.this, "Failed to create post.");
                }
            } else {
                JOptionPane.showMessageDialog(ClientGUI.this, "Post content cannot be empty.");
            }
        }
    }


    // ActionListener for logout
    private class LogoutButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            currentUser = null;
            toggleLoginUI(true);
            feedPanel.setVisible(false);
        }
    }
    private void showNewsFeed() {
        JPanel feedContentPanel = (JPanel) ((JScrollPane) feedPanel.getComponent(0)).getViewport().getView();
        feedContentPanel.removeAll();

        String response = handleRequest("GET_FEED " + currentUser);

        if (response != null && !response.isEmpty()) {
            String[] lin = response.split("12345");
            ArrayList<String> lines = new ArrayList<>();
            Collections.addAll(lines, lin);

            int index = 0;
            while (index < lines.size()) {
                String line = lines.get(index);

                if (line.equals("---------------------------------------------------------------------------------------------------------")) {
                    index++;
                    continue;
                }

                if (line.startsWith("Posted by:")) {
                    feedContentPanel.add(new JLabel(line));
                    index++;

                    String content = lines.get(index);
                    feedContentPanel.add(new JLabel(content));
                    index++;

                    String upvotes = lines.get(index);
                    feedContentPanel.add(new JLabel(upvotes));
                    index++;

                    String downvotes = lines.get(index);
                    feedContentPanel.add(new JLabel(downvotes));
                    index++;

                    String postId = lines.get(index);
                    index++;

                    JButton upvoteButton = new JButton("Upvote");
                    upvoteButton.addActionListener(e -> {
                        try {
                            out.println("UPVOTE_POST " + postId + " " + currentUser);
                            String voteResponse = in.readLine();
                            JOptionPane.showMessageDialog(ClientGUI.this, voteResponse);
                            showNewsFeed();
                        } catch (IOException ex) {
                            JOptionPane.showMessageDialog(ClientGUI.this, "Error communicating with the server.", "Communication Error", JOptionPane.ERROR_MESSAGE);
                        }
                    });
                    feedContentPanel.add(upvoteButton);

                    JButton downvoteButton = new JButton("Downvote");
                    downvoteButton.addActionListener(e -> {
                        try {
                            out.println("DOWNVOTE_POST " + postId + " " + currentUser);
                            String voteResponse = in.readLine();
                            JOptionPane.showMessageDialog(ClientGUI.this, voteResponse);
                            showNewsFeed();
                        } catch (IOException ex) {
                            JOptionPane.showMessageDialog(ClientGUI.this, "Error communicating with the server.", "Communication Error", JOptionPane.ERROR_MESSAGE);
                        }
                    });
                    feedContentPanel.add(downvoteButton);

                    boolean hasComments = false;
                    while (index < lines.size() && lines.get(index).equals("     Comments: ")) {
                        hasComments = true;
                        index++;
                        while (index < lines.size() && !lines.get(index).equals("---")) {
                            String commentContent = lines.get(index).trim();
                            index++;
                            int upVotes = Integer.parseInt(lines.get(index).trim());
                            index++;
                            int downVotes = Integer.parseInt(lines.get(index).trim());
                            index++;
                            String commenter = lines.get(index);
                            index++;

                            feedContentPanel.add(new JLabel("   Commented by: " + commenter));
                            feedContentPanel.add(new JLabel("     " + commentContent));
                            feedContentPanel.add(new JLabel("     Upvotes: " + upVotes));
                            feedContentPanel.add(new JLabel("     DownVotes: " + downVotes));

                            String commentId = lines.get(index);
                            index++;

                            JButton commentUpvoteButton = new JButton("Upvote Comment");
                            commentUpvoteButton.addActionListener(e -> {
                                try {
                                    out.println("UPVOTE_COMMENT " + commentId + " " + currentUser);
                                    String voteResponse = in.readLine();
                                    JOptionPane.showMessageDialog(ClientGUI.this, voteResponse);
                                    showNewsFeed();
                                } catch (IOException ex) {
                                    JOptionPane.showMessageDialog(ClientGUI.this, "Error communicating with the server.", "Communication Error", JOptionPane.ERROR_MESSAGE);
                                }
                            });
                            feedContentPanel.add(commentUpvoteButton);

                            JButton commentDownvoteButton = new JButton("Downvote Comment");
                            commentDownvoteButton.addActionListener(e -> {
                                try {
                                    out.println("DOWNVOTE_COMMENT " + commentId + " " + currentUser);
                                    String voteResponse = in.readLine();
                                    JOptionPane.showMessageDialog(ClientGUI.this, voteResponse);
                                    showNewsFeed();
                                } catch (IOException ex) {
                                    JOptionPane.showMessageDialog(ClientGUI.this, "Error communicating with the server.", "Communication Error", JOptionPane.ERROR_MESSAGE);
                                }
                            });
                            feedContentPanel.add(commentDownvoteButton);

                            JButton deleteCommentButton = new JButton("Delete Comment");
                            deleteCommentButton.addActionListener(e -> {
                                try {
                                    out.println("DELETE_COMMENT " + postId + " " + commentId + " " + currentUser);
                                    String deleteResponse = in.readLine();
                                    JOptionPane.showMessageDialog(ClientGUI.this, deleteResponse);
                                    showNewsFeed();
                                } catch (IOException ex) {
                                    JOptionPane.showMessageDialog(ClientGUI.this, "Error communicating with the server.", "Communication Error", JOptionPane.ERROR_MESSAGE);
                                }
                            });
                            feedContentPanel.add(deleteCommentButton);
                        }
                    }

                    if (hasComments) {
                        feedContentPanel.add(new JLabel("     Comments:"));
                    }

                    JTextField commentField = new JTextField();
                    commentField.setColumns(30);
                    feedContentPanel.add(commentField);

                    JButton postCommentButton = new JButton("Post Comment");
                    postCommentButton.addActionListener(e -> {
                        String commentContent = commentField.getText().trim();
                        if (!commentContent.isEmpty()) {
                            try {
                                out.println("ADD_COMMENT " + postId + " " + currentUser + " " + commentContent);
                                String commentResponse = in.readLine();
                                JOptionPane.showMessageDialog(ClientGUI.this, commentResponse);
                                showNewsFeed();
                            } catch (IOException ex) {
                                JOptionPane.showMessageDialog(ClientGUI.this, "Error communicating with the server.", "Communication Error", JOptionPane.ERROR_MESSAGE);
                            }
                        } else {
                            JOptionPane.showMessageDialog(ClientGUI.this, "Comment cannot be empty!");
                        }
                    });
                    feedContentPanel.add(postCommentButton);
                }
                index++;
            }
        }

        feedContentPanel.revalidate();
        feedContentPanel.repaint();

        feedPanel.setVisible(true);
    }


    private void handleVote(String action, String id) {
        String response = handleRequest(action + " " + id + " " + currentUser);
        if (response != null && (response.equals("Post upvoted.") || response.equals("Comment upvoted.") ||
                response.equals("Post downvoted.") || response.equals("Comment downvoted."))) {
            JOptionPane.showMessageDialog(ClientGUI.this, response);
            showNewsFeed();
        } else {
            JOptionPane.showMessageDialog(ClientGUI.this, "Failed to vote.");
        }
    }







    // Toggle the visibility of login UI components
    private void toggleLoginUI(boolean show) {
        usernameField.setVisible(show);
        passwordField.setVisible(show);
        loginButton.setVisible(show);
        registerButton.setVisible(show);
    }
    public static void main(String[] args) {
        // Initialize and show the GUI
        SwingUtilities.invokeLater(() -> {
            ClientGUI gui = new ClientGUI();
            gui.setVisible(true);
        });
    }
}



