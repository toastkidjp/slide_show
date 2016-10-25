package jp.toastkid.slideshow.slide;

import org.eclipse.collections.impl.utility.ArrayIterate;

import com.jfoenix.controls.JFXProgressBar;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Screen;

/**
 * Slide implementation.
 *
 * @author Toast kid
 */
public class Slide extends VBox {

    /** Header text font. */
    private static final Font HEAD_FONT = new Font(150);

    /** Title label. */
    protected final Label title;

    /** Contents. */
    protected final Pane contents;

    /** Has background image. */
    private boolean hasBgImage;

    /**
     * Constructor.
     */
    public Slide() {
        title = new Label();
        initTitle();
        contents = new VBox();
        this.setVisible(false);
        this.getChildren().addAll(LineFactory.centering(title), contents);
    }

    /**
     * Init title.
     */
    protected void initTitle() {
        title.setFont(HEAD_FONT);
        title.setWrapText(true);
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

    /**
     * Set background image.
     * @param n Node
     * @param image url
     */
    public void setBgImage(final String image) {
        setStyle(
                "-fx-background-image: url('" + image + "'); " +
                "-fx-background-position: center center; " +
                "-fx-background-repeat: stretch;"
        );
        hasBgImage = true;
    }

    /**
     * Return has title in this slide.
     * @return
     */
    public boolean hasTitle() {
        return title.getText().length() != 0;
    }

    /**
     * Return this has background image.
     * @return
     */
    public boolean hasBgImage() {
        return hasBgImage;
    }

    /**
     * Set indicator.
     * @param index
     * @param maxPages
     */
    public void setIndicator(final int index, final int maxPages) {
        final double progress = (double) index / (double) maxPages;
        final JFXProgressBar jfxProgressBar = new JFXProgressBar(progress);
        jfxProgressBar.setPrefWidth(Screen.getPrimary().getBounds().getWidth());
        final VBox barBox = new VBox(jfxProgressBar);
        barBox.setAlignment(Pos.BOTTOM_CENTER);
        this.getChildren().add(barBox);
    }

    /**
     * Factory of Slide.
     * @author Toast kid
     *
     */
    public static class Factory {

        /**
         * Make simple slide.
         * @param title title
         * @param lines lines of content.
         * @return Slide
         */
        public static Slide make(final String title, final String... lines) {
            final Slide slide = new Slide();
            ArrayIterate.collect(lines, LineFactory::normal).each(slide::addContents);
            slide.setTitle(title);
            return slide;
        }

    }

}
