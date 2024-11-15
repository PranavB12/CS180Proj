package src;
import java.util.HashMap;
import java.util.Map;

public class Post {
    private String id;
    private String content;
    private User author;
    private int upVotes = 0;
    private int downVotes = 0;
    private Map<String, Comment> comments = new HashMap<>();
    private boolean commentsEnabled = true;
    private boolean hidden;

    public Post(String content, User author, String postId) {
        this.content = content;
        this.author = author;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public User getAuthor() {
        return author;
    }

    public int getUpVotes() {
        return upVotes;
    }

    public void setUpVotes(int upVotes) {
        this.upVotes = upVotes;
    }

    public int getDownVotes() {
        return downVotes;
    }

    public void setDownVotes(int downVotes) {
        this.downVotes = downVotes;
    }

    public Map<String, Comment> getComments() {
        return comments;
    }

    public void upvote() {
        upVotes++;
    }

    public void downvote() {
        downVotes++;
    }

    public void addComment(String comment, Comment com) {
        comments.put(comment, com);
    }

    public void deleteComment(String commentId) {
        comments.remove(commentId);
    }

    public void hidePost() {
        // Hides the post, add logic as needed
    }

    public void enableComments() {
        commentsEnabled = true;
    }



    public void disableComments() {
        commentsEnabled = false;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public boolean getHidden() {
        return this.hidden;
    }
    public String toString() {
        return "Post{" +
                "id='" + id + '\'' +
                ", content='" + content + '\'' +
                ", author='" + author.getUsername() + '\'' +
                ", upVotes=" + upVotes +
                ", downVotes=" + downVotes +
                ", comments=" + comments +
                ", commentsEnabled=" + commentsEnabled +
                ", hidden=" + hidden +
                '}';
    }

    

}
