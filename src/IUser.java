package src;

import java.util.List;

public interface IUser {
    String getUsername();
    String getPassword();
    String getName();
    List<User> getFriends();
    List<User> getBlockedUsers();
    List<Post> getPosts();
    void addFriend(User friend);
    void removeFriend(User friend);
    void blockUser(User user);
    void unblockUser(User user);
    void addPost(Post post);
    boolean deletePost(Post post);
    void setDescription(String description);
    String getDescription();
    void setFriends(List<User> friends);
    boolean equals(Object o);
}
