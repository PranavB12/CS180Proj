import java.util.ArrayList;

public class Post implements IPost {
    private ArrayList<String> comments;
    private int votes;
    private String comment;
    private boolean commentsEnabled;
    private boolean postHidden;
    private

    public Post(String postId, String userId, String title, String content) {
        this.postId = postId;
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.votes = 0;
        this.comments = new ArrayList<>();
        this.isHidden = false;
        this.commentsEnabled = true;
    }

    public void upvote() {
        votes++;
    }
    public void downvote() {
        votes--;
    }

    public void enableComments() {
        commentsEnabled = true;
    }
    public void disableComments() {
        //
    }

    public void addComment(String comment) {
        if (commentsEnabled)  {
            comments.add(comment);
        }
    }
    public void deleteComment(int commentId) {
        if (commentsEnabled)  {
            comments.remove(commentId);
        }
    }
    public void hidePost() {
        postHidden = true;
    }
    public void deletePost() {
        //
    }
}
