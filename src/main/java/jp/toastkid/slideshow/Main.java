package jp.toastkid.slideshow;

import java.nio.file.Paths;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jfoenix.controls.JFXProgressBar;

import javafx.application.Application;
import javafx.collections.ObservableMap;
import javafx.geometry.BoundingBox;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import jp.toastkid.slideshow.converter.WikiToSlides;
import jp.toastkid.slideshow.slide.Slide;

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

    /** Quit key. */
    private static final KeyCodeCombination QUIT = new KeyCodeCombination(KeyCode.ESCAPE);

    /** This app's Stage. */
    private final Stage stage;

    /** Slides. */
    private List<Slide> slides;

    /** Current slide index. */
    private int current;

    /** Progress bar. */
    private JFXProgressBar jfxProgressBar;

    /** Progress indicator. */
    private Label indicator;

    /**
     * Constructor.
     */
    public Main() {
        this.stage = new Stage(StageStyle.DECORATED);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setTitle(TITLE);
        stage.setOnCloseRequest(event -> stage.close());
        maximizeStage();
    }

    /**
     * Maximized the stage.
     */
    private void maximizeStage() {
        final Screen screen = Screen.getScreens().get(0);
        final Rectangle2D bounds = screen.getVisualBounds();
        final BoundingBox maximizedBox = new BoundingBox(
                bounds.getMinX(), bounds.getMinY(), bounds.getWidth(), bounds.getHeight());
        stage.setX(maximizedBox.getMinX());
        stage.setY(maximizedBox.getMinY());
        stage.setWidth(maximizedBox.getWidth());
        stage.setHeight(maximizedBox.getHeight());
    }

    /**
     * show this app.
     * @param filePath
     */
    public void show(final String filePath) {
        show(null, filePath);
    }

    /**
     * Show this app with passed stage.
     *
     * @param owner
     */
    public void show(final Stage owner, final String filePath) {
        readSlides(filePath);
        final Scene scene = new Scene(loadRootPane());
        putAccelerators(scene);
        stage.setScene(scene);
        if (owner == null) {
            this.stage.setOnCloseRequest(event -> System.exit(0));
        } else {
            this.stage.initOwner(owner);
        }
        this.stage.setFullScreen(true);
        stage.showAndWait();
    }

    /**
     * Put accelerators.
     * @param scene
     */
    private void putAccelerators(final Scene scene) {
        final ObservableMap<KeyCombination,Runnable> accelerators = scene.getAccelerators();
        accelerators.put(FULL_SCREEN_KEY, () -> stage.setFullScreen(true));
        accelerators.put(BACK_1,    this::back);
        accelerators.put(BACK_2,    this::back);
        accelerators.put(FORWARD_1, this::forward);
        accelerators.put(FORWARD_2, this::forward);
        accelerators.put(QUIT,      this::quit);
    }

    /**
     * If this screen is not full screen, This app will close.
     */
    private void quit() {
        if (stage.isFullScreen()) {
            return;
        }
        stage.close();
    }

    /**
     * Make root pane and set slides.
     * @return
     */
    private Pane loadRootPane() {
        final Pane root = new StackPane();
        root.getChildren().addAll(slides);
        initProgressBox(root);
        move();
        return root;
    }

    /**
     * Initialize progress bar and indicator.
     * @param root
     */
    private void initProgressBox(final Pane root) {
        indicator = new Label();
        indicator.setFont(Font.font(40));
        final HBox indicatorBox = new HBox(indicator);
        indicatorBox.setAlignment(Pos.CENTER_RIGHT);
        jfxProgressBar = new JFXProgressBar(0);
        jfxProgressBar.setPrefWidth(Screen.getPrimary().getBounds().getWidth());
        final VBox barBox = new VBox(indicatorBox, jfxProgressBar);
        barBox.setAlignment(Pos.BOTTOM_CENTER);
        root.getChildren().add(barBox);
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
        slides.get(current).setVisible(true);
        final int i = current + 1;
        indicator.setText(String.format("%d / %d", i, slides.size()));
        jfxProgressBar.setProgress((double) i / (double) slides.size());
    }

    /**
     * Read slides.
     * @param filePath path/to/file
     */
    private void readSlides(final String filePath) {
        slides = new WikiToSlides(Paths.get(filePath)).convert();
    }

    @Override
    public void start(final Stage stage) throws Exception {
        show("sample.txt");
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
