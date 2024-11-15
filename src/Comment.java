package src;

import java.util.UUID;

public class Comment implements IComment {
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



    public int getDownVotes() {
        return downVotes;
    }

    public int getUpVotes() {
        return upVotes;
    }

    public String getComment() {
        return comment;
    }
    public String getAuthor() {
        return this.author.toString();
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
