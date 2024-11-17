package src;

/**
 * Group Project - CS18000 Gold
 *
 * Interface for the comment class
 *
 * @author Pranav Bansal, Vivaan Malhotra, Rishi Rao, Mike Lee, lab sec 37
 *
 * @version November 16, 2024
 *
 */

public interface IComment {

    String getPostID();
    int getDownVotes();
    int getUpVotes();
    String getContent();
    User getAuthor();
    void upVote();
    void downVote();
    void undoUpVote();
    void undoDownVote();
    String getID();
    String toString();
}
