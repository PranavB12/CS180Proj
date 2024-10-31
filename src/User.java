public class User {
    private String username;
    private String password;
    private String name;
    private String content;
    private String description;
    private Picture postPicture;
    private int postId;
    private Picture profilePicture;

    // Constructors
    public User(String username, String password, Picture profilePicture, String description, String name){
        this.username = username;
        this.password = password;
        this.profilePicture = profilePicture;
        this.description = description;
        this.name = name;
    }

    public User(String username, String password, String name){
        this.username = username;
        this.password = password;
        this.name = name;
    }

    public User() {

    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public Picture getPostPicture() {
        return postPicture;
    }
    public void setPostPicture(Picture postPicture) {
        this.postPicture = postPicture;
    }

    public int getPostId() {
        return postId;
    }
    public void setPostId(int postId) {
        this.postId = postId;
    }

    // user interact w post
    




}
