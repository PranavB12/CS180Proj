package src;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
/**
 * Group Project - CS18000 Gold
 *
 * Comment class
 *
 * @author Pranav Bansal, Vivaan Malhotra, Rishi Rao, Mike Lee, Vaishnavi Sharma, lab sec 37
 *
 * @version November 19, 2024
 *
 */
public class Comment {
    private String comment;
    private String ID;
    private User author;
    private int upVotes;
    private int downVotes;
    private String postID;
    private Set<String> upvoters = new HashSet<>();
    private Set<String> downvoters = new HashSet<>();

    public Comment(String comment, User author, String postID) {
        if (comment == null || author == null || postID == null) {
            throw new IllegalArgumentException("Arguments cannot be null.");
        }
        this.comment = comment;
        this.ID = UUID.randomUUID().toString();
        this.author = author;
        this.postID = postID;
        this.upVotes = 0;
        this.downVotes = 0;
    }


    public Comment(String comment, User author, String postID, String commentId, int upVotes, int downVotes) {
        this.comment = comment;
        this.ID = commentId;
        this.upVotes = 0;
        this.downVotes = 0;
        this.postID = postID;
        this.author = author;


    }


    public String getPostID() {
        return this.postID;
    }
    public synchronized void upvote(String username) {
        if (upvoters.contains(username)) {
            throw new IllegalStateException("User " + username + " has already upvoted this post.");
        }
        if (downvoters.contains(username)) {
            downvoters.remove(username);
            downVotes--;
        }
        upvoters.add(username);
        upVotes++;
    }

    public synchronized void downvote(String username) {
        if (downvoters.contains(username)) {
            throw new IllegalStateException("User " + username + " has already downvoted this post.");
        }
        if (upvoters.contains(username)) {
            upvoters.remove(username);
            upVotes--;
        }
        downvoters.add(username);
        downVotes++;
    }



    public int getDownVotes() {
        return downVotes;
    }

    public int getUpVotes() {
        return upVotes;
    }

    public String getContent() {
        return comment;
    }
    public User getAuthor() {
        return this.author;
    }
    public void upVote() {
        this.upVotes ++;
    }
    public void downVote() {
        this.downVotes ++;
    }
    public String getID() {
        return ID;
    }
    public String toString() {
        return "COMMENT|" + this.ID + "|" + this.author.getUsername() + "|" + this.comment;
    }

    public void setDownvotes(int downVotes) {
        this.downVotes = downVotes;
    }

    public void setUpvotes(int upVotes) {
        this.upVotes = upVotes;
    }


}
