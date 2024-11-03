package src;

import src.Picture;
import src.Post;

import java.util.ArrayList;
import java.util.List;
/*

 */

public class User extends Post {
    private String username;
    private String password;
    private String name;
    private String description;


    private Picture profilePicture;
    private List<User> friends;
    private List<User> blockedUsers;
    private List<Post> posts;

    // Constructors
    public User(String username, String password, Picture profilePicture, String description, String name) {
        this();
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }




    public List<User> getFriends() {
        return this.friends;
    }
    public List<User> getBlockedUsers() {
        return this.blockedUsers;
    }
    public List<Post> getPosts() {
        return this.posts;
    }


    public void deletePost() {

    }
}
