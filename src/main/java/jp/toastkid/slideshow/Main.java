package jp.toastkid.slideshow;

import java.nio.file.Paths;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Simple presentation tool powered by JavaFX.
 *
 * @author Toast kid
 */
public class Main extends Application {

    /** Title of this app. */
    private static final String TITLE = "Slide show";

    @Override
    public void start(final Stage stage) throws Exception {
        stage.setTitle(TITLE);
        stage.setOnCloseRequest(event -> stage.close());
        new Slideshow.Builder()
            .setStage(stage)
            .setSource(Paths.get("sample.md"))
            .setIsFullScreen(false)
            .build()
            .launch();
    }

    /**
     * Main method.
     *
     * @param args
     */
    public static void main(final String[] args) {
        Application.launch(Main.class);
    }
}
