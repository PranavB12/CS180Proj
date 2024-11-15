package src;

import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ServerClientTest {
    @Test
    public void testAddUser() {
        Database db = new Database();
        User user = new User("testUser", "password", "Test Name");
        assertTrue(db.addUser(user));
        assertFalse(db.addUser(user)); // Duplicate user
    }
}
