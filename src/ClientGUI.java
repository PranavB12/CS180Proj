package src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

public class ClientGUI extends JFrame {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private Server server; // Connect to the server
    private String currentUser;

    // GUI components
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPanel feedPanel; // Updated feed panel for dynamic layout
    private JTextArea createPostArea;
    private JButton loginButton;
    private JButton registerButton;
    private JButton createPostButton;
    private JButton logoutButton; // New logout button

    public ClientGUI(Server server) {
        this.server = server;

        setTitle("News Feed App - Client");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);  // Prevent the window from closing immediately
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

        // Add a WindowListener to perform the writeDatabase function when closing the window
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                server.saveData();  // Call writeDatabase before closing
                System.exit(0);  // Close the application
            }
        });
    }

    // ActionListener for login
    private class LoginButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            if (server.loginUser(username, password)) {
                currentUser = username;
                showNewsFeed();
                toggleLoginUI(false);
            } else {
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

            if (server.registerUser(username, password)) {
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
                String postId = server.createPost(postContent, currentUser);
                if (postId != null) {
                    JOptionPane.showMessageDialog(ClientGUI.this, "Post created with ID: " + postId);
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

    // Show the user's newsfeed
    private void showNewsFeed() {
        JPanel feedContentPanel = (JPanel) ((JScrollPane) feedPanel.getComponent(0)).getViewport().getView();
        feedContentPanel.removeAll();

        ArrayList<String> lines = new ArrayList<>(server.getPostsForUser(currentUser)); // Fetch posts from the server

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

                String postId = lines.get(index);  // Assuming the postId is right after the vote counts
                index++;

                // Add upvote button
                JButton upvoteButton = new JButton("Upvote");
                upvoteButton.addActionListener(e -> {
                    if (server.upvotePost(postId, currentUser)) {
                        JOptionPane.showMessageDialog(ClientGUI.this, "Post upvoted.");
                        showNewsFeed(); // Refresh feed
                    } else {
                        JOptionPane.showMessageDialog(ClientGUI.this, "Failed to upvote post.");
                    }
                });
                feedContentPanel.add(upvoteButton);

                // Add downvote button
                JButton downvoteButton = new JButton("Downvote");
                downvoteButton.addActionListener(e -> {
                    if (server.downvotePost(postId, currentUser)) {
                        JOptionPane.showMessageDialog(ClientGUI.this, "Post downvoted.");
                        showNewsFeed(); // Refresh feed
                    } else {
                        JOptionPane.showMessageDialog(ClientGUI.this, "Failed to downvote post.");
                    }
                });
                feedContentPanel.add(downvoteButton);

                // Render comments (if any)
                boolean hasComments = false;
                while (index < lines.size() && lines.get(index).equals("     Comments: ")) {
                    hasComments = true;
                    index++; // Skip over "Comments: " line

                    while (index < lines.size() && !lines.get(index).equals("---------------------------------------------------------------------------------------------------------") && !lines.get(index).startsWith("Posted by:")) {
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

                    }
                }

                // If comments exist, render them under the buttons
                if (hasComments) {
                    feedContentPanel.add(new JLabel("     Comments:"));
                }

                // Add input for new comment
                JTextField commentField = new JTextField();
                commentField.setColumns(30);
                feedContentPanel.add(commentField);

                JButton postCommentButton = new JButton("Post Comment");
                postCommentButton.addActionListener(e -> {
                    String commentContent = commentField.getText().trim();
                    if (!commentContent.isEmpty()) {
                        String commentId = server.addCommentToPost(postId, commentContent, currentUser);
                        if (commentId != null) {
                            JOptionPane.showMessageDialog(ClientGUI.this, "Comment added with ID: " + commentId);
                            showNewsFeed(); // Refresh feed
                        } else {
                            JOptionPane.showMessageDialog(ClientGUI.this, "Failed to add comment.");
                        }
                    } else {
                        JOptionPane.showMessageDialog(ClientGUI.this, "Comment cannot be empty!");
                    }
                });

                feedContentPanel.add(postCommentButton);
            }
            index++;
        }

        feedContentPanel.revalidate();
        feedContentPanel.repaint();

        feedPanel.setVisible(true);
    }

    // Toggle the visibility of login UI components
    private void toggleLoginUI(boolean show) {
        usernameField.setVisible(show);
        passwordField.setVisible(show);
        loginButton.setVisible(show);
        registerButton.setVisible(show);
    }

    public static void main(String[] args) {
        // Initialize the server and read the database before showing the GUI
        Server server = new Server();
        server.readData();
          // Load data from the database/file

        ClientGUI gui = new ClientGUI(server);
        gui.setVisible(true);
    }
}
