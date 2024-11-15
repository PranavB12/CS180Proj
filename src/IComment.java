package src;

public interface IComment {
    String getComment();
    String getAuthor();
    String getID();
    int getUpVotes();
    int getDownVotes();

    void upVote();
    void downVote();

    String toString();
}
