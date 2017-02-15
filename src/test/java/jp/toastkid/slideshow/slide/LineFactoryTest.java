package jp.toastkid.slideshow.slide;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

import javafx.scene.control.Label;
import javafx.scene.control.OverrunStyle;
import javafx.stage.Stage;

/**
 * {@link LineFactory}'s test cases.
 *
 * @author Toast kid
 *
 */
public class LineFactoryTest extends ApplicationTest {

    /**
     * Test of {@link LineFactory#normal(String)}.
     */
    @Test
    public void test_normal() {
        final Label label = LineFactory.normal("Normal");
        assertEquals("Normal", label.getText());
        assertEquals(OverrunStyle.ELLIPSIS, label.getTextOverrun());
        assertTrue(label.isWrapText());
        assertEquals(Double.NEGATIVE_INFINITY, label.getMinHeight(), 0.1d);
    }

    @Override
    public void start(Stage stage) throws Exception {
        // NOP.
    }

}
