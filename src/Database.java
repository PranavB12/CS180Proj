package src;

import java.util.*;
import java.io.*;

public class Database implements IDatabase {

    private ArrayList<User> users = new ArrayList<>();
    private Map<Integer, Post> posts = new HashMap<>();
    private static int postIdCounter = 0;
    private static final Object postIdLock = new Object();
    private static final Object usersLock = new Object();
    private static final Object postsLock = new Object();

//    public Database(List<User> users, Map<Integer, Post> posts) {
//        this.posts = posts;
//        this.users = new ArrayList<>();
//        this.posts = new HashMap<>();
//    }

    // Add a new user to the database
    @Override
    public boolean addUser(User user) {
        if (user == null || userExists(user.getUsername())) {
            System.out.println("User already exists or invalid user data.");
            return false;
        }
        synchronized (usersLock) {
            users.add(user);
        }
        System.out.println("User added: " + user.getUsername());
        return true;
    }

    // Remove an existing user from the database
    @Override
    public boolean removeUser(User user) {
        // This needs to be fixed -> if (user == null || !userExists(user.getUsername()))
        if (user == null || !userExists(user.getUsername())) {
            System.out.println("User does not exist.");
            return false;
        }
        synchronized (usersLock) {
            users.remove(user);
        }
        System.out.println("User removed: " + user.getUsername());
        return true;
    }

    // Account creation method
    public boolean createAccount(String username, String password, String name) {
        if (username == null || password == null || name == null) {
            System.out.println("Invalid content or author does not exist.");
            return false;
        }
        User user = new User(username, password, name);
        synchronized (usersLock) {
            users.add(user);
        }
        return true;
    }

    // Validate user credentials
    @Override
    public boolean validateCredentials(String username, String password) {
        User user = findUserByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            System.out.println("Credentials validated for user: " + username);
            return true;
        }
        System.out.println("Invalid credentials.");
        return false;
    }

    // Check if a user exists by username
    @Override
    public boolean userExists(String username) {
        return findUserByUsername(username) != null;
    }

    // Create a new post by a user
    public int createPost(String content, User author) {
        if (content == null || author == null || !userExists(author.getUsername())) {
            System.out.println("Invalid content or author does not exist.");
            return -1;
        }
        int postId;
        synchronized (postIdLock) {
            postId = ++postIdCounter;
        }
        Post post = new Post(content, author) {
            public void deletePost() {

            }
        };
        post.setId(postId);
        synchronized (postsLock) {
            posts.put(post.getId(), post);
        }
        System.out.println("Post created with ID: " + post.getId() + " by user: " + author.getUsername());
        return post.getId();
    }

    // Delete a post by its ID
    public boolean deletePost(int postId) {
        synchronized (postsLock) {
            if (!posts.containsKey(postId)) {
                System.out.println("Post with ID " + postId + " does not exist.");
                return false;
            }
            posts.remove(postId);
        }
        System.out.println("Post with ID " + postId + " deleted.");
        return true;
    }

    // View user information and their posts
    public User viewUser(String username) {
        User user = findUserByUsername(username);
        if (user == null) {
            System.out.println("User does not exist.");
            return null;
        }
        System.out.println("User: " + user.getUsername());
        System.out.println("Name: " + user.getName());
        System.out.println("Friends: " + user.getFriends().size());
        System.out.println("Blocked Users: " + user.getBlockedUsers().size());
        System.out.println("Posts:");
        synchronized (postsLock) {
            for (Post post : posts.values()) {
                if (post.getAuthor().equals(user)) {
                    System.out.println(" - Post ID: " + post.getId() + ", Content: " + post.getContent());
                }
            }
        }
        return user;
    }

    // Add a friend to a user's friend list
    public boolean addFriend(User user, User friend) {
        if (user == null || friend == null || !userExists(user.getUsername()) || !userExists(friend.getUsername())) {
            System.out.println("One or both users do not exist.");
            return false;
        }
        synchronized (usersLock) {
            if (user.getFriends().contains(friend)) {
                System.out.println("Friend already added.");
                return false;
            }
            user.getFriends().add(friend);
            friend.getFriends().add(user);
        }
        System.out.println("Friend added: " + friend.getUsername() + " to user: " + user.getUsername());
        return true;
    }

    // Remove a friend from a user's friend list
    public boolean removeFriend(User user, User friend) {
        if (user == null || friend == null || !userExists(user.getUsername()) || !userExists(friend.getUsername())) {
            System.out.println("One or both users do not exist.");
            return false;
        }
        synchronized (usersLock) {
            if (!user.getFriends().remove(friend)) {
                System.out.println("Friend not found in list.");
                return false;
            }
            friend.getFriends().remove(user);
        }
        System.out.println("Friend removed: " + friend.getUsername() + " from user: " + user.getUsername());
        return true;
    }

    // Block a user and remove them from friends if necessary
    public boolean blockUser(User user, User toBlock) {
        if (user == null || toBlock == null || !userExists(user.getUsername()) || !userExists(toBlock.getUsername())) {
            System.out.println("One or both users do not exist.");
            return false;
        }
        synchronized (usersLock) {
            if (user.getBlockedUsers().contains(toBlock)) {
                System.out.println("User is already blocked.");
                return false;
            }
            user.getBlockedUsers().add(toBlock);
            user.getFriends().remove(toBlock);
            toBlock.getFriends().remove(user);
        }
        System.out.println("User blocked: " + toBlock.getUsername() + " by user: " + user.getUsername());
        return true;
    }

    // Helper method to find a user by username
    private User findUserByUsername(String username) {
        synchronized (usersLock) {
            System.out.println("Before the for loop " + users.size());
            if (users.size() != 0) {
                for (User user : users) {
                    System.out.println("User in Database Class Username: " + user.getUsername());
                    if (user.getUsername().trim().equals(username.trim())) {
                        return user;
                    }
                }
            }
        }
        return null;
    }

    // Upvote a post
    public void upvotePost(int postId) {
        Post post;
        synchronized (postsLock) {
            post = posts.get(postId);
        }
        if (post != null) {
            post.upvote();
            synchronized (postsLock) {
                posts.put(post.getId(), post);
            }
        } else {
            System.out.println("Post with ID " + postId + " not found.");
        }
    }

    // Downvote a post
    public void downvotePost(int postId) {
        Post post;
        synchronized (postsLock) {
            post = posts.get(postId);
        }
        if (post != null) {
            post.downvote();
            synchronized (postsLock) {
                posts.put(post.getId(), post);
            }
        } else {
            System.out.println("Post with ID " + postId + " not found.");
        }
    }

    // Add a comment to a post
    public void addCommentToPost(int postId, String comment) {
        Post post;
        synchronized (postsLock) {
            post = posts.get(postId);
        }
        if (post != null) {
            post.addComment(comment);
            synchronized (postsLock) {
                posts.put(post.getId(), post);
            }
        } else {
            System.out.println("Post with ID " + postId + " not found.");
        }
    }

    // Delete a comment from a post
    public void deleteCommentFromPost(int postId, int commentId) {
        Post post;
        synchronized (postsLock) {
            post = posts.get(postId);
        }
        if (post != null) {
            post.deleteComment(commentId);
            synchronized (postsLock) {
                posts.put(post.getId(), post);
            }
        } else {
            System.out.println("Post with ID " + postId + " not found.");
        }
    }

    // Hide a post
    public void hidePost(int postId) {
        Post post;
        synchronized (postsLock) {
            post = posts.get(postId);
        }
        if (post != null) {
            post.hidePost();
            synchronized (postsLock) {
                posts.put(post.getId(), post);
            }
        } else {
            System.out.println("Post with ID " + postId + " not found.");
        }
    }

    // Enable comments for a post
    public void enableCommentsForPost(int postId) {
        Post post;
        synchronized (postsLock) {
            post = posts.get(postId);
        }
        if (post != null) {
            post.enableComments();
            synchronized (postsLock) {
                posts.put(post.getId(), post);
            }
        } else {
            System.out.println("Post with ID " + postId + " not found.");
        }
    }

    // Disable comments for a post
    public void disableCommentsForPost(int postId) {
        Post post;
        synchronized (postsLock) {
            post = posts.get(postId);
        }
        if (post != null) {
            post.disableComments();
            synchronized (postsLock) {
                posts.put(post.getId(), post);
            }
        } else {
            System.out.println("Post with ID " + postId + " not found.");
        }
    }
    public void saveDatabaseToFile(String filename) {
        try (FileWriter writer = new FileWriter(filename)) {
            writer.write("Users:\n");
            for (User user : users) {
                writer.write("Username: " + user.getUsername() + ", Name: " + user.getName() + "\n");
                writer.write("Friends: ");
                for (User friend : user.getFriends()) {
                    writer.write(friend.getUsername() + " ");
                }
                writer.write("\nBlocked Users: ");
                for (User blocked : user.getBlockedUsers()) {
                    writer.write(blocked.getUsername() + " ");
                }
                writer.write("\nPosts:\n");
                for (Post post : posts.values()) {
                    if (post.getAuthor().equals(user)) {
                        writer.write(" - Post ID: " + post.getId() + ", Content: " + post.getContent() + "\n");
                        writer.write("   Upvotes: " + post.getUpVotes() + ", Downvotes: " + post.getDownVotes() + "\n");
                        writer.write("   Comments:\n");
                        for (Map.Entry<Integer, String> comment : post.getComments().entrySet()) {
                            writer.write("    - ID " + comment.getKey() + ": " + comment.getValue() + "\n");
                        }
                    }
                }
                writer.write("\n");
            }
            System.out.println("Database saved to " + filename);
        } catch (IOException e) {
            System.out.println("An error occurred while saving the database: " + e.getMessage());
        }
    }
    public void readDatabaseFromFile(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            User currentUser = null;

            while ((line = reader.readLine()) != null) {
                line = line.trim();

                if (line.startsWith("Username:")) {
                    // Parse user details
                    String[] parts = line.split(", ");
                    if (parts.length >= 2) {
                        String[] usernameParts = parts[0].split(": ");
                        String[] nameParts = parts[1].split(": ");
                        String username = (usernameParts.length > 1) ? usernameParts[1] : "";
                        String name = (nameParts.length > 1) ? nameParts[1] : "";
                        currentUser = new User(username, "", name); // Password left empty for security reasons
                        synchronized (usersLock) {
                            users.add(currentUser);
                        }
                    }
                } else if (line.startsWith("Friends:") && currentUser != null) {
                    // Parse friends for the current user
                    String friendsList = line.substring("Friends: ".length()).trim();
                    if (!friendsList.isEmpty()) {
                        String[] friends = friendsList.split(" ");
                        for (String friendUsername : friends) {
                            User friend = findUserByUsername(friendUsername);
                            if (friend != null) {
                                currentUser.getFriends().add(friend);
                            }
                        }
                    }
                } else if (line.startsWith("Blocked Users:") && currentUser != null) {
                    // Parse blocked users for the current user
                    String blockedList = line.substring("Blocked Users: ".length() - 1).trim();
                    if (!blockedList.isEmpty()) {
                        String[] blockedUsers = blockedList.split(" ");
                        for (String blockedUsername : blockedUsers) {
                            User blockedUser = findUserByUsername(blockedUsername);
                            if (blockedUser != null) {
                                currentUser.getBlockedUsers().add(blockedUser);
                            }
                        }
                    }
                } else if (line.startsWith(" - Post ID:") && currentUser != null) {
                    // Parse post details
                    String[] parts = line.split(", ");
                    if (parts.length >= 2) {
                        String[] postIdParts = parts[0].split(": ");
                        String[] contentParts = parts[1].split(": ");
                        int postId = (postIdParts.length > 1) ? Integer.parseInt(postIdParts[1]) : -1;
                        String content = (contentParts.length > 1) ? contentParts[1] : "";
                        Post post = new Post(content, currentUser) {
                            public void deletePost() {

                            }
                        };
                        post.setId(postId);

                        // Parse upvotes and downvotes
                        String upvotesLine = reader.readLine().trim();
                        String downvotesLine = reader.readLine().trim();
                        if (upvotesLine.startsWith("Upvotes:") && downvotesLine.startsWith("Downvotes:")) {
                            post.setUpVotes(Integer.parseInt(upvotesLine.split(": ")[1]));
                            post.setDownVotes(Integer.parseInt(downvotesLine.split(": ")[1]));
                        }

                        // Parse comments
                        String commentLine;
                        while ((commentLine = reader.readLine()) != null && commentLine.startsWith("    - ID ")) {
                            String[] commentParts = commentLine.split(": ");
                            if (commentParts.length >= 2) {
                                int commentId = Integer.parseInt(commentParts[0].split(" ")[2]);
                                String commentContent = commentParts[1];
                                post.addComment(commentContent);
                            }
                        }

                        synchronized (postsLock) {
                            posts.put(post.getId(), post);
                        }
                    }
                }
            }
            System.out.println("Database loaded from " + filename);
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + filename);
        } catch (IOException e) {
            System.out.println("An error occurred while reading the database: " + e.getMessage());
        }
    }
}

