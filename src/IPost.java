// IPost Interface
public interface IPost {
    void upvote();
    void downvote();
    void addComment(String comment);
    void deleteComment(int commentId);
    void hidePost();
    void deletePost();
    void enableComments();
    void disableComments();
}