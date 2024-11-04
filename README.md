# CS180 Project

### Compilation and Execution Instructions

1. Navigate to the src folder

2. Compile the project by running
```javac src/*.java```

3. Run the classes by running
```java -cp src ClassName```

Alternatively

Open the project in IntelliJ IDEA.\
Right-click on the main class or any test class you want to run, and select Run 'ClassName'.\
To run all tests, you can also navigate to the Run menu, select Run All Tests, or use the JUnit configuration to execute all test cases.

### Submission Details
#### Pranav Bansal -`bansa124@purdue.edu`
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

