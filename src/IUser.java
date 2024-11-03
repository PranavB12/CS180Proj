package src;

import src.Picture;

// IUser Interface
public interface IUser {
    void createAccount(String username, String password, Picture profilePicture);
    void deleteAccount();
    boolean login(String username, String password);
    void searchUser(String username, String name);
    void addFriend(String username, String name);
    void removeFriend(String username, String name);
    void blockUser(String username, String name);
    void viewUser();
    void createPost(String content, String description, Picture postPicture);
    void deletePost(int postId);
}
