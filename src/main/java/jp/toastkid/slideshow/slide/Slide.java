package jp.toastkid.slideshow.slide;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.eclipse.collections.impl.utility.ArrayIterate;

import com.jfoenix.controls.JFXProgressBar;

import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.util.Duration;

/**
 * Slide implementation.
 *
 * @author Toast kid
 */
public class Slide extends ScrollPane {

    /** Slide duration. */
    private static final Duration TRANSITION_DURATION = Duration.seconds(0.3d);

    /** Title label's font. */
    private static final Font TITLE_MAIN_FONT = new Font(200);

    /** Header text font. */
    private static final Font HEAD_FONT       = new Font(150);

    /** Title label. */
    private final Label title;

    /** Contents. */
    private final VBox contents;

    /**
     * Factory of Slide.
     *
     * @author Toast kid
     */
    public static class Builder {

        private boolean isFront;

        private String bgImage;

        private String title;

        private List<String> lines;

        private List<Node> contents;

        public Builder title(final String title) {
            this.title = title;
            return this;
        }

        public Builder isFront(final boolean isFront) {
            this.isFront = isFront;
            return this;
        }

        public Builder addLines(final String... lines) {
            if (this.lines == null) {
                this.lines = new ArrayList<>();
            }
            ArrayIterate.forEach(lines, this.lines::add);
            return this;
        }

        public Builder withContents(final Node... lines) {
            if (this.contents == null) {
                this.contents = new ArrayList<>();
            }
            this.contents.addAll(Arrays.asList(lines));
            return this;
        }

        public Builder background(final String image) {
            this.bgImage = image;
            return this;
        }

        /**
         * Return has title in this slide.
         * @return
         */
        public boolean hasTitle() {
            return title != null && title.length() != 0;
        }

        /**
         * Return this has background image.
         * @return
         */
        public boolean hasBgImage() {
            return bgImage != null && bgImage.length() != 0;
        }

        public Slide build() {
            return new Slide(this);
        }

    }

    /**
     * Constructor.
     */
    private Slide(final Builder b) {
        this.setHbarPolicy(ScrollBarPolicy.NEVER);
        this.setHbarPolicy(ScrollBarPolicy.AS_NEEDED);
        this.setFitToWidth(true);
        title = new Label(b.title);
        initTitle(b.isFront);
        contents = new VBox();
        contents.getChildren().addAll(LineFactory.centering(title));
        Optional.ofNullable(b.lines).ifPresent(lines -> lines.stream()
                    .map(line -> b.isFront ? LineFactory.centeredText(line) : LineFactory.normal(line))
                    .forEach(contents.getChildren()::add));
        Optional.ofNullable(b.contents).ifPresent(contents.getChildren()::addAll);
        this.setVisible(false);
        Optional.ofNullable(b.bgImage).ifPresent(this::setBgImage);
        if (b.isFront) {
            this.contents.setAlignment(Pos.CENTER);
        }
        this.setContent(contents);
    }

    /**
     * Init title.
     */
    protected void initTitle(final boolean isTitle) {
        title.setFont(isTitle ? TITLE_MAIN_FONT : HEAD_FONT);
        title.setWrapText(true);
        title.getStyleClass().add("title");
        title.setMinHeight(Region.USE_PREF_SIZE);
    }

    /**
     * Set background image.
     * @param n Node
     * @param image url
     */
    private void setBgImage(final String image) {
        setStyle(
                "-fx-background-image: url('" + image + "'); " +
                "-fx-background-position: center center;" +
                "-fx-background-size: stretch;"
        );
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
     * Generate image.
     * @param width image's width
     * @param height image's height
     * @return BufferedImage
     */
    public BufferedImage generateImage(final int width, final int height) {
        final WritableImage image = new WritableImage(width, height);
        this.snapshot(new SnapshotParameters(), image);
        return SwingFXUtils.fromFXImage(image, null);
    }

    /**
     * Animate slide to next.
     */
    public void leftIn() {
        final TranslateTransition back = new TranslateTransition(TRANSITION_DURATION, this);
        back.setFromX(-2000);
        back.setToX(0);
        back.setInterpolator(Interpolator.LINEAR);
        back.setCycleCount(1);
        back.play();
    }

    /**
     * Animate slide to next.
     */
    public void rightIn() {
        final TranslateTransition next = new TranslateTransition(TRANSITION_DURATION, this);
        next.setFromX(2000);
        next.setToX(0);
        next.setInterpolator(Interpolator.LINEAR);
        next.setCycleCount(1);
        next.play();
    }

    /**
     * Scroll up.
     */
    public void scrollUp() {
        setVvalue(this.getVvalue() - 0.20d);
    }

    /**
     * Scroll down.
     */
    public void scrollDown() {
        setVvalue(this.getVvalue() + 0.20d);
    }

}
