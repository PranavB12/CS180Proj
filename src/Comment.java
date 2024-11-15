package src;

import java.util.UUID;

public class Comment {
    private final String comment;
    private final String ID;
    private final User author;
    private final String postID;
    private int upVotes;
    private int downVotes;

    public Comment(String comment, User author, String postID) {
        if (comment == null || author == null || postID == null) {
            throw new IllegalArgumentException("Comment, author, and postID cannot be null.");
        }
        this.comment = comment;
        this.ID = UUID.randomUUID().toString();
        this.upVotes = 0;
        this.downVotes = 0;
        this.postID = postID;
        this.author = author;
    }

    public Comment(String comment, User author, String postID, String commentId) {
        if (comment == null || author == null || postID == null || commentId == null) {
            throw new IllegalArgumentException("Comment, author, postID, and commentId cannot be null.");
        }
        this.comment = comment;
        this.ID = commentId;
        this.upVotes = 0;
        this.downVotes = 0;
        this.postID = postID;
        this.author = author;
    }

    public String getPostID() {
        return this.postID;
    }

    public synchronized int getDownVotes() {
        return downVotes;
    }

    public synchronized int getUpVotes() {
        return upVotes;
    }

    public String getContent() {
        return comment;
    }

    public User getAuthor() {
        return this.author;
    }

    public synchronized void upVote() {
        this.upVotes++;
    }

    public synchronized void downVote() {
        this.downVotes++;
    }

    public synchronized void undoUpVote() {
        if (this.upVotes > 0) {
            this.upVotes--;
        }
    }

    public synchronized void undoDownVote() {
        if (this.downVotes > 0) {
            this.downVotes--;
        }
    }

    public String getID() {
        return ID;
    }

    @Override
    public String toString() {
        return "COMMENT|" + this.ID + "|" + this.author.getUsername() + "|" + this.comment;
    }
}
