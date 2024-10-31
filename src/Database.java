package src;

import java.util.*;

public class Database implements IDatabase {

    // initialize maps that contain the uses and posts
    private final Map<String, User> users;
    private final Map<Integer, Post> posts;

    // temporary file path for downloading the necessary data from the server
    private final String dataFilePath = "data/database.json";
    // server url
    private final String dataDumpUrl = "";
    // counter to assign IDs to posts
    private int postIdCounter;

    // Constructor
    public Database() {
        // will have a method call to fetch the data from the server and then parse it into object

    }

    // database will use sync methods to keep network updated

    @Override
    public boolean addUser(User user) {
        // integrity check
        // http post to make sure server stays updated
        // add user to local map
        return false;
    }

    @Override
    public boolean removeUser(User user) {
        // integrity check
        // http post to make sure server stays updated
        // remove user from local map
        return false;
    }

    @Override
    public boolean validateCredentials(String username, String password) {
        // check validity of credentials
        return false;
    }

    @Override
    public boolean userExists(String username) {
        // check if username already exists
        return false;
    }

    @Override
    public boolean addFriend(User user, String friendUsername) {
        // integrity check
        // add request senders username to friendUsername's pending friend requests
        return false;
    }

    @Override
    public boolean removeFriend(User user, String friendUsername) {
        // integrity check
        // Remove friendUsername from Users friends list

        return false;
    }

    @Override
    public boolean blockUser(User user, String blockedUsername) {
        // integrity check
        // add blockedUsername to Users blocked user list
        return false;
    }

    @Override
    public boolean unblockUser(User user, String blockedUsername) {
        // integrity check
        // remove blockedUsername from Users blocked user list
        return false;
    }

    @Override
    public List<String> getFriendsList(User user) {
        // return list of string reference for friend usernames
        return null;
    }

    @Override
    public List<String> getBlockedList(User user) {
        // return list of string reference for blocked usernames
        return null;
    }

    @Override
    public int addPost(Post post) {
        // add post to database and return an ID which is stored in the user object.
        return 0;
    }

    @Override
    public boolean deletePost(int postID) {
        // integrity check to make sure postID is posted by User
        return false;
    }

    @Override
    public boolean enableComments(int postID) {
        // integrity check to make sure postID is posted by User
        // Set comments to true in post
        return false;
    }

    @Override
    public boolean disableComments(int postID) {
        // integrity check to make sure postID is posted by User
        // Set comments to false in post
        return false;
    }

    @Override
    public List<Integer> getUserPosts(String username) {
        // return a list of postIDs for parameter Username
        return null;
    }

}
