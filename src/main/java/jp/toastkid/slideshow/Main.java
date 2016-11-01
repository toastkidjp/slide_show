package jp.toastkid.slideshow;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.impl.list.Interval;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jfoenix.controls.JFXProgressBar;

import javafx.application.Application;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ObservableMap;
import javafx.fxml.FXMLLoader;
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

    /** PDF file name. */
    private static final String DEFAULT_PDF_FILE_NAME = "slide.pdf";

    /** Title of this app. */
    private static final String TITLE = "Slide show";

    /** Full screen key. */
    private static final KeyCodeCombination FULL_SCREEN_KEY = new KeyCodeCombination(KeyCode.F11);

    /** Back key. */
    private static final KeyCodeCombination BACK_1 = new KeyCodeCombination(KeyCode.BACK_SPACE);

    /** Back key. */
    private static final KeyCodeCombination BACK_2 = new KeyCodeCombination(KeyCode.LEFT);

    /** Back key. */
    private static final KeyCodeCombination BACK_3 = new KeyCodeCombination(KeyCode.UP);

    /** Forward key. */
    private static final KeyCodeCombination FORWARD_1 = new KeyCodeCombination(KeyCode.ENTER);

    /** Forward key. */
    private static final KeyCodeCombination FORWARD_2 = new KeyCodeCombination(KeyCode.RIGHT);

    /** Forward key. */
    private static final KeyCodeCombination FORWARD_3 = new KeyCodeCombination(KeyCode.DOWN);

    /** Quit key. */
    private static final KeyCodeCombination SUB
        = new KeyCodeCombination(KeyCode.F, KeyCombination.CONTROL_DOWN);

    /** Quit key. */
    private static final KeyCodeCombination SAVE
        = new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN);

    /** Quit key. */
    private static final KeyCodeCombination QUIT = new KeyCodeCombination(KeyCode.ESCAPE);

    /** fxml file. */
    private static final String FXML_PATH = "scenes/SubMenu.fxml";

    /** This app's Stage. */
    private final Stage stage;

    /** Slides. */
    private MutableList<Slide> slides;

    /** Current slide index. */
    private IntegerProperty current;

    /** Progress bar. */
    private JFXProgressBar jfxProgressBar;

    /** Progress indicator. */
    private Label indicator;

    /** SubMenu's pane */
    private Pane subPane;

    /** Controller object. */
    private SubMenuController controller;

    /**
     * Constructor.
     */
    public Main() {
        this.stage = new Stage(StageStyle.DECORATED);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setTitle(TITLE);
        stage.setOnCloseRequest(event -> stage.close());

        initSubMenu();

        maximizeStage();
    }

    /**
     * Initialize SubMenu.
     */
    private void initSubMenu() {
        controller = readSub();
        subPane = controller.getRoot();
        subPane.setStyle("-fx-background-color: #EEEEEE;"
                + "-fx-effect: dropshadow(three-pass-box, #000033, 10, 0, 0, 0);");
        subPane.setManaged(false);
        controller.setOnGeneratePdf(this::generatePdf);
        controller.setOnHide(this::hideSubMenu);
        controller.setOnQuit(this::quit);
        controller.setOnBack(this::back);
        controller.setOnForward(this::forward);
        controller.setOnMoveTo(this::moveTo);
    }

    /**
     * Init submenu controller.
     * @return Controller
     */
    private final SubMenuController readSub() {
        try {
            final FXMLLoader loader
                = new FXMLLoader(getClass().getClassLoader().getResource(FXML_PATH));
            loader.load();
            return (SubMenuController) loader.getController();
        } catch (final IOException e) {
            e.printStackTrace();
        }
        return null;
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
        scene.getStylesheets().add(getClass().getClassLoader().getResource("keywords.css").toExternalForm());
        putAccelerators(scene);
        stage.setScene(scene);
        if (owner == null) {
            this.stage.setOnCloseRequest(event -> System.exit(0));
        } else {
            this.stage.initOwner(owner);
        }
        //this.stage.setFullScreen(true);
        stage.showAndWait();
    }

    /**
     * Generate PDF file.
     */
    private void generatePdf() {
        final long start = System.currentTimeMillis();
        LOGGER.info("Start generating PDF.");
        try (final PDDocument doc = new PDDocument()) {
            final int width  = (int) stage.getWidth();
            final int height = (int) stage.getHeight();
            Interval.zeroTo(slides.size() - 1).each(i ->{
                moveTo(i);
                final long istart = System.currentTimeMillis();
                final PDPage page = new PDPage(new PDRectangle(width, height));
                try (final PDPageContentStream content = new PDPageContentStream(doc, page)) {
                    content.drawImage(LosslessFactory.createFromImage(
                            doc, slides.get(i).generateImage(width, height)), 0, 0);
                } catch(final IOException ie) {
                    LOGGER.error("Occurred Error!", ie);
                }
                doc.addPage(page);
                LOGGER.info("Ended page. {}[ms]", System.currentTimeMillis() - istart);
            });
            doc.save(new File(DEFAULT_PDF_FILE_NAME));
        } catch(final IOException ie) {
            LOGGER.error("Occurred Error!", ie);
        }
        LOGGER.info("Ended generating PDF. {}[ms]", System.currentTimeMillis() - start);
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
        accelerators.put(BACK_3,    this::back);
        accelerators.put(FORWARD_1, this::forward);
        accelerators.put(FORWARD_2, this::forward);
        accelerators.put(FORWARD_3, this::forward);
        accelerators.put(SUB,       this::switchSubMenu);
        accelerators.put(SAVE,      this::generatePdf);
        accelerators.put(QUIT,      this::quit);
    }

    /**
     * Switch SubMenu visibility.
     */
    private void switchSubMenu() {
        if (subPane.isManaged()) {
            hideSubMenu();
        } else {
            showSubMenu();
        }
    }

    /**
     * Hide subMenu.
     */
    private void hideSubMenu() {
        subPane.setManaged(false);
        subPane.setVisible(false);
    }

    /**
     * Show subMenu.
     */
    private void showSubMenu() {
        subPane.setManaged(true);
        subPane.setVisible(true);
        controller.setFocus();
    }

    /**
     * This app close.
     */
    private void quit() {
        stage.close();
        if (stage.getOwner() == null) {
            System.exit(0);
        }
    }

    /**
     * Make root pane and set slides.
     * @return
     */
    private Pane loadRootPane() {
        final Pane root = new StackPane();
        root.getChildren().addAll(slides);
        initProgressBox(root);
        root.getChildren().add(subPane);
        move(false);
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
        moveTo(current.get() - 1);
    }

    /**
     * Forward slide.
     */
    private void forward() {
        moveTo(current.get() + 1);
    }

    /**
     * Move to specified index slide.
     * @param index
     */
    private void moveTo(final int index) {
        if (index <= 0 || slides.size() < index) {
            return;
        }

        slides.stream().filter(s -> s.isVisible()).forEach(s -> s.setVisible(false));
        final boolean isForward = current.get() < index;
        current.set(index);
        move(isForward);
    }

    /**
     * Move new slide.
     * @param isForward
     */
    private void move(final boolean isForward) {
        final Slide slide = slides.get(current.get() - 1);
        slide.setVisible(true);
        if (isForward) {
            slide.rightIn();
        } else {
            slide.leftIn();
        }
        final int i = current.get();
        indicator.setText(String.format("%d / %d", i, slides.size()));
        jfxProgressBar.setProgress((double) i / (double) slides.size());
    }

    /**
     * Read slides.
     * @param filePath path/to/file
     */
    private void readSlides(final String filePath) {
        slides = new WikiToSlides(Paths.get(filePath)).convert();
        current = new SimpleIntegerProperty(1);
        controller.setRange(1, slides.size(), current);
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
