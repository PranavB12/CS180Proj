# CS180 Project

### Prerequisites
- Java Development Kit (JDK) version 11 or higher.
- JUnit 5 for running tests.


### Compilation and Execution Instructions

1. Navigate to the src folder

2. Compile the project by running
```javac src/*.java```

3. Run the classes by running
```java -cp src ClassName```

##### Alternatively
1. Open the project in IntelliJ IDEA.
2. Right-click on the main class or any test class you want to run, and select Run 'ClassName'.
3. To run all tests, you can also navigate to the Run menu, select Run All Tests, or use the JUnit configuration to execute all test cases.

### Submission Details
#### Rishi Rao -`rao308@purdue.edu`
- Submitted to Vocareum workspace.


### Class Descriptions

#### Database
- Functionality: This class manages the users and posts within the application. It has methods to handle adding, removing, and authenticating users, creating and deleting posts, adding friends, blocking users, and persistent storage.
- Testing: The testing includes tests for every single method, including adding removing and validating credentials as well as post management and file operations. Verified in `DatabaseTest.java`
- Relationships: It works closely with User and Post classes. It retrieves posts from the User's friend and manages them in a local list as well as information about the User.

### NewsFeed
- Functionality: This manages and displays posts from a user's friend in a randomized order, which allows for a feed. It includes methods to delete a post from the feed and clear all posts.
- Testing: This tests for correct post retrieval, ensures that the posts are randomized and the deletion of posts from the feed. Verified in `NewsFeedTest.java`
- Relationships:  works with the User and Post classes by retrieving posts from the User's friends and manages them in a local list.

### Picture
- Functionality: This class represents a picture associated with a user or post by storing the picture's URL.
- Testing: It uses basic tests to verify URL assignment and retrieval. Verified in `PictureTest.java`
- Relationships: It is used by the User and Post as an optional property for storing profile and post images.

### User
- Functionality: This class is used to represent a user in the application, it holds information about the user such as their username, password, name, description, profile picture, friends list, blocked users, and their posts. It also includes functionality for friend and post management.
- Testing: The tests ensure that creating users, updating profile information, adding and removing friends as well as managing blocked users works as expected. Verified in `UserTest.java`
- Relationships: This class interacts with Database for validation and management, it interacts with Post to manage user-generated posts, and Picture to manage profile images. It also maintains lists of User objects for friends and blocked users.

### Post
- Functionality: This class represents a post created by a user, it contains details about the post like content, author, up votes, down votes, comments, visibility, and whether comments are disabled or enabled. It provides methods for upvoting, downvoting, adding and removing comments, and managing the post visibility.
- Testing: The tests for this ensure that voting, commenting, and visibility control all work as expected. Verified in `PostTest.java`
- Relationships: This class uses the User class as the author of the post and optionally includes a Picture for post images. It is managed by the Database and NewsFeed classes to enable post sharing and feed display.

### Comment
- Functionality: This class represents a comment on a post created by a user. It contains details about the comment, such as its content, author, unique identifier (ID), the ID of the associated post, and the number of upvotes and downvotes. It provides methods to interact with the comment, including upvoting, downvoting, undoing votes, and retrieving information such as the comment content, author, or associated post ID. This class ensures thread-safe operations for vote management and uniquely identifies each comment using a randomly generated or pre-specified ID.
- Testing: The tests for this ensure that voting, commenting, deleting, and visibility all work as expected. Verefied in `CommentTest.java`
- Relationships: This class uses the User class to represent the author of the comment, ensuring that each comment is linked to a valid user. It is associated with a post through the postID field, which identifies the specific post to which the comment belongs. The class interacts with systems managing posts and comments, such as a Database or NewsFeed class, to facilitate comment storage, retrieval, and display within a larger application framework.

## Server
- Functionality: The Server class represents the backend server for handling user accounts, posts, and comments within a multi-threaded system. It listens for client connections via a ServerSocket and processes requests concurrently using a cached thread pool managed by an ExecutorService. The server provides numerous functionalities, including user authentication, account management, post creation, and comment handling. It uses a command-based system where clients send textual commands (e.g., ADD_USER, CREATE_POST), and the server executes corresponding actions through its database. Requests are handled thread-safely and scalably, ensuring that multiple clients can interact with the server without interference. The server also supports advanced operations like hiding posts, upvoting or downvoting content, and enabling or disabling comments.
- Testing:
- Relationship: The Server class relies on a Database object to store and manage all user, post, and comment data. It interacts with users through sockets and delegates client-specific logic to the ClientHandler inner class, which implements the IClientHandler interface. It also implements the IDatabase and IServer interfaces, allowing it to act as both a database provider and a server controller. The ExecutorService facilitates concurrency, and the server's functionality integrates with other components, such as user-facing client applications, to provide a complete system for managing posts and comments.

## Client
- Functionality: The Client class facilitates communication between a user and the server. It connects to the server using a specified hostname and port, enabling the user to send textual commands and receive responses interactively. The class provides a simple command-line interface where the user can input commands such as ADD_USER or CREATE_POST to interact with the server's functionality. The client handles input and output streams for back-and-forth communication with the server. It continuously listens for user input, sends commands to the server, and displays the server’s responses. The class ensures a clean shutdown of resources in case of errors or termination.
- Testing:
- Relationship: The Client class connects to the Server class over a network using Socket. It acts as an interface for users to interact with the server's functionality, bridging the gap between the user and the server-side Database. It implements the IClient interface, which defines the client’s responsibilities. The class relies on system input and output for user interaction and collaborates with the server to execute operations such as user account management and content creation within a larger application framework.

### Test
This class is a test class that adds some sample users and posts to the database to test the data persistence it will not be present in the final project.
