package src;

import java.util.Map;

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

public interface IPost {
    void upvote();
    void downvote();
    void addComment(String comment);
    void deleteComment(String commentId);
    void hidePost();
    void deletePost(User user);
    void enableComments();
    void disableComments();
    User getAuthor();
    String getId();
    void setId(String id);
    String getContent();
    Picture getPicture();
    boolean isHidden();
    boolean isCommentsEnabled();
    Map<String, String> getComments();
    int getUpVotes();
    int getDownVotes();
    void setUpVotes(int upVotes);
    void setDownVotes(int downVotes);
}
