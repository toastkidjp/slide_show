package jp.toastkid.slideshow.slide;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.mockito.internal.util.reflection.Whitebox;
import org.testfx.framework.junit.ApplicationTest;

import javafx.geometry.Pos;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Labeled;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
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
        assertEquals("Title", ((Labeled) Whitebox.getInternalState(slide, "title")).getText());
        assertEquals(3, ((Pane) Whitebox.getInternalState(slide, "contents")).getChildren().size());
    }

    /**
     * Test of front {@link Slide}.
     */
    @Test
    public void test_frontSlide() {
        final Slide front = new Slide.Builder().title("Title").addLines("name").isFront(true).build();
        final Labeled labeled = (Labeled) Whitebox.getInternalState(front, "title");
        assertEquals(200.0d, labeled.getFont().getSize(), 0.0d);

        final HBox node = (HBox) ((Pane) Whitebox.getInternalState(front, "contents")).getChildren().get(0);
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
     * Test of {@link Slide#setIndicator(int, int)}.
     */
    @Test
    public void test_setIndicator() {
        slide.setIndicator(-1, -1);
        final ProgressBar slider
            = (ProgressBar) ((VBox) slide.getChildren().get(2)).getChildren().get(0);
        assertEquals(1.0d, slider.getProgress(), 0.1d);
        slide.getChildren().remove(2);

        slide.setIndicator(0, 0);
        final ProgressBar zero
            = (ProgressBar) ((VBox) slide.getChildren().get(2)).getChildren().get(0);
        assertEquals(Double.NaN, zero.getProgress(), 0.1d);
        slide.getChildren().remove(2);

        slide.setIndicator(1, 12);
        final ProgressBar posi
            = (ProgressBar) ((VBox) slide.getChildren().get(2)).getChildren().get(0);
        assertEquals(0.08333333333333333d, posi.getProgress(), 0.01d);
        slide.getChildren().remove(2);
    }

    /**
     * Test for coverage.
     */
    @Test
    public void test() {
        slide.rightIn();
        slide.leftIn();
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
