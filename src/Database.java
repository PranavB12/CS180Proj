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
        if (content == null || author == null || getUserByUsername(author.getUsername()) == null) {
            System.out.println("Invalid content or author does not exist.");
            return null;
        }

        // Generate a unique post ID using UUID
        String postId = UUID.randomUUID().toString();
        Post post = new Post(postId, content, author);

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
    public boolean deletePost(String postId, User requestedUser) {
        synchronized (postsLock) {
            if (!posts.containsKey(postId)) {
                System.out.println("Post with ID " + postId + " does not exist.");
                return false;
            }

            Post post = posts.get(postId);

            // Verify that the requested user is the author of the post
            if (!post.getAuthor().equals(requestedUser)) {
                System.out.println("User " + requestedUser.getUsername() + " is not authorized to delete this post.");
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
    public User findUserByUsername(String username) {
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
    public void upvotePost(String postId, User requestedUser) {
        Post post;
        synchronized (postsLock) {
            post = posts.get(postId);
        }

        if (post != null) {
            try {
                post.upvote(requestedUser.getUsername());
                synchronized (postsLock) {
                    posts.put(post.getId(), post);
                }
                System.out.println("Post with ID " + postId + " upvoted by " + requestedUser.getUsername() + ".");
            } catch (IllegalStateException e) {
                System.out.println(e.getMessage());
            }
        } else {
            System.out.println("Post with ID " + postId + " not found.");
        }
    }

    // Downvote a post
    public void downvotePost(String postId, User requestedUser) {
        Post post;
        synchronized (postsLock) {
            post = posts.get(postId);
        }

        if (post != null) {
            try {
                post.downvote(requestedUser.getUsername());
                synchronized (postsLock) {
                    posts.put(post.getId(), post);
                }
                System.out.println("Post with ID " + postId + " downvoted by " + requestedUser.getUsername() + ".");
            } catch (IllegalStateException e) {
                System.out.println(e.getMessage());
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
    public void deleteCommentFromPost(String postId, String commentId, User requestedUser) {
        Post post;
        synchronized (postsLock) {
            post = posts.get(postId);
        }

        if (post != null) {
            Comment comment = comments.get(commentId);

            if (comment == null) {
                System.out.println("Comment with ID " + commentId + " not found.");
                return;
            }

            // Verify that the requested user is the author of the comment
            if (!comment.getAuthor().equals(requestedUser)) {
                System.out.println("User " + requestedUser.getUsername() + " is not authorized to delete this comment.");
                return;
            }

            synchronized (postsLock) {
                comments.remove(commentId);
                post.deleteComment(commentId, requestedUser);
                posts.put(post.getId(), post);
            }

            System.out.println("Comment with ID " + commentId + " deleted from post with ID " + postId + ".");
        } else {
            System.out.println("Post with ID " + postId + " not found.");
        }
    }


    // Hide a post
    public void hidePost(String postId, User requestedUser) {
        Post post;
        synchronized (postsLock) {
            post = posts.get(postId);
        }

        if (post != null) {
            // Verify that the requested user is the author of the post
            if (!post.getAuthor().equals(requestedUser)) {
                System.out.println("User " + requestedUser.getUsername() + " is not authorized to hide this post.");
                return;
            }

            post.hidePost();

            synchronized (postsLock) {
                posts.put(post.getId(), post);
            }

            System.out.println("Post with ID " + postId + " has been hidden by the author.");
        } else {
            System.out.println("Post with ID " + postId + " not found.");
        }
    }

    // Enable comments for a post
    // Enable comments for a post
    public void enableCommentsForPost(String postId, User requestedUser) {
        Post post;
        synchronized (postsLock) {
            post = posts.get(postId);
        }

        if (post != null) {
            // Verify that the requested user is the author of the post
            if (!post.getAuthor().equals(requestedUser)) {
                System.out.println("User " + requestedUser.getUsername() + " is not authorized to enable comments for this post.");
                return;
            }

            post.enableComments();
            synchronized (postsLock) {
                posts.put(postId, post);
            }

            System.out.println("Comments enabled for post with ID " + postId + " by the author.");
        } else {
            System.out.println("Post with ID " + postId + " not found.");
        }
    }

    // Disable comments for a post
    public void disableCommentsForPost(String postId, User requestedUser) {
        Post post;
        synchronized (postsLock) {
            post = posts.get(postId);
        }

        if (post != null) {
            // Verify that the requested user is the author of the post
            if (!post.getAuthor().equals(requestedUser)) {
                System.out.println("User " + requestedUser.getUsername() + " is not authorized to disable comments for this post.");
                return;
            }

            post.disableComments();
            synchronized (postsLock) {
                posts.put(postId, post);
            }

            System.out.println("Comments disabled for post with ID " + postId + " by the author.");
        } else {
            System.out.println("Post with ID " + postId + " not found.");
        }
    }

    @Override
    public void writeDatabaseToFile(String filename) {

    }

    @Override
    public void readDatabaseFromFile(String filename) {

    }

    // Helper method to get User by username
    public User getUserByUsername(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }
    public void updateUserInDatabase(User user) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUsername().equals(user.getUsername())) {
                users.set(i, user); // Replace the old user object with the updated one
                return;
            }
        }
    }
    public Post getPostById(String postId) {
        // Check if the posts map contains the postId
        if (posts.containsKey(postId)) {
            return posts.get(postId); // Return the post with the specified postId
        } else {
            // If postId is not found, return null or handle as needed
            System.err.println("Post with ID " + postId + " not found.");
            return null;
        }
    }
    public synchronized void addPost(Post post) {
        // Check if the post already exists in the database
        if (posts.containsKey(post.getId())) {
            System.out.println("Post with ID " + post.getId() + " already exists.");
        } else {
            // Add the post to the database
            posts.put(post.getId(), post);
            System.out.println("Post created with ID: " + post.getId() + " by user: " + post.getAuthor().getUsername());
        }
    }


//    public synchronized void writeDatabaseToFile(String filePath) {
//        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
//            // Write Users
//            writer.write("USERS\n");
//
//            for (User user : users) {
//                writer.write(user.getUsername() + "," + user.getPassword() + "," + user.getName() + "," + user.getDescription() + "\n");
//
//                // Write Friends (if any)
//                List<User> friends = user.getFriends();
//                if (friends != null && !friends.isEmpty()) {
//                    writer.write("Friends for " + user.getUsername() + ": ");
//                    writer.write(String.join(",", friends.stream().map(User::getUsername).toList()) + "\n");
//                }
//
//                // Write Blocked Users (if any)
//                List<User> blockedUsers = user.getBlockedUsers();
//                if (blockedUsers != null ) {
//                    writer.write("Blocked users for " + user.getUsername() + ": ");
//                    writer.write(String.join(",", blockedUsers.stream().map(User::getUsername).toList()) + "\n");
//                }
//            }
//
//            // Write Posts
//            writer.write("POSTS\n");
//            for (Post post : posts.values()) {
//                // Write post ID, content, author, upvotes, and downvotes
//                writer.write(post.getId() + "," + post.getContent() + "," + post.getAuthor().getUsername() + ","
//                        + post.getUpVotes() + "," + post.getDownVotes() + "\n");
//                System.out.println("Post created with ID: " + post.getId() + " Upvotes: " + post.getUpVotes() + " Downvotes: " + post.getDownVotes()); // Debugging line
//            }
//            // Write Comments
//            writer.write("COMMENTS\n");
//            for (Comment comment : comments.values()) {
//                // Include the postId along with other comment data
//                writer.write(comment.getID() + "," + comment.getContent() + "," + comment.getAuthor().getUsername() + ","
//                        + comment.getUpVotes() + "," + comment.getDownVotes() + "," + comment.getPostID() + "\n");
//            }
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    // Method to read the database from a file (synchronized for thread safety)
//    public void readDatabaseFromFile(String filePath) {
//        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
//            String line;
//            String currentSection = "";  // Will track the current section (USERS, POSTS, COMMENTS)
//
//            while ((line = br.readLine()) != null) {
//                // Handle section headers
//                if (line.equals("USERS")) {
//                    currentSection = "USERS";
//                } else if (line.equals("POSTS")) {
//                    currentSection = "POSTS";
//                } else if (line.equals("COMMENTS")) {
//                    currentSection = "COMMENTS";
//                } else {
//                    // Handle data in each section
//                    switch (currentSection) {
//                        case "USERS":
//                            // Process user data (4 values)
//                            if (!line.startsWith("Friends for") && !line.startsWith("Blocked users for")) {
//                                String[] userParts = line.split(",");
//                                if (userParts.length == 4) {
//                                    // Create user and add to the database
//                                    User user = new User(userParts[0], userParts[1], userParts[2], userParts[3]);
//                                    this.addUser(user);
//                                    System.out.println("User added: " + user.getUsername());
//                                } else {
//                                    System.err.println("Skipping invalid user line: " + line);
//                                }
//                            } else {
//                                // Handle friends and blocked users for the current user
//                                String username = line.split(":")[0].split(" ")[2];  // Extract the username
//                                User currentUser = this.getUserByUsername(username);
//
//                                if (currentUser != null) {
//                                    if (line.startsWith("Friends for")) {
//                                        // Process friends
//                                        String friendsLine = line.substring("Friends for ".length());
//                                        String[] friends = friendsLine.split(",");
//                                        for (String friendUsername : friends) {
//                                            User friend = this.getUserByUsername(friendUsername);
//                                            if (friend != null) {
//                                                currentUser.addFriend(friend);
//                                                System.out.println(currentUser.getUsername() + " added friend: " + friend.getUsername());
//                                            } else {
//                                                System.err.println("Friend with username " + friendUsername + " not found.");
//                                            }
//                                        }
//                                    } else if (line.startsWith("Blocked users for")) {
//                                        // Process blocked users
//                                        String blockedLine = line.substring("Blocked users for ".length());
//                                        String[] blockedUsers = blockedLine.split(",");
//                                        for (String blockedUsername : blockedUsers) {
//                                            User blockedUser = this.getUserByUsername(blockedUsername);
//                                            if (blockedUser != null) {
//                                                currentUser.blockUser(blockedUser);
//                                                System.out.println(currentUser.getUsername() + " blocked user: " + blockedUser.getUsername());
//                                            } else {
//                                                System.err.println("Blocked user with username " + blockedUsername + " not found.");
//                                            }
//                                        }
//                                    }
//                                } else {
//                                    System.err.println("User with username " + username + " not found for friends/blocked users.");
//                                }
//                            }
//                            break;
//
//                        case "POSTS":
//                            // Parse post data (5 values)
//                            String[] postParts = line.split(",");
//                            if (postParts.length == 5) {
//                                String postId = postParts[0];
//                                String content = postParts[1];
//                                String authorUsername = postParts[2];
//                                int upVotes = Integer.parseInt(postParts[3]);
//                                int downVotes = Integer.parseInt(postParts[4]);
//
//                                User author = this.getUserByUsername(authorUsername);
//                                if (author != null) {
//                                    Post post = new Post(postId, content, author);
//                                    post.setUpVotes(upVotes);
//                                    post.setDownVotes(downVotes);
//                                    this.addPost(post);
//                                    System.out.println("Post created with ID: " + postId + " Content: " + content);
//                                } else {
//                                    System.err.println("Post author " + authorUsername + " not found.");
//                                }
//                            } else {
//                                System.err.println("Skipping invalid post line: " + line);
//                            }
//                            break;
//
//                        case "COMMENTS":
//                            // Parse comment data (6 values)
//                            String[] commentParts = line.split(",");
//                            if (commentParts.length == 6) {
//                                String commentId = commentParts[0];
//                                String commentContent = commentParts[1];
//                                String authorUsername = commentParts[2];
//                                int upVotes = Integer.parseInt(commentParts[3]);
//                                int downVotes = Integer.parseInt(commentParts[4]);
//                                String postId = commentParts[5];
//
//                                // Find the post for this comment
//                                Post post = this.getPostById(postId);
//                                if (post != null) {
//                                    User author = this.getUserByUsername(authorUsername);
//                                    if (author != null) {
//                                        Comment comment = new Comment(commentContent, author, commentId);
//                                        comment.setUpvotes(upVotes);
//                                        comment.setDownvotes(downVotes);
//                                        this.addCommentToPost(postId, commentContent, author);
//                                        System.out.println("Added comment to post with ID: " + postId);
//                                    } else {
//                                        System.err.println("Comment author " + authorUsername + " not found.");
//                                    }
//                                } else {
//                                    System.err.println("Post with ID " + postId + " not found for comment.");
//                                }
//                            } else {
//                                System.err.println("Skipping invalid comment line: " + line);
//                            }
//                            break;
//                    }
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }




}
