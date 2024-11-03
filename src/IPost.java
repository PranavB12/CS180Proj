package src;

import java.util.Map;


public interface IPost {
    void upvote();
    void downvote();
    void addComment(String comment);
    void deleteComment(int commentId);
    void hidePost();
    void deletePost(User user);
    void enableComments();
    void disableComments();
    User getAuthor();
    int getId();
    void setId(int id);
    String getContent();
    Picture getPicture();
    boolean isHidden();
    boolean isCommentsEnabled();
    Map<Integer, String> getComments();
    int getUpVotes();
    int getDownVotes();
    void setUpVotes(int upVotes);
    void setDownVotes(int downVotes);
}
