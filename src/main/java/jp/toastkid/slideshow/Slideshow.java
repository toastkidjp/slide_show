package jp.toastkid.slideshow;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

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
import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXSnackbar.SnackbarEvent;

import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ObservableList;
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
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;
import jp.toastkid.slideshow.converter.Converter;
import jp.toastkid.slideshow.converter.MarkdownToSlides;
import jp.toastkid.slideshow.message.HidingMenuMessage;
import jp.toastkid.slideshow.message.Message;
import jp.toastkid.slideshow.message.MoveMessage;
import jp.toastkid.slideshow.message.PdfMessage;
import jp.toastkid.slideshow.message.QuitMessage;
import jp.toastkid.slideshow.slide.Slide;
import jp.toastkid.slideshow.style.StyleManager;

/**
 * Main launcher.
 *
 * @author Toast kid
 *
 */
public class Slideshow {

    /** Logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    /** Default CSS. */
    private static final String DEFAULT_CSS = "plain";

    /** PDF file name. */
    private static final String DEFAULT_PDF_FILE_NAME = "slide.pdf";

    /** Full screen key. */
    private static final KeyCodeCombination FULL_SCREEN_KEY = new KeyCodeCombination(KeyCode.F11);

    /** Back key. */
    private static final KeyCodeCombination BACK_1 = new KeyCodeCombination(KeyCode.BACK_SPACE);

    /** Back key. */
    private static final KeyCodeCombination BACK_2 = new KeyCodeCombination(KeyCode.LEFT);

    /** Back key. */
    private static final KeyCodeCombination UP = new KeyCodeCombination(KeyCode.UP);

    /** Forward key. */
    private static final KeyCodeCombination FORWARD_1 = new KeyCodeCombination(KeyCode.ENTER);

    /** Forward key. */
    private static final KeyCodeCombination FORWARD_2 = new KeyCodeCombination(KeyCode.RIGHT);

    /** Forward key. */
    private static final KeyCodeCombination DOWN = new KeyCodeCombination(KeyCode.DOWN);

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

    /** Snackbar popup. */
    private JFXSnackbar snackbar;

    /** This app's Stage. */
    private final Stage   owner;

    /** Path of source. */
    private final Path    source;

    /** Path of stylesheet. */
    private final Path    css;

    /** If you want to test, this flag should be false. */
    private final boolean isFullScreen;

    private Stage stage;

    /**
     * Builder of {@link Slideshow}.
     *
     * @author Toast kid
     *
     */
    public static class Builder {

        /** Stage. */
        private Stage   stage;

        /** Path of source. */
        private Path    source;

        /** Path of stylesheet. */
        private Path    css;

        /** If you want to test, this flag should be false. */
        private boolean isFullScreen;

        /**
         * Set stage.
         * @param s
         * @return
         */
        public Builder setStage(final Stage s) {
            this.stage = s;
            return this;
        }

        /**
         * Set source path.
         * @param p
         * @return
         */
        public Builder setSource(final Path p) {
            this.source = p;
            return this;
        }

        /**
         * Set css path.
         * @param p
         * @return
         */
        public Builder setStylesheet(final Path p) {
            this.css = p;
            return this;
        }

        /**
         * Set is full screen.
         * @param f
         * @return
         */
        public Builder setIsFullScreen(final boolean f) {
            this.isFullScreen = f;
            return this;
        }

        /**
         * Build {@link Slideshow} instance.
         * @return {@link Slideshow}
         */
        public Slideshow build() {
            return new Slideshow(this);
        }

    }

    /**
     * Call from internal.
     * @param b Builder instance
     */
    private Slideshow(final Builder b) {
        this.owner        = b.stage;
        this.source       = b.source;
        this.css          = b.css;
        this.isFullScreen = b.isFullScreen;
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

        snackbar = new JFXSnackbar(subPane);

        controller.getMessenger().subscribe(this::processMessage);
    }

    /**
     * Process passed message.
     * @param m
     */
    private void processMessage(final Message m) {
        if (m instanceof HidingMenuMessage) {
            hideSubMenu();
            return;
        }

        if (m instanceof MoveMessage) {
            final MoveMessage message = (MoveMessage) m;
            switch (message.getCommand()) {
                case START:
                    Platform.runLater(() -> moveTo(message.getTo()));
                    return;
                case TO:
                    Platform.runLater(() -> moveTo(message.getTo()));
                    return;
                case BACK:
                    Platform.runLater(this::back);
                    return;
                case FORWARD:
                    Platform.runLater(this::forward);
                    return;
                default:
                    return;
            }
        }

        if (m instanceof PdfMessage) {
            Platform.runLater(this::generatePdf);
            return;
        }

        if (m instanceof QuitMessage) {
            Platform.runLater(this::quit);
            return;
        }
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
        owner.setX(maximizedBox.getMinX());
        owner.setY(maximizedBox.getMinY());
        owner.setWidth(maximizedBox.getWidth());
        owner.setHeight(maximizedBox.getHeight());
    }

    /**
     * Show this app with passed stage.
     *
     * @param owner
     * @param filePath
     * @param cssPath
     */
    private void show(final Path filePath, final Path cssPath, final boolean isFullScreen) {
        final String cssUri = readSlides(filePath);

        final Scene scene = new Scene(loadRootPane());
        applyStyle(scene, cssPath != null
                ? cssPath.toUri().toString()
                : StyleManager.findUri(DEFAULT_CSS));
        Optional.ofNullable(cssUri).ifPresent(c -> applyStyle(scene, c));
        putAccelerators(scene);

        stage = new Stage();
        stage.setScene(scene);
        if (owner == null) {
            stage.setOnCloseRequest(event -> System.exit(0));
        } else {
            stage.initOwner(owner);
        }
        stage.setFullScreen(isFullScreen);
        stage.show();
    }

    /**
     * Apply style with file uri to scene.
     * @param scene
     * @param cssUri
     */
    private void applyStyle(final Scene scene, final String cssUri) {
        final ObservableList<String> stylesheets = scene.getStylesheets();
        final ClassLoader classLoader = getClass().getClassLoader();
        stylesheets.addAll(
                classLoader.getResource("keywords.css").toExternalForm(),
                classLoader.getResource("css/snackbar.css").toExternalForm()
                );
        Optional.ofNullable(cssUri).ifPresent(stylesheets::add);
    }

    /**
     * Generate PDF file.
     */
    private void generatePdf() {
        final long start = System.currentTimeMillis();
        LOGGER.info("Start generating PDF.");
        try (final PDDocument doc = new PDDocument()) {
            final int width  = (int) owner.getWidth();
            final int height = (int) owner.getHeight();
            Interval.oneTo(slides.size()).each(i ->{
                moveTo(i);
                final long istart = System.currentTimeMillis();
                final PDPage page = new PDPage(new PDRectangle(width, height));
                try (final PDPageContentStream content = new PDPageContentStream(doc, page)) {
                    content.drawImage(LosslessFactory.createFromImage(
                            doc, slides.get(current.get() - 1).generateImage(width, height)), 0, 0);
                } catch(final IOException ie) {
                    LOGGER.error("Occurred Error!", ie);
                }
                doc.addPage(page);
                LOGGER.info("Ended page {}. {}[ms]", i, System.currentTimeMillis() - istart);
            });
            doc.save(new File(DEFAULT_PDF_FILE_NAME));

            snackbar.fireEvent(new SnackbarEvent("Ended generating PDF."));
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
        accelerators.put(FORWARD_1, this::forward);
        accelerators.put(FORWARD_2, this::forward);
        accelerators.put(SUB,       this::switchSubMenu);
        accelerators.put(SAVE,      this::generatePdf);
        accelerators.put(QUIT,      this::quit);
        accelerators.put(UP,        () -> slides.get(current.get() - 1).scrollUp());
        accelerators.put(DOWN,      () -> slides.get(current.get() - 1).scrollDown());
        scene.setOnMouseClicked(event -> {
            if (!event.getButton().equals(MouseButton.SECONDARY)) {
                return;
            }
            this.showSubMenu();
        });
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
        if (owner == null) {
            Platform.exit();
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
        if (index <= 0 || slides.size() < index || index == current.get()) {
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
     * @return css uri
     */
    private String readSlides(final Path filePath) {
        final Converter converter = findConverter(filePath);
        slides = converter.convert();
        current = new SimpleIntegerProperty(1);
        controller.setRange(1, slides.size(), current);
        return StyleManager.findUri(converter.getCss());
    }

    /**
     * Find suitable converter by file extension.
     * @param filePath
     * @return Converter
     */
    private Converter findConverter(final Path filePath) {
        return new MarkdownToSlides(filePath);
    }

    /**
     * Launch slideshow.
     */
    public void launch() {
        show(this.source, this.css, this.isFullScreen);
    }
}
