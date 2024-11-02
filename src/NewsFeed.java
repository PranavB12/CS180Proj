import java.util.*;
public class NewsFeed {
    private List<Post> posts;
    // Deleting a Post
   
    public void displayPosts(User user) {
        List<User> friends = user.getFriends();
        for (int i = 0; i < friends.size(); i++) {
            User friend = friends.get(i);
            List<User> friendPosts = friend.getPosts();
            for (int j = 0; j < friendPosts.size(); j++) {
                posts.add(friendPosts.get(j));
            }
        }
        Collections.shuffle(posts);
        for (int k = 0; k < posts.size(); k++) {
            System.out.println(posts.get(k));
        }
    }
}
