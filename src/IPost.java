package src;

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
