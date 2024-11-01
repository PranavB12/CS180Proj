import java.util.ArrayList;
import java.util.List;
/*

 */

public class User {
    private String username;
    private String password;
    private String name;
    private String content;
    private String description;
    private Picture postPicture;
    private int postId;
    private Picture profilePicture;
    private List<User> friends;
    private List<User> blockedUsers;
    private List<Post> posts;

    // Constructors
    public User(String username, String password, Picture profilePicture, String description, String name) {
        this.username = username;
        this.password = password;
        this.profilePicture = profilePicture;
        this.description = description;
        this.name = name;
        this.friends = new ArrayList<>();
        this.blockedUsers = new ArrayList<>();
        this.posts = new ArrayList<>();
    }

    public User(String username, String password, String name) {
        this(username, password, null, "", name);
    }

    public User() {
        this.friends = new ArrayList<>();
        this.blockedUsers = new ArrayList<>();
        this.posts = new ArrayList<>();
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public Picture getPostPicture() {
        return postPicture;
    }
    public void setPostPicture(Picture postPicture) {
        this.postPicture = postPicture;
    }

    public int getPostId() {
        return postId;
    }
    public void setPostId(int postId) {
        this.postId = postId;
    }

    // Account Creation
    public void createAccount(String username, String password, String name, Picture profilePicture) {
        if (username == null || password == null || name == null) {
            throw new IllegalArgumentException("Username, password, and name cannot be null.");
        }
        this.username = username;
        this.password = password;
        this.name = name;
        this.profilePicture = profilePicture;
        System.out.println("Account created for user: " + username);
    }

    // Account Deletion
    public void deleteAccount() {
        this.username = null;
        this.password = null;
        this.name = null;
        this.profilePicture = null;
        this.description = null;
        this.posts.clear();
        this.friends.clear();
        this.blockedUsers.clear();

        System.out.println("Account deleted.");
    }

    // Protected Password Login
    public boolean login(String username, String password) throws AuthenticationException {
        if (this.username == null) {
            throw new AuthenticationException("Account not found.");
        }
        if (this.username.equals(username) && this.password.equals(password)) {
            System.out.println("Login was successful.");
            return true;
        } else {
            throw new AuthenticationException("Login failed. Incorrect username or password.");
        }
    }

    // User Search
    public boolean searchUser(String username) throws UserNotFoundException {
        if (this.username == null || !this.username.equals(username)) {
            throw new UserNotFoundException("User with username " + username + " not found.");
        }
        return true;
    }

    // Adding Friends
    public void addFriend(User friend) throws FriendException {
        if (this.blockedUsers.contains(friend)) {
            throw new FriendException("Cannot add user as friend. User is blocked.");
        }
        if (this.friends.contains(friend)) {
            throw new FriendException("User is already a friend.");
        }
        this.friends.add(friend);
        System.out.println(friend.getUsername() + " added as a friend.");
    }

    // Removing Friends
    public void removeFriend(User friend) throws FriendException {
        if (!this.friends.remove(friend)) {
            throw new FriendException("User is not in friends list.");
        }
        System.out.println(friend.getUsername() + " removed from friends.");
    }

    // Blocking Friends
    public void blockUser(User user) throws FriendException {
        if (this.blockedUsers.contains(user)) {
            throw new FriendException("User is already blocked.");
        }
        this.blockedUsers.add(user);
        this.friends.remove(user);
        System.out.println(user.getUsername() + " is blocked.");
    }

    // User Viewer (list all posts made by the user)
    public void viewUser() {
        System.out.println("User: " + this.username);
        System.out.println("Name: " + this.name);
        System.out.println("Description: " + this.description);
        System.out.println("Posts:");
        for (Post post : posts) {
            System.out.println(" - " + post.getContent());
        }
    }

    // Creating a Post
    public void createPost(String content, Picture picture) {
        Post post = new Post(content, picture);
        post.setId(posts.size() + 1);
        posts.add(post);
        System.out.println("Post created with ID: " + post.getId());
    }

    // Deleting a Post
    public void deletePost(int postId) throws PostException {
        boolean removed = posts.removeIf(post -> post.getId() == postId);
        if (!removed) {
            throw new PostException("Post with ID " + postId + " not found.");
        }
        System.out.println("Post with ID " + postId + " deleted.");
    }

    // Picture (for illustration)

    public static class Picture {
        private String url;

        public Picture(String url) {
            this.url = url;
        }

        public String getUrl() {
            return url;
        }
    }
}
