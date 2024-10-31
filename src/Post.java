import java.util.ArrayList;

public class Post implements IPost {
    private ArrayList<String> comments;
    private int votes;
    private String comment;
    private String postId;
    private String userId;
    private String title;
    private String content;
    private boolean isHidden;
    private boolean commentsEnabled;
    
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

    // METHODS
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
        commentsEnabled = false;
    }

    public void addComment(String comment, int commentID) {
        if (commentsEnabled)  {
            comments.add(comment);
        }
    }
    
    public void deleteComment(String comment, int commentID) {
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

    // GETTERS AND SETTERS
    public int getPostID() {
        return postId;
    }

    public String getUserId() {
        return userID;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public int getVotes() {
        return votes;
    }
    
    public List<String> getComments() {
        return new ArrayList<>(comments); 
    }

    public boolean getHidden() {
        return isHidden;
    }

    public boolean getCommentsEnabled() {
        return commentsEnabled;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }

    public void setComments(List<String> comments) {
        this.comments = comments;
    }

    public void setHidden(boolean hidden) {
        isHidden = hidden;
    }

    public void setCommentsEnabled(boolean commentsEnabled) {
        this.commentsEnabled = commentsEnabled;
    }
}
