
package src;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Group Project - CS18000 Gold
 *
 * User class
 *
 * @author Pranav Bansal, Vivaan Malhotra, Rishi Rao, Mike Lee, Vaishnavi Sharma, lab sec 37
 *
 * @version November 19, 2024
 *
 */

public class User implements  IUser {
    private final String username;
    private String password;
    private final String name;
    private List<User> friends = new ArrayList<>();
    private List<User> blockedUsers = new ArrayList<>();
    private List<Post> posts = Collections.synchronizedList(new ArrayList<>());
    private String description;

    public User(String username, String password, String name, String description) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.description = description;
    }
    public User(String username, String password, String name) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.description = "";
    }



    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public List<User> getFriends() {
        synchronized (friends) {
            return new ArrayList<>(friends); // Return a copy to avoid exposing internal list
        }
    }

    public List<User> getBlockedUsers() {
        synchronized (blockedUsers) {
            return new ArrayList<>(blockedUsers); // Return a copy to avoid exposing internal list
        }
    }

    public List<Post> getPosts() {
        synchronized (posts) {
            return new ArrayList<>(posts); // Return a copy to avoid exposing internal list
        }
    }

    public void addFriend(User friend) {
        synchronized (friends) {
            if (!friends.contains(friend)) {
                friends.add(friend);
            }
        }
    }

    public void removeFriend(User friend) {
        synchronized (friends) {
            friends.remove(friend);
        }
    }

    public void blockUser(User user) {
        synchronized (blockedUsers) {
            if (!blockedUsers.contains(user)) {
                blockedUsers.add(user);
            }
        }
        removeFriend(user); // Automatically removes from friends if blocking
    }

    public void unblockUser(User user) {
        synchronized (blockedUsers) {
            blockedUsers.remove(user);
        }
    }

    public void addPost(Post post) {
        synchronized (posts) {
            if (!posts.contains(post)) {
                posts.add(post);
            }
        }
    }
    public void setDescription(String des) {
        this.description = des;
    }

    public String getDescription() {
        return this.description;
    }
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", name='" + name + '\'' +
                ", friends=" + friends.stream().map(User::getUsername).toList() +
                ", blockedUsers=" + blockedUsers.stream().map(User::getUsername).toList() +
                ", description='" + description + '\'' +
                '}';
    }
    public void setFriends(List<User> friends) {
        this.friends = friends;
    }
    public void setPosts(List<Post> posts) {
        this.posts = posts;
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
