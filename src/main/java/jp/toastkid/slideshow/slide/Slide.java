package jp.toastkid.slideshow.slide;

import org.eclipse.collections.impl.utility.ArrayIterate;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

/**
 * Slide implementation.
 *
 * @author Toast kid
 */
public class Slide extends Scene {

    /** Header text font. */
    private static final Font HEAD_FONT = new Font(100);

    /** Title label. */
    protected final Label title;

    /** Contents. */
    protected final Pane contents;

    /**
     * Constructor.
     */
    public Slide() {
        this(new StackPane());
    }

    /**
     * Constructor.
     * @param root
     */
    public Slide(final Parent root) {
        super(root);
        title = new Label("Title");
        initTitle();
        contents = new VBox();
        final VBox box = new VBox(title, contents);
        box.setPrefWidth(1200);
        box.setPrefHeight(800);
        this.setRoot(box);
    }

    /**
     * Init title.
     */
    protected void initTitle() {
        title.setFont(HEAD_FONT);
    }

    /**
     * Set passed text to title.
     * @param title
     */
    public void setTitle(final String title) {
        this.title.setText(title);
    }

    /**
     * Add Node to contents box.
     * @param contents
     */
    public void addContents(Node... contents) {
        this.contents.getChildren().addAll(contents);
    }

    public static class Factory {

        /**
         * Make simple slide.
         * @param title title
         * @param lines lines of content.
         * @return Slide
         */
        public static Slide make(final String title, final String... lines) {
            final Slide slide = new Slide(new StackPane());
            ArrayIterate.forEach(lines, line -> slide.addContents(new Label(line)));
            slide.setTitle(title);
            return slide;
        }

    }


}
