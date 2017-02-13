package jp.toastkid.slideshow.style;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * {@link StyleManager}'s test case.
 *
 * @author Toast kid
 *
 */
public class StyleManagerTest {


    /**
     * Test of {@link StyleManager#findUri(String)}'s empty input case.
     */
    @Test
    public void test_findUri_empty_case() {
        assertEquals(null, StyleManager.findUri(null));
        assertEquals("",   StyleManager.findUri(""));
    }

    /**
     * Test of {@link StyleManager#findUri(String)}'s not found file case.
     */
    @Test
    public void test_findUri_not_found_case() {
        assertEquals("plain.css", StyleManager.findUri("plain.css"));
    }

    /**
     * Test of {@link StyleManager#findUri(String)}.
     */
    @Test
    public void test_findUri() {
        final String uri = StyleManager.findUri("plain");
        assertTrue(uri.startsWith("file:/"));
        assertTrue(uri.endsWith("css/slide/plain.css"));
    }

}
