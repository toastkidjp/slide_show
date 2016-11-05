package jp.toastkid.slideshow.slide;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.text.Font;

/**
 * Title slide's implementation.
 *
 * @author Toast kid
 */
public class TitleSlide extends Slide {

    /** Title label's font. */
    private static final Font TITLE_FONT = new Font(200);

    /** Title label's font. */
    private static final Font SUB_FONT   = new Font(100);

    /**
     * Centering text.
     */
    public TitleSlide() {
        super();
        this.setAlignment(Pos.CENTER);
    }

    @Override
    protected void initTitle() {
        super.initTitle();
        title.setFont(TITLE_FONT);
    }

    /**
     * Simple Factory of TitleSlide.
     *
     * @author Toast kid
     */
    public static class Factory {

        /**
         * Make {@link TitleSlide} with title.
         * @param title
         * @return
         */
        public static Slide make(final String title) {
            final TitleSlide slide = new TitleSlide();
            slide.setTitle(title);
            return slide;
        }

        /**
         * Make {@link TitleSlide} with title and simple text.
         * @param title
         * @param text
         * @return
         */
        public static Slide make(final String title, final String text) {
            final Slide slide = make(title);
            final Label sub = new Label(text);
            sub.getStyleClass().add("line");
            sub.setFont(SUB_FONT);
            slide.addContents(LineFactory.centering(sub));
            return slide;
        }
    }
}
