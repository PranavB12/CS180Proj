
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Post implements IPost {
    public User author;
    private static int id;
    private String content;
    private Picture picture;
    private boolean hidden;
    private boolean commentsEnabled;
    private int upVotes;
    private int downVotes;
    private Map<Integer, String> comments; //
    private static Object obj = new Object();// Stores comments with unique IDs

    // Static fields for total upvotes and downvotes (as per IPost)
    public static int totalUpVotes;
    public static int totalDownVotes;

    // Constructor
    public Post(String content, User user) {
        this.author = user;
        this.content = content;
        this.hidden = false;
        this.commentsEnabled = true;
        this.upVotes = 0;
        this.downVotes = 0;
        this.comments = new HashMap<>();
    }

    // IPost Interface Methods

    @Override
    public void upvote() {
        synchronized (obj) {
            totalUpVotes++;
        }
        System.out.println("Post upvoted. Total upvotes: " + totalUpVotes);
    }

    @Override
    public void downvote() {
        synchronized (obj) {
            totalDownVotes++;
        }
        System.out.println("Post downvoted. Total downvotes: " + totalDownVotes);
    }




    @Override

    public User getAuthor() {
        return this.author;
    }

    // Getters for compatibility with the User class
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public Picture getPicture() {
        return picture;
    }

    public boolean isHidden() {
        return hidden;
    }

    public boolean isCommentsEnabled() {
        return commentsEnabled;
    }
    public Map<Integer, String> getComments() {
        return this.comments;
    }
    public int getUpVotes() {
        return totalUpVotes;
    }
    public int getDownVotes() {
        return totalDownVotes;
    }
    public synchronized void setUpVotes(int upVotes) {
        this.upVotes = upVotes;
    }
    public synchronized void setDownVotes(int downVotes) {
        this.downVotes = downVotes;
    }


}

// Picture class (for completeness)
public class Picture {
    private String url;

    public Picture(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
