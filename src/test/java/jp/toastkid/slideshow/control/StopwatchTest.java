package jp.toastkid.slideshow.control;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

import javafx.stage.Stage;

/**
 * {@link Stopwatch}'s test case.
 *
 * @author Toast kid
 *
 */
public class StopwatchTest extends ApplicationTest {

    /** Test object. */
    private Stopwatch sw;

    /**
     * Test of {@link Stopwatch}.
     */
    @Test
    public void test() {
        assertTrue(sw.getText().startsWith("00:00"));
        assertFalse(sw.isActive());
        sw.start();
        assertTrue(sw.isActive());
        sw.reset();
        assertTrue(sw.getText().startsWith("00:00"));
        sw.start();
        assertFalse(sw.isActive());
    }

    @Override
    public void start(Stage stage) throws Exception {
        sw = new Stopwatch();
    }

}
