package src;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Post {
    private String id;
    private String content;
    private User author;
    private int upVotes = 0;
    private int downVotes = 0;
    private Map<String, Comment> comments = new HashMap<>();
    private boolean commentsEnabled = true;
    private boolean hidden;
    private Set<String> upvoters = new HashSet<>();
    private Set<String> downvoters = new HashSet<>();

    public Post(String content, User author, String postId) {
        this.content = content;
        this.author = author;
        this.id = postId;
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

    public void upvote(String username) {
        if (upvoters.contains(username)) {
            System.out.println("User " + username + " has already upvoted this post.");
            return;
        }
        if (downvoters.contains(username)) {
            downvoters.remove(username);
            downVotes--;
        }
        upvoters.add(username);
        upVotes++;
    }

    public void downvote(String username) {
        if (downvoters.contains(username)) {
            System.out.println("User " + username + " has already downvoted this post.");
            return;
        }
        if (upvoters.contains(username)) {
            upvoters.remove(username);
            upVotes--;
        }
        downvoters.add(username);
        downVotes++;
    }

    public void addComment(String comment, Comment com) {
        comments.put(comment, com);
    }

    public void deleteComment(String commentId) {
        comments.remove(commentId);
    }

    public void hidePost() {
        hidden = true;
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
