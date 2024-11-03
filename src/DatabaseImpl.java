package src;

import java.util.List;
import java.util.Map;

public class DatabaseImpl extends Database {

    public DatabaseImpl(List<User> users, Map<Integer, Post> posts) {
        super(users, posts);
    }

    @Override
    public boolean addFriend(User user, String friendUsername) {
        return false;
    }
    @Override
    public boolean removeFriend(User user, String friendUsername) {
        return false;
    }

    @Override
    public boolean blockUser(User user, String blockedUsername) {
        return false;
    }

    @Override
    public boolean unblockUser(User user, String blockedUsername) {
        return false;
    }

    @Override
    public List<String> getFriendsList(User user) {
        return List.of();
    }

    @Override
    public List<String> getBlockedList(User user) {
        return List.of();
    }

    @Override
    public int addPost(Post post) {
        return 0;
    }

    @Override
    public boolean enableComments(int postID) {
        return false;
    }

    @Override
    public boolean disableComments(int postID) {
        return false;
    }

    @Override
    public List<Integer> getUserPosts(User user) {
        return List.of();
    }
}
