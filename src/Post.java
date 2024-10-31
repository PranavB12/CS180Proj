import java.util.ArrayList;

public class Post implements IPost {
    private ArrayList<String> comments;
    private int votes;
    private String comment;
    private boolean commentsEnabled;

    public Post() {
        
    }

    public void upvote() {
        votes++;
    }
    public void downvote() {
        votes--;
    }
    public void addComment(String comment) {
        if (commentsEnabled)  {
            comments.add(comment);
        }
    }
    public void deleteComment(int commentId) {

    }
    public void hidePost() {

    }
    public void deletePost() {

    }
    public void enableComments() {

    }
    public void disableComments() {

    }
}
