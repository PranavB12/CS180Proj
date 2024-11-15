package src;
import java.util.UUID;


public class Comment {
    private String comment;
    private String ID;
    private User author;
    private int upVotes;
    private int downVotes;
    private String postID;

    public Comment(String comment, User author, String postID) {
        this.comment = comment;
        String Id = UUID.randomUUID().toString();
        this.ID = Id;
        this.upVotes = 0;
        this.downVotes = 0;
        this.postID = postID;
        this.author = author;


    }

    public Comment(String comment, User author, String postID, String commentId) {
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



    public int getDownVotes() {
        return downVotes;
    }

    public int getUpVotes() {
        return upVotes;
    }

    public String getContent() {
        return comment;
    }
    public User getAuthor() {
        return this.author;
    }
    public void upVote() {
        this.upVotes ++;
    }
    public void downVote() {
        this.downVotes ++;
    }
    public String getID() {
        return ID;
    }
    public String toString() {
        return "COMMENT|" + this.ID + "|" + this.author.getUsername() + "|" + this.comment;
    }


}
