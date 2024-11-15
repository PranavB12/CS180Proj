package src;

import java.util.Map;
import java.util.Set;

public interface IPost {

    String getId();

    void setId(String id);

    String getContent();

    User getAuthor();

    int getUpVotes();

    void setUpVotes(int upVotes);

    int getDownVotes();

    void setDownVotes(int downVotes);

    Map<String, Comment> getComments();

    void upvote(String username);

    void downvote(String username);

    void addComment(String commentId, Comment comment);

    void deleteComment(String commentId);

    void hidePost();

    void enableComments();

    void disableComments();

    void setHidden(boolean hidden);

    boolean getHidden();

    String toString();
}
