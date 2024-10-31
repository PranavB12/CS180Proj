// IDatabase Interface
public interface IDatabase {
    void addUser(User user);
    void removeUser(String username);
    boolean validateUser(String username, String password);
    boolean userExists(String username);
}