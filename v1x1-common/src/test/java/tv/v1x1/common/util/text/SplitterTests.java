package tv.v1x1.common.util.text;

import com.google.common.collect.ImmutableList;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author Josh
 */
public class SplitterTests {
    @Test
    public void testSplitNone() {
        Assert.assertEquals(Splitter.split(500, "...", "The quick red fox"),
                ImmutableList.of("The quick red fox"),
                "Split when message was smaller than line length");
    }

    @Test
    public void testSplitTwo() {
        Assert.assertEquals(Splitter.split(25, "...", "The quick red fox jumps over the lazy brown dog"),
                ImmutableList.of(
                        "The quick red fox jump...",
                        "...s over the lazy brown dog"));
    }

    @Test
    public void testSplitMany() {
        Assert.assertEquals(Splitter.split(15, "...", "The quick red fox jumps over the lazy brown dog"),
                ImmutableList.of(
                        "The quick re...",
                        "...d fox jum...",
                        "...ps over t...",
                        "...he lazy b...",
                        "...rown dog"));
    }

    @Test
    public void testSplitCompressLastLine() {
        Assert.assertEquals(Splitter.split(15, "...", "The quick red fox jumps over the lazy brown dog >.<"),
                ImmutableList.of(
                        "The quick re...",
                        "...d fox jum...",
                        "...ps over t...",
                        "...he lazy b...",
                        "...rown dog >.<"
        ));
    }
}
