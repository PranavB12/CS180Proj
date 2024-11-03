package src;

// IPost Interface
public interface IPost {
    static int totalUpVotes = 0;
    static int totalDownVotes = 0;
    void upvote();
    void downvote();
    void addComment(String comment);
    void deleteComment(int commentId);
    void hidePost();
    void deletePost();

    void deletePost(User user);

    void enableComments();
    void disableComments();
}
