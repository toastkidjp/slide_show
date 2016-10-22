package jp.toastkid.slideshow;

import java.util.List;

import org.eclipse.collections.impl.factory.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.application.Application;
import javafx.collections.ObservableMap;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import jp.toastkid.slideshow.slide.Slide;
import jp.toastkid.slideshow.slide.TitleSlide;

/**
 * Simple presentation tool powered by JavaFX.
 *
 * @author Toast kid
 */
public class Main extends Application {

    /** Logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    /** Title of this app. */
    private static final String TITLE = "Slide show";

    /** Full screen key. */
    private static final KeyCodeCombination FULL_SCREEN_KEY = new KeyCodeCombination(KeyCode.F11);

    /** Back key. */
    private static final KeyCodeCombination BACK_1 = new KeyCodeCombination(KeyCode.BACK_SPACE);

    /** Back key. */
    private static final KeyCodeCombination BACK_2 = new KeyCodeCombination(KeyCode.LEFT);

    /** Back key. */
    private static final KeyCodeCombination FORWARD_1 = new KeyCodeCombination(KeyCode.ENTER);

    /** Back key. */
    private static final KeyCodeCombination FORWARD_2 = new KeyCodeCombination(KeyCode.RIGHT);

    /** This app's Stage. */
    private final Stage stage;

    /** Slides. */
    private List<Slide> slides;

    /** Current slide index. */
    private int current;

    /**
     * Constructor.
     */
    public Main() {
        this.stage = new Stage(StageStyle.DECORATED);

        //stage.setScene(readScene());
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setTitle(TITLE);
        stage.setOnCloseRequest(event -> stage.close());
    }

    /**
     * show this app with passed stage.
     * @param owner
     */
    public void show(final Stage owner) {
        this.stage.getScene().getStylesheets().addAll(owner.getScene().getStylesheets());
        this.stage.initOwner(owner);
        show();
    }

    /**
     * show this app.
     */
    public void show() {
        if (stage.getOwner() == null) {
            stage.setOnCloseRequest(event -> System.exit(0));
        }

        readSlides();
        final Pane root = new StackPane();
        root.getChildren().addAll(slides);
        slides.get(0).setVisible(true);
        final Scene scene = new Scene(root);
        final ObservableMap<KeyCombination,Runnable> accelerators = scene.getAccelerators();
        accelerators.put(FULL_SCREEN_KEY, () -> stage.setFullScreen(true));
        accelerators.put(BACK_1,    this::back);
        accelerators.put(BACK_2,    this::back);
        accelerators.put(FORWARD_1, this::forward);
        accelerators.put(FORWARD_2, this::forward);
        stage.setScene(scene);
        stage.showAndWait();
    }

    /**
     * Back slide.
     */
    private void back() {
        moveTo(current - 1);
    }

    /**
     * Forward slide.
     */
    private void forward() {
        moveTo(current + 1);
    }

    /**
     * Move to specified index slide.
     * @param index
     */
    private void moveTo(final int index) {
        if (index < 0 || slides.size() <= index) {
            return;
        }
        slides.get(current).setVisible(false);
        current = index;
        move();
    }

    /**
     * Move new slide.
     */
    private void move() {
        //stage.getScene().getRoot().getChildrenUnmodifiable()

        slides.get(current).setVisible(true);
    }

    /**
     * Read slides.
     */
    private void readSlides() {
        final Slide title  = TitleSlide.Factory.make("My First\n Presentation.", "Toast kid(@y_q1m)");
        final Slide first  = Slide.Factory.make("My First Slide",  "Line 1", "Line 2", "Line 3");
        final Slide second = Slide.Factory.make("My Second Slide", "Line 1", "Line 2", "Line 3");
        final Slide eop    = TitleSlide.Factory.make("Thank you for\n your kindness.", "EOP");
        slides = Lists.mutable.of(title, first, second, eop);
    }

    @Override
    public void start(final Stage stage) throws Exception {
        show();
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
