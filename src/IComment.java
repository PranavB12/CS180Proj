package src;

/**
 * Group Project - CS18000 Gold
 *
 * Comment class interface
 *
 * @author Pranav Bansal, Vivaan Malhotra, Rishi Rao, Mike Lee lab sec 37
 *
 * @version November 19, 2024
 *
 */

public interface IComment {
    String getPostID();
    void upvote(String username);
    void downvote(String username);
    int getDownVotes();
    int getUpVotes();
    String getContent();
    String getID();
    void setDownvotes(int downVotes);
    void setUpvotes(int upVotes);
}
