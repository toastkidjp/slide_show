package jp.toastkid.slideshow.converter;

import static org.junit.Assert.assertEquals;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.eclipse.collections.api.list.MutableList;
import org.fxmisc.richtext.GenericStyledArea;
import org.junit.Test;
import org.mockito.internal.util.reflection.Whitebox;
import org.testfx.framework.junit.ApplicationTest;

import javafx.scene.control.Labeled;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jp.toastkid.slideshow.slide.Slide;

/**
 * {@link MarkdownToSlides}'s test cases.
 *
 * @author Toast kid
 *
 */
public class MarkdownToSlidesTest extends ApplicationTest {

    /**
     * Test of {@link MarkdownToSlides#convert()}.
     * @throws URISyntaxException
     */
    @Test
    public void test() throws URISyntaxException {
        final Path testResource
            = Paths.get(getClass().getClassLoader().getResource("jp/toastkid/slideshow/converter/test.md").toURI());
        final MarkdownToSlides toSlides = new MarkdownToSlides(testResource);
        final MutableList<Slide> slides = toSlides.convert();
        assertEquals(5, slides.size());
        assertEquals("plain.css", toSlides.getCss());

        checkFrontSlide(slides);

        checkCodeSlide(slides);
    }

    /**
     * Check of code slide.
     * @param slides
     */
    private void checkCodeSlide(final MutableList<Slide> slides) {
        final Slide code = slides.get(3);
        final VBox contents = (VBox) Whitebox.getInternalState(code, "contents");
        @SuppressWarnings("rawtypes")
        final GenericStyledArea node
            = (GenericStyledArea) ((VBox) contents.getChildren().get(1)).getChildren().get(0);
        assertEquals(
                "int a = 100;System.out.println(a);",
                node.getText().replaceFirst("\r\n|[\n\r\u2028\u2029\u0085]", "")
                );
    }

    /**
     * Check of front slide.
     * @param slides
     */
    private void checkFrontSlide(final MutableList<Slide> slides) {
        final Slide front = slides.get(0);
        final Labeled title = (Labeled) Whitebox.getInternalState(front, "title");
        assertEquals(200.0d, title.getFont().getSize(), 0.1d);
    }

    @Override
    public void start(Stage stage) throws Exception {
        // NOP.
    }

}
