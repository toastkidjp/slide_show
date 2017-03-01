package jp.toastkid.slideshow;

import java.nio.file.Paths;

import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

/**
 * {@link Slideshow}'s test case.
 *
 * @author Toast kid
 *
 */
public class SlideshowTest extends ApplicationTest {

    /** Slideshow object. */
    private Slideshow slideshow;

    /**
     * Test of {@link Slideshow}.
     */
    @Test
    public void test() {
        Platform.runLater(() -> {
            slideshow.launch();
            type(KeyCode.CONTROL, KeyCode.F);
            lookup("Quit").query().fireEvent(new ActionEvent());
        });
    }

    @Override
    public void start(final Stage stage) throws Exception {
        slideshow = new Slideshow.Builder()
            .setOwner(stage)
            .setSource(TestResources.source())
            .setStylesheet(Paths.get("css"))
            .setIsFullScreen(false)
            .build();
    }

}
