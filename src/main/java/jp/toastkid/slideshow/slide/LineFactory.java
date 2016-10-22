package jp.toastkid.slideshow.slide;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;

/**
 * Slide's line factory.
 *
 * @author Toast kid
 */
public class LineFactory {

    /** Title label's font. */
    private static final Font LINE_FONT = new Font(80);

    /**
     * Make simple line.
     * @param text line's text
     * @return Label
     */
    public static Label normal(final String text) {
        final Label line = new Label(text);
        line.setFont(LINE_FONT);
        return line;
    }

    /**
     * Return centering item.
     * @param n Node
     * @return
     */
    public static Node centering(final Node n) {
        final HBox forCentering = new HBox(n);
        forCentering.setAlignment(Pos.CENTER);
        return forCentering;
    }
}
