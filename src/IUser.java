package src;

import src.Picture;

/**
 * Group Project - CS18000 Gold
 *
 * additional interfaces
 *
 * @author Pranav Bansal, Vivaan Malhotra, Rishi Rao, Mike Lee, Vaishnavi Sharma, lab sec 37
 *
 * @version November 3, 2024
 *
 */

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
