package src;

import java.util.ArrayList;
import java.util.List;

/**
 * Group Project - CS18000 Gold
 *
 * User class, where User's data is managed
 *
 * @author Pranav Bansal, Vivaan Malhotra, Rishi Rao, Mike Lee, Vaishnavi Sharma, lab sec 37
 *
 * @version November 3, 2024
 *
 */

public class User extends Post implements IUser {

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
    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }


    public void deletePost() {

    }
    public String toString() {
        String a = "Username: " + username + "\n" + "Password: " + password + "\n" + "Name: "  + name;
        return a;
    }
    public boolean equals(Object o) {
        if(this == o) {
            return true;
        }
        if (!(o instanceof User)) {
            return false;
        }
        User other = (User) o;
        return this.username != null && this.username.equals(other.username);
    }
}
