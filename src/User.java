package src;

import src.Picture;
import src.Post;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class User extends Post {

    private String username;
    private String password;
    private String name;
    private String description;

    private Picture profilePicture;
    private Set<User> friends;          // Changed to Set to avoid duplicates
    private Set<User> blockedUsers;      // Changed to Set to avoid duplicates
    private List<Post> posts;

    // Constructors
    public User(String username, String password, Picture profilePicture, String description, String name) {
        this();
        this.username = username;
        this.password = password;
        this.profilePicture = profilePicture;   // Initialize profile picture correctly
        this.description = description;
        this.name = name;
    }

    public User(String username, String password, String name) {
        this(username, password, null, "", name);
    }

    public User() {
        this.friends = new HashSet<>();         // Use HashSet to prevent duplicate friends
        this.blockedUsers = new HashSet<>();    // Use HashSet to prevent duplicate blocked users
        this.posts = new ArrayList<>();
    }

    // Getters and Setters
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Picture getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(Picture profilePicture) {
        this.profilePicture = profilePicture;
    }

    // Friend management
    public Set<User> getFriends() {
        return this.friends;
    }

    public boolean addFriend(User friend) {
        return this.friends.add(friend); // Returns false if friend is already in the set
    }

    public boolean removeFriend(User friend) {
        return this.friends.remove(friend);
    }

    // Blocked user management
    public Set<User> getBlockedUsers() {
        return this.blockedUsers;
    }

    public boolean blockUser(User user) {
        return this.blockedUsers.add(user); // Returns false if user is already blocked
    }

    public boolean unblockUser(User user) {
        return this.blockedUsers.remove(user);
    }

    // Post management
    public List<Post> getPosts() {
        return this.posts;
    }

    public void addPost(Post post) {
        this.posts.add(post);
    }

    public void deletePost(Post post) {
        this.posts.remove(post);
    }
}
