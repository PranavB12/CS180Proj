package src;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

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

    public static void main(String[] args) {
        PictureTest test = new PictureTest();
        test.testPictureUrl();
    }

    @Test
    void testPictureUrl() {
        String testUrl = "http://example.com/image.jpg";

        Picture picture = new Picture(testUrl);

        assertEquals(testUrl, picture.getUrl(), "The URL should match the one provided during construction");
    }
}
