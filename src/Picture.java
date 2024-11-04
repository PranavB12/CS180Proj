package src;

/**
 * Group Project - CS18000 Gold
 *
 * Class for Picture, where the url is managed
 *
 * @author Pranav Bansal, Vivaan Malhotra, Rishi Rao, Mike Lee, Vaishnavi Sharma, lab sec 37
 *
 * @version November 3, 2024
 *
 */

// Picture class (for completeness)

public class Picture implements IPicture{
    private String url;

    public Picture(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
