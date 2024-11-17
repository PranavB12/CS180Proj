package src;
public class
DatabaseTest2 {
    public static void main(String[] args) {
        // Create the database instance
        Database db = new Database();

        // Add users
        User user1 = new User("john_doe", "password123", "John Doe", "A regular user");
        User user2 = new User("jane_smith", "password456", "Jane Smith", "Another regular user");
        User user3 = new User("sam_brown", "password789", "Sam Brown", "A new user");

        db.addUser(user1);
        db.addUser(user2);
        db.addUser(user3);

        // Validate user addition
        System.out.println("Users after adding:");
        for (User user : db.getUsers()) {
            System.out.println(user.getUsername());
        }

        // Create a post
        String postId = db.createPost("This is a test post.", user1);

        // Validate post creation
        System.out.println("\nPosts after creating a post:");
        for (Post post : db.getPosts().values()) {
            System.out.println("Post ID: " + post.getId() + ", Content: " + post.getContent());
        }

        // Add a comment to the post
        db.addCommentToPost(postId, "This is a test comment.", user2);

        // Validate comment addition
        System.out.println("\nComments after adding a comment to the post:");
        for (Comment comment : db.getComments().values()) {
            System.out.println("Comment ID: " + comment.getID() + ", Content: " + comment.getContent() + ", Author: " + comment.getAuthor().getUsername());
        }

        // Add a friend
        boolean friendAdded = db.addFriend(user1, user2);
        System.out.println("\nFriendship between " + user1.getUsername() + " and " + user2.getUsername() + ": " + friendAdded);

        // Validate friendship (this assumes you have some method to check friendships in the Database class)
        // For example, you could print the friends list for user1
        System.out.println("\nFriends of " + user1.getUsername() + ":");
        // db.getFriends(user1).forEach(friend -> System.out.println(friend.getUsername())); // Uncomment if you have this method

        // Block a user
        boolean userBlocked = db.blockUser(user1, user3);
        System.out.println("\nUser " + user3.getUsername() + " blocked by " + user1.getUsername() + ": " + userBlocked);

        // Validate block status (assuming you have a method to check blocked users)
        // For example, you could print the blocked users list for user1
        // System.out.println("\nBlocked users of " + user1.getUsername() + ":");
        // db.getBlockedUsers(user1).forEach(blockedUser -> System.out.println(blockedUser.getUsername())); // Uncomment if you have this method

        // Write the database to file
        String filePath = "testDatabase.txt";
        db.writeDatabaseToFile(filePath);
        System.out.println("\nDatabase written to file: " + filePath);

        // Clear database and read from file
        Database newDb = new Database();
        newDb.readDatabaseFromFile(filePath);

        // Validate data after reading from file
        System.out.println("\nUsers after reading from file:");
        for (User user : newDb.getUsers()) {
            System.out.println(user.getUsername());
        }

        System.out.println("\nPosts after reading from file:");
        for (Post post : newDb.getPosts().values()) {
            System.out.println("Post ID: " + post.getId() + ", Content: " + post.getContent());
        }

        System.out.println("\nComments after reading from file:");
        for (Comment comment : newDb.getComments().values()) {
            System.out.println("Comment ID: " + comment.getID() + ", Content: " + comment.getContent());
        }

        // Check friendships and blocks after reading from file
        System.out.println("\nFriendships after reading from file:");
        // You can print out the friendships if you have a method like getFriends
        // System.out.println(newDb.getFriends(user1)); // Uncomment if applicable

        System.out.println("\nBlocked users after reading from file:");
        // You can print out the blocked users if you have a method like getBlockedUsers
        // System.out.println(newDb.getBlockedUsers(user1)); // Uncomment if applicable
    }
}
