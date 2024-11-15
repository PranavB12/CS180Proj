package src;

import java.util.*;
import java.io.*;
/**
 * Group Project - CS18000 Gold
 *
 * Database class, where all data is managed
 *
 * @author Pranav Bansal, Vivaan Malhotra, Rishi Rao, Mike Lee, Vaishnavi Sharma, lab sec 37
 *
 * @version November 3, 2024
 *
 */
public class Database implements IDatabase {


    private ArrayList<User> users = new ArrayList<>();
    private Map<String, Post> posts = new HashMap<>();
    private static int postIdCounter = 0;
    private static final Object postIdLock = new Object();
    private static final Object usersLock = new Object();
    private static final Object postsLock = new Object();

    // temp
//    public Database(List<User> users, Map<Integer, Post> posts) {
//        this.posts = posts;
//        this.users = new ArrayList<>();
//        this.posts = new HashMap<>();
//    }

    // Add a new user to the database
    public ArrayList<User> getUsers() {
        return users;
    }
    public Map<String, Post> getPosts() {
        return posts;
    }
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
        for (int i = 0; i < users.size(); i++) {
            User newUser = users.get(i);
            if (newUser.getUsername().equals(username)) {
                System.out.println("User already exists.");
                return false;
            }
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

    public String createPost(String content, User author) {
        if (content == null || author == null || !userExists(author.getUsername())) {
            System.out.println("Invalid content or author does not exist.");
            return null;
        }

        // Generate a unique post ID using UUID
        String postId = UUID.randomUUID().toString();
        Post post = new Post(content, author, postId);

        // Use a method to add the post to the author's list
//        author.addPost(post);

        // Update the user in the users list
        for (int i = 0; i < users.size(); i++) {
            if (author.equals(users.get(i))) {
                users.set(i, author);
            }
        }

        // Add the post to the posts map
        synchronized (postsLock) {
            posts.put(postId, post);
        }

        System.out.println("Post created with ID: " + post.getId() + " by user: " + author.getUsername());
        return postId;
    }

    // Delete a post by its ID
    public boolean deletePost(String postId) {
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
    User findUserByUsername(String username) {
        synchronized (usersLock) {
            // users is not iterating through anything because users is null

            for (User user : users) {
                //System.out.println("User in Database Class Username: " + user.getUsername());
                if (user.getUsername().trim().equals(username.trim())) {
                    return user;
                }
            }
        }
        return null;
    }

    // Upvote a post
    public void upvotePost(String postId) {
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
    public void downvotePost(String postId) {
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
    public void addCommentToPost(String postId, String comment) {
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
    public void deleteCommentFromPost(String postId, String commentId) {
        Post post;
        synchronized (postsLock) {
            post = posts.get(postId);
        }
        if (post != null) {
            post.deleteComment(String.valueOf(commentId));
            synchronized (postsLock) {
                posts.put(post.getId(), post);
            }
        } else {
            System.out.println("Post with ID " + postId + " not found.");
        }
    }

    // Hide a post
    public void hidePost(String postId) {
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
    public void enableCommentsForPost(String postId) {
        Post post;
        synchronized (postsLock) {
            post = posts.get(postId);
        }
        if (post != null) {
            post.enableComments();
            synchronized (postsLock) {
                posts.put(postId, post);
            }
        } else {
            System.out.println("Post with ID " + postId + " not found.");
        }
    }

    // Disable comments for a post
    public void disableCommentsForPost(String postId) {
        Post post;
        synchronized (postsLock) {
            post = posts.get(postId);
        }
        if (post != null) {
            post.disableComments();
            synchronized (postsLock) {
                posts.put(postId, post);
            }

        } else {
            System.out.println("Post with ID " + postId + " not found.");
        }
    }

    // Save Database to txt file
    public void saveDatabaseToFile(String filename) {
        synchronized (usersLock) {
            synchronized (postsLock) {
                try (FileWriter writer = new FileWriter(filename)) {
                    writer.write("Users:\n");
                    for (User user : users) {
                        writer.write("Username: " + user.getUsername() + ", Password: " + user.getPassword() + ", Name: " + user.getName() + "\n");
                        writer.write("Description: " + user.getDescription());
                        writer.write("\nFriends: ");

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
                                writer.write(" - Post ID: " + post.getId() + ", Content: " + post.getContent() + ", Hidden: " + post.isHidden() + "\n");
                                writer.write("   Upvotes: " + post.getUpVotes() + "\n   Downvotes: " + post.getDownVotes() + "\n");
                                String a;
                                if (post.isCommentsEnabled()) {
                                    a = "Enabled";
                                } else {
                                    a = "Disabled";
                                }
                                writer.write("   Comments: " + a + "\n");

                                for (Map.Entry<String, String> comment : post.getComments().entrySet()) {
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
        }
    }

    public void readDatabaseFromFile(String filename) {
        synchronized (usersLock) {
            synchronized (postsLock) {
                try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
                    String line;
                    User currentUser = null;

                    while ((line = reader.readLine()) != null) {
                        line = line.trim();
                        if (line.startsWith("Username: ")) {
                            if (currentUser != null) {
                                users.add(currentUser);
                            }

                            String[] userDetails = line.split(", ");
                            String username = userDetails[0].split(": ")[1];
                            String password = userDetails[1].split(": ")[1];
                            String name = userDetails[2].split(": ")[1];

                            currentUser = new User(username, password, name);
                        } else if (line.startsWith("Description: ")) {
                            if (currentUser != null) {
                                currentUser.setDescription(line.split(": ", 2)[1]);
                            }
                        } else if (line.startsWith("Friends: ")) {
                            if (currentUser != null) {
                                String[] friends = line.split(": ")[1].trim().split(" ");
                                for (String friend : friends) {
                                    User friendUser = findUserByUsername(friend);
                                    if (friendUser != null) {
                                        currentUser.getFriends().add(friendUser);
                                    }
                                }
                            }
                        } else if (line.startsWith("Blocked Users: ")) {
                            if (currentUser != null) {
                                String[] blockedUsers = line.split(": ")[1].trim().split(" ");
                                for (String blockedUser : blockedUsers) {
                                    User blocked = findUserByUsername(blockedUser);
                                    if (blocked != null) {
                                        currentUser.getBlockedUsers().add(blocked);
                                    }
                                }
                            }
                        } else if (line.startsWith("Posts:")) {
                            while ((line = reader.readLine()) != null && !line.trim().isEmpty()) {
                                line = line.trim();
                                if (line.startsWith("- Post ID: ")) {
                                    String[] postDetails = line.split(", ");
                                    String postId = postDetails[0].split(": ")[1];
                                    String content = postDetails[1].split(": ")[1];
                                    boolean hidden = Boolean.parseBoolean(postDetails[2].split(": ")[1]);

                                    Post post = new Post(content, currentUser, postId);
                                    post.setHidden(hidden);

                                    line = reader.readLine().trim();
                                    if (line.startsWith("Upvotes: ")) {
                                        int upvotes = Integer.parseInt(line.split(": ")[1]);
                                        post.setUpVotes(upvotes);
                                    }
                                    line = reader.readLine().trim();
                                    if (line.startsWith("Downvotes: ")) {
                                        int downvotes = Integer.parseInt(line.split(": ")[1]);
                                        post.setDownVotes(downvotes);
                                    }
                                    line = reader.readLine().trim();
                                    if (line.startsWith("Comments: ")) {
                                        boolean commentsEnabled = line.split(": ")[1].equalsIgnoreCase("Enabled");
                                        post.setCommentsEnabled(commentsEnabled);
                                    }
                                    while ((line = reader.readLine()) != null && line.startsWith(" - ID ")) {
                                        int commentId = Integer.parseInt(line.split(":")[0].split(" ")[2]);
                                        String commentContent = line.split(": ", 2)[1].trim();
                                        post.addComment(commentContent);
                                    }
                                    posts.put(post.getId(), post);
                                    currentUser.getPosts().add(post);
                                }
                            }
                        }
                    }
                    if (currentUser != null) {
                        users.add(currentUser);
                    }
                    System.out.println("Database loaded from " + filename);
                } catch (IOException e) {
                    System.out.println("An error occurred while reading the database: " + e.getMessage());
                }
            }
        }
    }



}
