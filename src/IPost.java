package src;

/**
 * Group Project - CS18000 Gold
 *
 * interface for post class
 *
 * @author Pranav Bansal, Vivaan Malhotra, Rishi Rao, Mike Lee, Vaishnavi Sharma, lab sec 37
 *
 * @version November 19, 2024
 *
 */

import java.util.Map;
import java.util.Set;

public interface IPost {
    String getId();
    String getContent();
    User getAuthor();
    int getUpVotes();
    int getDownVotes();
    Set<String> getUpvoters();
    Set<String> getDownvoters();
    Map<String, Comment> getComments();
    boolean areCommentsEnabled();
    void enableComments();
    void disableComments();
    boolean isHidden();
    void hidePost();
    void unhidePost();
    void upvote(String username);
    void downvote(String username);
    void addComment(String id, Comment comment);
    void deleteComment(String commentId, User requestedUser);
}
