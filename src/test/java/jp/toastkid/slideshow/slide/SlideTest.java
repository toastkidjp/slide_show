package jp.toastkid.slideshow.slide;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

import javafx.geometry.Pos;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Labeled;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jp.toastkid.slideshow.slide.Slide.Builder;

/**
 * {@link Slide}'s test.
 * @author Toast kid
 *
 */
public class SlideTest extends ApplicationTest {

    /** Slide. */
    private Slide slide;

    /**
     * Test of {@link Slide}.
     */
    @Test
    public void test_slide() {
        final VBox contents = (VBox) slide.getContent();
        assertEquals(
                "Title",
                ((Labeled) ((HBox) contents.getChildren().get(0)).getChildren().get(0)).getText()
                );
        assertEquals(4, contents.getChildren().size());
    }

    /**
     * Test of front {@link Slide}.
     */
    @Test
    public void test_frontSlide() {
        final Slide front = new Slide.Builder().title("Title").addLines("name").isFront(true).build();
        final VBox contents = (VBox) front.getContent();
        final Labeled labeled = (Labeled) ((HBox) contents.getChildren().get(0)).getChildren().get(0);
        assertEquals(200.0d, labeled.getFont().getSize(), 0.0d);

        final HBox node = (HBox) contents.getChildren().get(0);
        assertEquals(Pos.CENTER, node.getAlignment());
    }

    /**
     * Test of {@link Slide.Builder#hasBgImage()} and {@link Slide.Builder#hasTitle()}.
     */
    @Test
    public void test_hasBgImage() {
        final Builder builder = new Slide.Builder();
        assertFalse(builder.hasBgImage());
        assertFalse(builder.hasTitle());
        builder.title("Tomato").background("sample.png");
        assertTrue(builder.hasBgImage());
        assertTrue(builder.hasTitle());
    }

    /**
     * Test for coverage.
     */
    @Test
    public void test_transition() {
        slide.rightIn();
        slide.leftIn();
    }

    /**
     * Test of {@link Slide#getTitle()}.
     */
    @Test
    public void test_getTitle() {
        assertEquals("Title", slide.getTitle());
    }

    @Override
    public void start(final Stage stage) throws Exception {
        slide = new Slide.Builder()
                         .title("Title")
                         .addLines("1st", "2nd")
                         .background("sample.png")
                         .withContents(new DatePicker())
                         .build();
    }

}
