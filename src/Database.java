

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
    private static final Object commentsLock = new Object();
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
            return false;
        }
        synchronized (usersLock) {
            users.add(user);
        }
        System.out.println("User added: " + user.getUsername());
        return true;
    }
    public void setPosts(Map<String, Post> posts) {
        this.posts = posts;
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
        List<Post> p = author.getPosts();
        p.add(post);
        author.setPosts(p);
        updateUserInDatabase(author);


        // Update the user in the users list


        // Add the post to the posts map
        synchronized (postsLock) {
            posts.put(postId, post);
            this.setPosts(posts);
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
        user = getUserByUsername(user.getUsername());

        List<User> a = user.getBlockedUsers();
        a.remove(friend);
        user.setBlockedUsers(a);
        updateUserInDatabase(user);



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
    public ArrayList<String> displayPosts(String username) {
        User user = this.getUserByUsername(username);
        ArrayList<String> lines = new ArrayList<String>();
        ArrayList<Post> posts = new ArrayList<Post>();
        List<User> friends = user.getFriends();  // Changed to Set<User>



        // Collect posts from each friend
        for (User friend : friends) {
            friend = getUserByUsername(friend.getUsername());
            List<Post> friendPosts = friend.getPosts();
            posts.addAll(friendPosts);
        }

        // Shuffle the posts for random order display

        lines.add("---------------------------------------------------------------------------------------------------------");

        // Display each post
        for (Post post : posts) {
            post = getPostById(post.getId());
            if (post.isHidden()) {
                continue;
            }


            lines.add("Posted by: " + post.getAuthor().getUsername());
            lines.add(post.getContent());
            lines.add("Upvotes: "+ post.getUpVotes());
            lines.add("Downvotes: "+ post.getDownVotes());
            lines.add(post.getId());

            Map<String, Comment> comments = post.getComments();
            if (comments != null && !comments.isEmpty()) {
                lines.add("     Comments: ");
                for (Map.Entry<String, Comment> entry : comments.entrySet()) {

                    Comment comment = entry.getValue(); // Retrieve the Comment object
                    lines.add(comment.getContent());
                    lines.add(String.valueOf(comment.getUpVotes()));
                    lines.add(String.valueOf(comment.getDownVotes()));
                    lines.add(comment.getAuthor().getUsername());
                    lines.add(comment.getID());
                }
                lines.add("---");
            }

            lines.add("---------------------------------------------------------------------------------------------------------");



        }

        return lines;
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
    public void upvoteComment(String commentID, User requestedUser) {
        Comment comment;
        synchronized (postsLock) {
            comment = getCommentById(commentID);
        }

        if (comment != null) {
            try {
                comment.upvote(requestedUser.getUsername());
                synchronized (postsLock) {
                    Post post = getPostById(comment.getPostID());

                    Map<String, Comment> commentss = post.getComments();
                    if (commentss.containsKey(commentID)) {
                        commentss.put(commentID, comment); // Replace the comment with the new one
                    }
                    post.setComments(commentss);
                    if (posts.containsKey(post.getId())) {
                        posts.put(post.getId(), post); // Replace the comment with the new one
                    }

                    comments.put(comment.getID(), comment);
                }
                System.out.println("Comment with ID " + commentID + " upvoted by " + requestedUser.getUsername() + ".");
            } catch (IllegalStateException e) {
                System.out.println(e.getMessage());
            }
        } else {
            System.out.println("Comment with ID " + commentID + " not found.");
        }
    }
    public void downvoteComment(String commentID, User requestedUser) {
        Comment comment;
        synchronized (postsLock) {
            comment = getCommentById(commentID);
        }

        if (comment != null) {
            try {
                comment.downvote(requestedUser.getUsername());
                synchronized (postsLock) {
                    Post post = getPostById(comment.getPostID());

                    Map<String, Comment> commentss = post.getComments();
                    if (commentss.containsKey(commentID)) {
                        commentss.put(commentID, comment); // Replace the comment with the new one
                    }
                    post.setComments(commentss);
                    if (posts.containsKey(post.getId())) {
                        posts.put(post.getId(), post); // Replace the comment with the new one
                    }
                    comments.put(comment.getID(), comment);
                }
                System.out.println("Post with ID " + commentID + " downvoted by " + requestedUser.getUsername() + ".");
            } catch (IllegalStateException e) {
                System.out.println(e.getMessage());
            }
        } else {
            System.out.println("Post with ID " + commentID + " not found.");
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



    public String addCommentToPost(String postId, String comment, User commentAuthor) {
        Post post;
        synchronized (postsLock) {
            post = posts.get(postId);
        }

        if (post != null) {
            Comment com = new Comment(comment, commentAuthor, postId);
            String commentId = com.getID();
            comments.put(commentId, com);
            post.addComment(commentId, com);
            synchronized (postsLock) {
                posts.put(post.getId(), post);
            }
            return commentId; // Return generated comment ID
        } else {
            System.out.println("Post with ID " + postId + " not found.");
            return null;
        }
    }


    // Delete a comment from a post
    public String deleteCommentFromPost(String postId, String commentId, User requestedUser) {
        Post post;
        synchronized (postsLock) {
            post = posts.get(postId);
        }

        if (post != null) {
            Comment comment = this.getCommentById(commentId);

            if (comment == null) {
                return ("Comment with ID " + commentId + " not found.");
            }

            // Verify that the requested user is the author of the comment
            if (!(comment.getAuthor().equals(requestedUser) || post.getAuthor().equals(requestedUser))) {
                return ("User " + requestedUser.getUsername() + " is not authorized to delete this comment.");
            }

            synchronized (postsLock) {
                comments.remove(commentId);
                post.deleteComment(commentId, requestedUser);
                posts.put(post.getId(), post);
            }

            return ("Comment deleted from the post.");
        } else {
            return ("Post with ID " + postId + " not found.");
        }
    }

    // Unhide a post
    public void unhidePost(String postId, User requestedUser) {
        Post post;
        synchronized (postsLock) {
            post = posts.get(postId);
        }

        if (post != null) {
            if (!post.getAuthor().equals(requestedUser)) {
                System.out.println("User " + requestedUser.getUsername() + " is not authorized to unhide this post.");
                return;
            }

            post.unhidePost();

            synchronized (postsLock) {
                posts.put(post.getId(), post);
            }

            System.out.println("Post with ID " + postId + " has been unhidden by the author.");
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
    public String enableCommentsForPost(String postId, User requestedUser) {
        Post post;
        synchronized (postsLock) {
            post = posts.get(postId);
        }

        if (post != null) {
            // Verify that the requested user is the author of the post
            if (!post.getAuthor().equals(requestedUser)) {
                return ("User " + requestedUser.getUsername() + " is not authorized to enable comments for this post.");
            }
            if (post.areCommentsEnabled()) {
                return "Comments already enabled.";
            }

            post.enableComments();
            synchronized (postsLock) {
                posts.put(postId, post);
            }

            return ("Comments enabled for post.");
        } else {
            return ("Post with ID " + postId + " not found.");
        }
    }

    // Disable comments for a post
    public String disableCommentsForPost(String postId, User requestedUser) {
        Post post;
        synchronized (postsLock) {
            post = posts.get(postId);
        }

        if (post != null) {
            // Verify that the requested user is the author of the post
            if (!post.getAuthor().equals(requestedUser)) {
                return ("User " + requestedUser.getUsername() + " is not authorized to disable comments for this post.");
            }

            Set<String> commentsIDs = post.getComments().keySet();
            synchronized (commentsLock) {
                for (String commentId : commentsIDs) {
                    comments.remove(commentId);  // Remove each comment from the map
                }
            }




            post.disableComments();
            synchronized (postsLock) {
                posts.put(postId, post);
            }

            return ("Comments disabled for post.");
        } else {
            return ("Post with ID " + postId + " not found.");
        }
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
    public Comment getCommentById(String commentId) {
        // Check if the posts map contains the postId
        if (comments.containsKey(commentId)) {
            return comments.get(commentId); // Return the post with the specified postId
        } else {
            // If postId is not found, return null or handle as needed
            System.err.println("Post with ID " + commentId + " not found.");
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
    public synchronized void writeDatabaseToFile(String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            // Write Users
            writer.write("USERS\n");

            for (User user : users) {
                writer.write(user.getUsername() + "," + user.getPassword() + "," + user.getName() + "," + user.getDescription() + "\n");

                // Write Friends (if any)
                List<User> friends = user.getFriends();
                if (friends != null ) {
                    writer.write("Friends for - " + user.getUsername() + ": ");
                    writer.write(String.join(",", friends.stream().map(User::getUsername).toList()) + "\n");
                }

                // Write Blocked Users (if any)
                List<User> blockedUsers = user.getBlockedUsers();
                if (blockedUsers != null) {
                    writer.write("Blocked users for - " + user.getUsername() + ": ");
                    writer.write(String.join(",", blockedUsers.stream().map(User::getUsername).toList()) + "\n");
                }


            }

            // Write Posts
            writer.write("POSTS\n");
            for (Post post : posts.values()) {
                // Write post ID, content, author, upvotes, and downvotes
                writer.write(post.getId() + "," + post.getContent() + "," + post.getAuthor().getUsername() + ","
                        + post.getUpVotes() + "," + post.getDownVotes() + "," + post.areCommentsEnabled() + "," + post.isHidden() + "\n");
                System.out.println("Post created with ID: " + post.getId() + " Upvotes: " + post.getUpVotes() + " Downvotes: " + post.getDownVotes()); // Debugging line
            }
            // Write Comments
            writer.write("COMMENTS\n");
            for (Comment comment : comments.values()) {
                // Include the postId along with other comment data
                writer.write(comment.getID() + "," + comment.getContent() + "," + comment.getAuthor().getUsername() + ","
                        + comment.getUpVotes() + "," + comment.getDownVotes() + "," + comment.getPostID() + "\n");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void readDatabaseFromFile(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            String currentTag = "";
            List<String> lines = new ArrayList<>();  // To store all lines from the file
            List<String> friendsList = new ArrayList<>();
            List<String> blockedList = new ArrayList<>();

            while ((line = reader.readLine()) != null) {
                lines.add(line.trim());  // Store each line into the list
            }

            // Loop through the stored lines
            for (int i = 0; i < lines.size(); i++) {
                line = lines.get(i);

                if (line.equals("USERS")) {
                    currentTag = "USERS";
                    continue;
                } else if (line.equals("POSTS")) {
                    currentTag = "POSTS";
                    continue;
                } else if (line.equals("COMMENTS")) {
                    currentTag = "COMMENTS";
                    continue;
                }
                int s = lines.indexOf("POSTS");

                switch (currentTag) {
                    case "USERS":
                        // Check for user details (every third line after encountering USERS)
                        for (int a = 1; a < s; a+=3) {
                            String[] userDetails = lines.get(a).split(",");
                            if (userDetails.length >= 4) {
                                String username = userDetails[0].trim();
                                String password = userDetails[1].trim();
                                String name = userDetails[2].trim();
                                String description = userDetails[3].trim();

                                this.addUser(new User(username, password, name, description));

                                // Parse the next lines for friends and blocked users
                                if (a + 1 < lines.size() && lines.get(a+ 1).startsWith("Friends for")) {
                                    String[] friends = lines.get(a + 1).split(":");//[1].trim().split(", ");
                                    if (friends.length > 1 ) {
                                        friends = friends[1].trim().split(", ");
                                        friendsList.addAll(Arrays.asList(friends));
                                    }

                                }
                                if (a + 2 < lines.size() && lines.get(a + 2).startsWith("Blocked users for")) {
                                    String[] blockedUsers = lines.get(a + 2).split(":");//.trim().split(", ");
                                    if (blockedUsers.length  > 1) {
                                        blockedUsers = blockedUsers[1].trim().split(", ");
                                        blockedList.addAll(Arrays.asList(blockedUsers));
                                    }

                                }

                                // Add friends and blocked users after adding the user
                                User user = this.getUserByUsername(username);
                                if (user != null) {
                                    for (String friend : friendsList) {
                                        if (!friend.isEmpty()) {
                                            User friendUser = this.getUserByUsername(friend);
                                            if (friendUser != null) {
                                                this.addUser(friendUser);
                                                this.addFriend(user, friendUser);
                                            }
                                        }
                                    }
                                    for (String blockedUser : blockedList) {
                                        if (!blockedUser.isEmpty()) {
                                            User blocked = this.getUserByUsername(blockedUser);
                                            if (blocked != null) {
                                                this.blockUser(user, blocked);
                                            }
                                        }
                                    }
                                }

                                // Clear lists for the next user
                                friendsList.clear();
                                blockedList.clear();
                            }
                        }
                        break;

                    case "POSTS":
                        // Parse posts
                        String[] postDetails = line.split(",");
                        if (postDetails.length == 7) {  // Make sure there are enough elements for a post
                            String postId = postDetails[0].trim();
                            String content = postDetails[1].trim();
                            String authorUsername = postDetails[2].trim();
                            int upVotes = Integer.parseInt(postDetails[3].trim());
                            int downVotes = Integer.parseInt(postDetails[4].trim());
                            boolean commentsEnabled = Boolean.valueOf(postDetails[5].trim());
                            boolean isHidden = Boolean.valueOf(postDetails[6].trim());

                            User postAuthor = this.getUserByUsername(authorUsername);
                            List<Post> a = postAuthor.getPosts();
                            boolean exists = false;
                            for (int y = 0; y < a.size(); y++) {
                                String f = a.get(y).getId();
                                if (f.equals(postId)) {
                                    exists = true;
                                }
                            }
                            if (exists) {
                                continue;
                            }
                            a.add(new Post(postId, content, postAuthor, upVotes, downVotes, commentsEnabled, isHidden));
                            postAuthor.setPosts(a);
                            if (postAuthor != null) {
                                this.addPost(new Post(postId, content, postAuthor, upVotes, downVotes, commentsEnabled, isHidden));
                            }
                        }
                        break;

                    case "COMMENTS":
                        // Parse comments
                        String[] commentDetails = line.split(",");
                        if (commentDetails.length == 6) {  // Make sure there are enough elements for a comment
                            String commentId = commentDetails[0].trim();
                            String commentContent = commentDetails[1].trim();
                            String commentAuthorUsername = commentDetails[2].trim();
                            int commentUpVotes = Integer.parseInt(commentDetails[3].trim());
                            int commentDownVotes = Integer.parseInt(commentDetails[4].trim());
                            String postIdForComment = commentDetails[5].trim();


                            User commentAuthor = this.getUserByUsername(commentAuthorUsername);
                            Post parentPost = this.getPostById(postIdForComment);
                            Map<String, Comment> comments = parentPost.getComments();

                            comments.put(commentId, new Comment(commentContent, commentAuthor, postIdForComment, commentId, commentUpVotes, commentDownVotes));
                            parentPost.setComments(comments);
                            this.comments.put(commentId, new Comment(commentContent, commentAuthor, postIdForComment, commentId, commentUpVotes, commentDownVotes));
                            synchronized (postsLock) {
                                posts.put(postIdForComment, parentPost);
                            }


                        }
                        break;

                    default:
                        break;
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading the database file: " + e.getMessage());
        }
    }
}
