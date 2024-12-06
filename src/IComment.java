package src;
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
