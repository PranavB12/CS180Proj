package src;

import java.util.HashMap;
import java.util.Map;

public class Post implements IPost {
    public User author;
    private static int id;
    private String content;
    private Picture picture;
    private boolean hidden;
    private boolean commentsEnabled;
    private int upVotes;
    private int downVotes;
    private Map<Integer, String> comments; //
    private static Object obj = new Object();// Stores comments with unique IDs

    // Static fields for total upvotes and downvotes (as per IPost)
    public static int totalUpVotes;
    public static int totalDownVotes;

    // Constructor
    public Post(String content, User user) {
        this.author = user;
        this.content = content;
        this.hidden = false;
        this.commentsEnabled = true;
        this.upVotes = 0;
        this.downVotes = 0;
        this.comments = new HashMap<>();
    }

    public Post() {
    }

    // IPost Interface Methods

    @Override
    public void upvote() {
        synchronized (obj) {
            totalUpVotes++;
        }
        System.out.println("Post upvoted. Total upvotes: " + totalUpVotes);
    }

    @Override
    public void downvote() {
        synchronized (obj) {
            totalDownVotes++;
        }
        System.out.println("Post downvoted. Total downvotes: " + totalDownVotes);
    }

    
    @Override
    public void addComment(String comment) {
        if (!commentsEnabled) {
            System.out.println("Comments are disabled for this post.");
            return;
        }
        int commentId = comments.size() + 1;
        comments.put(commentId, comment);
        System.out.println("Comment added with ID: " + commentId);
    }

    @Override
    public void deleteComment(int commentId) {
        if (comments.remove(commentId) != null) {
            System.out.println("Comment with ID " + commentId + " deleted.");
        } else {
            System.out.println("Comment with ID " + commentId + " not found.");
        }
    }

    @Override
    public void hidePost() {
        this.hidden = true;
        System.out.println("Post is now hidden.");
    }

    @Override
    public void deletePost(User user) {
        // Ensure the post is removed from the user's list of posts
        if (user.getPosts().remove(this)) {
            System.out.println("Post with ID " + this.id + " removed from user's posts.");
        } else {
            System.out.println("Post with ID " + this.id + " was not found in user's posts.");
        }

        // Clear comments and reset upvotes and downvotes
        comments.clear();
        totalUpVotes -= this.upVotes;
        totalDownVotes -= this.downVotes;
        this.upVotes = 0;
        this.downVotes = 0;
        System.out.println("All comments cleared, and upvotes/downvotes reset for post ID: " + this.id);

        // Set hidden status for post, marking it as inactive (optional)
        this.hidden = true;
        System.out.println("Post with ID " + this.id + " is now hidden and deleted.");
    }

    @Override
    public void enableComments() {
        this.commentsEnabled = true;
        System.out.println("Comments have been enabled for this post.");
    }

    @Override
    public void disableComments() {
        this.commentsEnabled = false;
        System.out.println("Comments have been disabled for this post.");
    }
    public User getAuthor() {
        return this.author;
    }

    // Getters for compatibility with the User class
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public Picture getPicture() {
        return picture;
    }

    public boolean isHidden() {
        return hidden;
    }

    public boolean isCommentsEnabled() {
        return commentsEnabled;
    }
    public Map<Integer, String> getComments() {
        return this.comments;
    }
    public int getUpVotes() {
        return totalUpVotes;
    }
    public int getDownVotes() {
        return totalDownVotes;
    }
    public synchronized void setUpVotes(int upVotes) {
        this.upVotes = upVotes;
    }
    public synchronized void setDownVotes(int downVotes) {
        this.downVotes = downVotes;
    }


}


