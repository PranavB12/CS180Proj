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


    private List<User> users = new ArrayList<>();
    private Map<String, Post> posts = new HashMap<>();
    private static int postIdCounter = 0;
    private static final Object postIdLock = new Object();
    private static final Object usersLock = new Object();
    private static final Object postsLock = new Object();
    private Map<String, Comment> comments;

    // temp
    public Database() {
        this.users = new ArrayList<>();
        this.posts = new HashMap<>();
        this.comments = new HashMap<>();
    }
    public Database(List<User> users, Map<String, Post> posts, Map<String, Comment> comments) {
        this.users = users;
        this.posts = posts;
        this.comments = comments;
    }
    public Map<String, Comment> getComments() {
        return this.comments;
    }

    // Add a new user to the database
    public List<User> getUsers() {
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
    public synchronized boolean addFriend(User user, User friend) {
        if (!user.getFriends().contains(friend)) {
            user.addFriend(friend); // Add friend to the user's list
            friend.addFriend(user);
            updateUserInDatabase(user);
            updateUserInDatabase(friend);// Update the user in the database
            return true;
        }
        return false; // Return false if the user is already friends with the given friend
    }

    // Remove a friend from a user's friend list
    public synchronized boolean removeFriend(User user, User friend) {
        if (user.getFriends().contains(friend)) {
            user.removeFriend(friend); // Remove friend from the user's list
            friend.removeFriend(user);
            updateUserInDatabase(user);
            updateUserInDatabase(friend);// Update the user in the database
            return true;
        }
        return false; // Return false if the user is not friends with the given friend
    }

    // Block a user and remove them from friends if necessary
    public synchronized boolean blockUser(User user, User toBlock) {
        if (!user.getBlockedUsers().contains(toBlock)) {
            user.blockUser(toBlock);
            updateUserInDatabase(user); // Update the user in the database
            return true;
        }
        return false; // Return false if the user has already blocked the given user
    }

    // Helper method to find a user by username
    private User findUserByUsername(String username) {
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
    public void addCommentToPost(String postId, String comment , User commentAuthor) {
        Post post;
        Comment com = new Comment(comment, commentAuthor, postId);
        synchronized (postsLock) {
            post = posts.get(postId);
        }
        comments.put(com.getID(), com);

        if (post != null) {
            post.addComment(com.getID(), com);
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
        comments.remove(commentId);
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
    public void writeDatabaseToFile(String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            // Write Users
            writer.write("USERS\n");
            synchronized (usersLock) {
                for (User user : users) {
                    writer.write(user.getUsername() + "," + user.getPassword() + "," + user.getName() + "," +
                            (user.getDescription() != null ? user.getDescription() : "") + "\n");
                    writer.write("FRIENDS:" + user.getFriends().stream().map(User::getUsername).toList() + "\n");
                    writer.write("BLOCKED:" + user.getBlockedUsers().stream().map(User::getUsername).toList() + "\n");
                }
            }

            // Write Posts
            writer.write("POSTS\n");
            synchronized (postsLock) {
                for (Post post : posts.values()) {
                    writer.write(post.getId() + "," + post.getContent() + "," + post.getAuthor().getUsername() + "," +
                            post.getUpVotes() + "," + post.getDownVotes() + "," + post.getHidden() + "," +
                            post.getComments().keySet() + "\n");
                }
            }

            // Write Comments
            writer.write("COMMENTS\n");
            synchronized (postsLock) {
                for (Comment comment : comments.values()) {
                    writer.write(comment.getID() + "," + comment.getContent() + "," + comment.getAuthor().getUsername() + "," +
                            comment.getPostID() + "\n");
                }
            }
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }

    public void readDatabaseFromFile(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            String section = "";
            Map<String, User> userMap = new HashMap<>(); // Temp map for quick access

            while ((line = reader.readLine()) != null) {
                if (line.equals("USERS")) {
                    section = "USERS";
                    continue;
                } else if (line.equals("POSTS")) {
                    section = "POSTS";
                    continue;
                } else if (line.equals("COMMENTS")) {
                    section = "COMMENTS";
                    continue;
                }

                switch (section) {
                    case "USERS" -> {
                        if (line.startsWith("FRIENDS:") || line.startsWith("BLOCKED:")) {
                            // Handle friends and blocked users
                            String[] data = line.split(":", 2);
                            List<String> relatedUsers = Arrays.asList(data[1].replaceAll("[\\[\\]]", "").split(", "));
                            if (data[0].equals("FRIENDS")) {
                                User user = users.get(users.size() - 1); // Last user added
                                for (String friendUsername : relatedUsers) {
                                    user.addFriend(userMap.get(friendUsername));
                                }
                            } else if (data[0].equals("BLOCKED")) {
                                User user = users.get(users.size() - 1); // Last user added
                                for (String blockedUsername : relatedUsers) {
                                    user.blockUser(userMap.get(blockedUsername));
                                }
                            }
                        } else {
                            // Handle regular user data
                            String[] data = line.split(",", 4);
                            User user = new User(data[0], data[1], data[2]);
                            user.setDescription(data[3]);
                            users.add(user);
                            userMap.put(data[0], user); // Map for quick access
                        }
                    }
                    case "POSTS" -> {
                        String[] data = line.split(",", 7);
                        User author = userMap.get(data[2]);
                        Post post = new Post(data[1], author, data[0]);
                        post.setUpVotes(Integer.parseInt(data[3]));
                        post.setDownVotes(Integer.parseInt(data[4]));
                        post.setHidden(Boolean.parseBoolean(data[5]));
                        posts.put(data[0], post);

                        // Add comments if any
                        if (!data[6].equals("[]")) {
                            List<String> commentIds = Arrays.asList(data[6].replaceAll("[\\[\\]]", "").split(", "));
                            for (String commentId : commentIds) {
                                Comment comment = comments.get(commentId);
                                post.addComment(commentId, comment);
                            }
                        }
                    }
                    case "COMMENTS" -> {
                        String[] data = line.split(",", 4);
                        User author = userMap.get(data[2]);
                        Comment comment = new Comment(data[1], author,data[3], data[0]);
                        comments.put(data[0], comment);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading from file: " + e.getMessage());
        }
    }
    private void updateUserInDatabase(User user) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUsername().equals(user.getUsername())) {
                users.set(i, user); // Replace the old user object with the updated one
                return;
            }
        }
    }


}
