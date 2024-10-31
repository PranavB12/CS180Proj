// IPost Interface
public interface IPost {
    static int totalUpVotes;
    static int totalDownVotes;
    void upvote();
    void downvote();
    void addComment(String comment);
    void deleteComment(int commentId);
    void hidePost();
    void deletePost();
    void enableComments();
    void disableComments();
}
