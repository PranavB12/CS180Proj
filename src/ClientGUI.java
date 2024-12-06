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

/**
 * Group Project - CS18000 Gold
 *
 * ClientGUI class
 *
 * @author Pranav Bansal, Vivaan Malhotra, Rishi Rao, Mike Lee, lab sec 37
 *
 * @version November 19, 2024
 *
 */

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
    private JButton logoutButton;
    private JButton searchUserButton; // New button for searching users

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

        // Add Search User button
        searchUserButton = new JButton("Search User");
        createPostPanel.add(searchUserButton, BorderLayout.WEST);

        feedPanel.add(createPostPanel, BorderLayout.SOUTH);

        // Inside the ClientGUI constructor (where feedPanel is set up)

        // Inside the ClientGUI constructor (where feedPanel is set up)

        // Create a separate panel for friend management buttons
        JPanel friendManagementPanel = new JPanel();
        friendManagementPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        // Add Friend Management Buttons
        JButton addFriendButton = new JButton("Add Friend");
        JButton removeFriendButton = new JButton("Remove Friend");
        JButton blockUserButton = new JButton("Block User");

        // Set smaller sizes for the buttons
        addFriendButton.setPreferredSize(new Dimension(100, 25));
        removeFriendButton.setPreferredSize(new Dimension(100, 25));
        blockUserButton.setPreferredSize(new Dimension(100, 25));

        // Add buttons to the friendManagementPanel
        friendManagementPanel.add(addFriendButton);
        friendManagementPanel.add(removeFriendButton);
        friendManagementPanel.add(blockUserButton);

        // Add the friendManagementPanel to the top of feedPanel
        feedPanel.add(friendManagementPanel, BorderLayout.NORTH);

        // Add Event Listeners for Friend Management Buttons
        addFriendButton.addActionListener(e -> {
            String friendUsername = JOptionPane.showInputDialog(this, "Enter the username of the friend to add:");
            if (friendUsername != null && !friendUsername.trim().isEmpty()) {
                String response = handleRequest("ADD_FRIEND " + currentUser + " " + friendUsername.trim());
                JOptionPane.showMessageDialog(this, response);
            }
        });

        removeFriendButton.addActionListener(e -> {
            String friendUsername = JOptionPane.showInputDialog(this, "Enter the username of the friend to remove:");
            if (friendUsername != null && !friendUsername.trim().isEmpty()) {
                String response = handleRequest("REMOVE_FRIEND " + currentUser + " " + friendUsername.trim());
                JOptionPane.showMessageDialog(this, response);
            }
        });

        blockUserButton.addActionListener(e -> {
            String blockUsername = JOptionPane.showInputDialog(this, "Enter the username of the user to block:");
            if (blockUsername != null && !blockUsername.trim().isEmpty()) {
                String response = handleRequest("BLOCK_USER " + currentUser + " " + blockUsername.trim());
                JOptionPane.showMessageDialog(this, response);
            }
        });



        // Add panels to frame
        getContentPane().add(loginPanel, BorderLayout.NORTH);
        getContentPane().add(feedPanel, BorderLayout.CENTER);

        // Set up event listeners
        loginButton.addActionListener(new LoginButtonListener());
        registerButton.addActionListener(new RegisterButtonListener());
        createPostButton.addActionListener(new CreatePostButtonListener());
        logoutButton.addActionListener(new LogoutButtonListener());
        searchUserButton.addActionListener(new SearchUserButtonListener()); // New ActionListener

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
    private class LogoutButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            currentUser = null;
            toggleLoginUI(true);
            feedPanel.setVisible(false);
        }
    }

    // ActionListener for login
    private class LoginButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            try {
                out.println("VALIDATE " + username + " " + password);
                String response = in.readLine();

                if ("Valid credentials".equals(response)) {
                    currentUser = username;
                    showNewsFeed();
                    feedPanel.setVisible(true);
                    toggleLoginUI(false);
                } else {
                    JOptionPane.showMessageDialog(ClientGUI.this, "Invalid credentials.");
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(ClientGUI.this, "Error communicating with the server.", "Communication Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // ActionListener for registration
    // ActionListener for registration
    private class RegisterButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            // Check if username and password fields are not empty
            if (username.trim().isEmpty() || password.trim().isEmpty()) {
                JOptionPane.showMessageDialog(ClientGUI.this, "Username and password cannot be empty.", "Input Error", JOptionPane.WARNING_MESSAGE);
                return; // Exit early if fields are empty
            }

            try {
                out.println("ADD_USER " + username + " " + password + " Some Name");
                String response = in.readLine();

                if ("User added".equals(response)) {
                    JOptionPane.showMessageDialog(ClientGUI.this, "User registered successfully.");
                } else {
                    JOptionPane.showMessageDialog(ClientGUI.this, "Failed to register user.");
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(ClientGUI.this, "Error communicating with the server.", "Communication Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


    // ActionListener for searching a user
    private class SearchUserButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String usernameToSearch = JOptionPane.showInputDialog(ClientGUI.this, "Enter username to search:");
            if (usernameToSearch != null && !usernameToSearch.trim().isEmpty()) {
                try {
                    out.println("GET_USER " + usernameToSearch);
                    String response = in.readLine();

                    if (response != null && !response.isEmpty()) {
                        // Split the response string at "--"
                        String[] details = response.split("--");
                        if (details.length == 3) {
                            String username = details[0].replace("Username: ", "").trim();
                            String actualName = details[1].replace("Actual Name: ", "").trim();
                            String description = details[2].replace("Description: ", "").trim();

                            // Display user details in a dialog
                            JOptionPane.showMessageDialog(ClientGUI.this,
                                    "Username: " + username + "\n" +
                                            "Actual Name: " + actualName + "\n" +
                                            "Description: " + description,
                                    "User Details", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            // Handle unexpected response format
                            JOptionPane.showMessageDialog(ClientGUI.this,
                                    "Unexpected response format: " + response,
                                    "Error", JOptionPane.WARNING_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(ClientGUI.this,
                                "User not found.",
                                "Search Result", JOptionPane.WARNING_MESSAGE);
                    }
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(ClientGUI.this,
                            "Error communicating with the server.",
                            "Communication Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(ClientGUI.this,
                        "Username cannot be empty!",
                        "Input Error", JOptionPane.WARNING_MESSAGE);
            }
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
                    // Create a panel for the post content and set its layout to BoxLayout
                    JPanel postPanel = new JPanel();
                    postPanel.setLayout(new BoxLayout(postPanel, BoxLayout.Y_AXIS)); // Vertical layout for the post content

                    // Add post details (Posted by, content, upvotes, downvotes, etc.)
                    postPanel.add(new JLabel(line));
                    index++;

                    String content = lines.get(index);
                    postPanel.add(new JLabel(content));
                    index++;

                    String upvotes = lines.get(index);
                    postPanel.add(new JLabel(upvotes));
                    index++;

                    String downvotes = lines.get(index);
                    postPanel.add(new JLabel(downvotes));
                    index++;

                    String postId = lines.get(index);
                    index++;

                    // Create and add upvote button
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
                    postPanel.add(upvoteButton);

                    // Create and add downvote button
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
                    postPanel.add(downvoteButton);

                    // Panel for Enable/Disable Comments buttons (side by side)
                    // Enable Comments Button
                    JButton enableCommentsButton = new JButton("Enable Comments");
                    enableCommentsButton.addActionListener(e -> {
                        try {
                            out.println("ENABLE_COMMENTS " + postId + " " + currentUser);
                            String responseEnable = in.readLine();
                            JOptionPane.showMessageDialog(ClientGUI.this, responseEnable);
                            showNewsFeed();  // Refresh the feed to reflect changes
                        } catch (IOException ex) {
                            JOptionPane.showMessageDialog(ClientGUI.this, "Error communicating with the server.", "Communication Error", JOptionPane.ERROR_MESSAGE);
                        }
                    });
                    postPanel.add(enableCommentsButton);

                    // Disable Comments Button
                    JButton disableCommentsButton = new JButton("Disable Comments");
                    disableCommentsButton.addActionListener(e -> {
                        try {
                            out.println("DISABLE_COMMENTS " + postId + " " + currentUser);
                            String responseDisable = in.readLine();
                            JOptionPane.showMessageDialog(ClientGUI.this, responseDisable);
                            showNewsFeed();  // Refresh the feed to reflect changes
                        } catch (IOException ex) {
                            JOptionPane.showMessageDialog(ClientGUI.this, "Error communicating with the server.", "Communication Error", JOptionPane.ERROR_MESSAGE);
                        }
                    });
                    postPanel.add(disableCommentsButton);

                    // Hide Post Button
                    JButton hidePostButton = new JButton("Hide Post");
                    hidePostButton.addActionListener(e -> {
                        try {
                            out.println("HIDE_POST " + postId + " " + currentUser);
                            String hideResponse = in.readLine();
                            JOptionPane.showMessageDialog(ClientGUI.this, hideResponse);
                            showNewsFeed();  // Refresh the feed to reflect changes
                        } catch (IOException ex) {
                            JOptionPane.showMessageDialog(ClientGUI.this, "Error communicating with the server.", "Communication Error", JOptionPane.ERROR_MESSAGE);
                        }
                    });
                    postPanel.add(hidePostButton);

                    // Add the post panel to the feed content panel
                    feedContentPanel.add(postPanel);

                    // Display comments (if any)
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

                            postPanel.add(new JLabel("   Commented by: " + commenter));
                            postPanel.add(new JLabel("     " + commentContent));
                            postPanel.add(new JLabel("     Upvotes: " + upVotes));
                            postPanel.add(new JLabel("     DownVotes: " + downVotes));

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
                            postPanel.add(commentUpvoteButton);

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
                            postPanel.add(commentDownvoteButton);

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
                            postPanel.add(deleteCommentButton);
                        }
                    }

                    if (hasComments) {
                        postPanel.add(new JLabel("     Comments:"));
                    }

                    JTextField commentField = new JTextField();
                    commentField.setColumns(30);
                    postPanel.add(commentField);

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
                    postPanel.add(postCommentButton);
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
    private synchronized String handleRequest(String request) {
        try {
            out.println(request);
            return in.readLine();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error communicating with the server.", "Communication Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }



    // Other methods (e.g., createPostButton, showNewsFeed, logoutButton) remain unchanged.

    private void toggleLoginUI(boolean show) {
        usernameField.setVisible(show);
        passwordField.setVisible(show);
        loginButton.setVisible(show);
        registerButton.setVisible(show);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ClientGUI gui = new ClientGUI();
            gui.setVisible(true);
        });
    }
}
