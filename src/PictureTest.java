package src;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Group Project - CS18000 Gold
 *
 * Picture Class JUnits
 *
 * @author Pranav Bansal, Vivaan Malhotra, Rishi Rao, Mike Lee, Vaishnavi Sharma, lab sec 37
 *
 * @version November 3, 2024
 *
 */

class PictureTest {
    private Picture picture; // Ensure this is Picture, not IPicture unless Picture implements IPicture

    @BeforeEach
    void setUp() {
        picture = new Picture("http://example.com/image.jpg"); // Initialize correctly here
    }

    @Test
    void testGetUrl() {
        // Ensure that picture is initialized and that getUrl() returns the correct URL
        assertNotNull(picture, "Picture should be initialized");
        assertEquals("http://example.com/image.jpg", picture.getUrl());
    }

    @Test
    void testGetUrlWithNull() {
        // Reassign picture with a null URL and test the behavior
        picture = new Picture(null);
        assertNull(picture.getUrl(), "URL should be null");
    }
}
