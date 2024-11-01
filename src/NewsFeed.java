import java.util.*;
public class NewsFeed {
    private ArrayList<Post> posts;

    public void createPost(String content, Picture picture) {
        Post post = new Post(content, picture);
        post.setId(posts.size() + 1);
        posts.add(post);
        System.out.println("Post created with ID: " + post.getId());
    }

    // Deleting a Post
    public void deletePost(int postId) throws PostException {
        boolean removed = posts.removeIf(post -> post.getId() == postId);
        if (!removed) {
            throw new PostException("Post with ID " + postId + " not found.");
        }
        System.out.println("Post with ID " + postId + " deleted.");
    }
    public void displayPosts(User user) {
        ArrayList<User> friends = user.getFriends();
        for (int i = 0; i < friends.size(); i++) {
            User friend = friends.get(i);
            ArrayList<Post> friendPosts= friend.getPosts();
            for (int j = 0; j < friendPosts.size(); j++) {
                posts.add(friendPosts.get(j));
            }
        }
        Collections.shuffle(posts);
        for (int k = 0; k < posts.size(); k++) {
            System.out.println(posts.get(k));
        }
    }
    public void upvote(Post post) {
        int votes = post.getVotes();
        votes++;
    }
    public void downvote(Post post) {
        int votes = post.getVotes();
        votes--;
    }

}
