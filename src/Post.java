package src;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Post implements IPost {
    private final String id;
    private final String content;
    private final User author;
    private int upVotes = 0;
    private int downVotes = 0;
    private final Set<String> upvoters = new HashSet<>();
    private final Set<String> downvoters = new HashSet<>();
    private final Map<String, Comment> comments = new HashMap<>();
    private boolean commentsEnabled = true;
    private boolean hidden = false;

    public Post(String id, String content, User author) {
        this.id = id;
        this.content = content;
        this.author = author;
    }
    public Post(String id, String content, User author, int upVotes, int downVotes) {
        this.id = id;
        this.content = content;
        this.author = author;
        this.upVotes = upVotes;
        this.downVotes = downVotes;
        this.commentsEnabled = commentsEnabled;
        this.hidden = hidden;
    }

    public String getId() {
        return id;
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

    public int getDownVotes() {
        return downVotes;
    }

    public Set<String> getUpvoters() {
        return upvoters;
    }

    public Set<String> getDownvoters() {
        return downvoters;
    }

    public Map<String, Comment> getComments() {
        return new HashMap<>(comments); // Return a copy to avoid exposing internal state
    }

    public boolean areCommentsEnabled() {
        return commentsEnabled;
    }

    public void enableComments() {
        commentsEnabled = true;
    }

    public void disableComments() {
        commentsEnabled = false;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void hidePost() {
        hidden = true;
    }

    public void unhidePost() {
        hidden = false;
    }

    public synchronized void upvote(String username) {
        if (upvoters.contains(username)) {
            throw new IllegalStateException("User " + username + " has already upvoted this post.");
        }
        if (downvoters.contains(username)) {
            downvoters.remove(username);
            downVotes--;
        }
        upvoters.add(username);
        upVotes++;
    }

    public synchronized void downvote(String username) {
        if (downvoters.contains(username)) {
            throw new IllegalStateException("User " + username + " has already downvoted this post.");
        }
        if (upvoters.contains(username)) {
            upvoters.remove(username);
            upVotes--;
        }
        downvoters.add(username);
        downVotes++;
    }

    public synchronized void addComment(String id, Comment comment) {
        if (!commentsEnabled) {
            throw new IllegalStateException("Comments are disabled for this post.");
        }
        comments.put(comment.getID(), comment);
    }

    public synchronized void deleteComment(String commentId, User requestedUser) {
        Comment comment = comments.get(commentId);
        if (comment == null) {
            throw new IllegalStateException("Comment with ID " + commentId + " does not exist.");
        }
        if (!comment.getAuthor().equals(requestedUser)) {
            throw new IllegalStateException("User " + requestedUser.getUsername() + " is not authorized to delete this comment.");
        }
        comments.remove(commentId);
    }

    @Override
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