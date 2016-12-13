package tv.v1x1.common.util.text;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created by cobi on 12/13/2016.
 */
public class ShortenTests {
    @Test
    public void testGenPreviewShort() {
        Assert.assertEquals(Shorten.genPreview("a", 10), "a");
    }

    @Test
    public void testGenPreviewMedium() {
        Assert.assertEquals(Shorten.genPreview("1234567890", 10), "1234567890");
    }

    @Test
    public void testGenPreviewLong() {
        Assert.assertEquals(Shorten.genPreview("12345678901234567890", 10), "1234567890...");
    }

    @Test
    public void testGenPreviewWordBoundary() {
        Assert.assertEquals(Shorten.genPreview("This is just a test.", 10), "This is just...");
    }

    @Test
    public void testGenPreviewWords() {
        Assert.assertEquals(Shorten.genPreview("If we test, then we test", 9), "If we test,...");
    }
}
