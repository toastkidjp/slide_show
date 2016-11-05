package jp.toastkid.slideshow.slide;

import java.io.IOException;

import org.fxmisc.richtext.CodeArea;

import com.jfoenix.controls.JFXButton;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;
import jp.toastkid.script.highlight.SimpleHighlighter;

/**
 * CSS Demo slide.
 *
 * @author Toast kid
 *
 */
public class CssDemoSlide extends Slide {

    /**
     * Load FXML.
     */
    public CssDemoSlide() {
        super();

        try {
            final FXMLLoader loader
                = new FXMLLoader(getClass().getClassLoader().getResource("scenes/CssDemo.fxml"));
            final VBox load = (VBox) loader.load();
            final CodeArea codeArea = new CodeArea();

            new SimpleHighlighter(codeArea, "fx.txt", "keywords/groovy.txt")
                .highlight();
            codeArea.setEditable(true);
            codeArea.setStyle("-fx-font-size: 40pt;");

            final JFXButton apply = new JFXButton("APPLY");
            apply.setOnAction(event -> {
                System.out.println(codeArea.getText());
                setStyle(codeArea.getText());
            });
            addContents(load, codeArea, apply);
        } catch (final IOException e) {
            e.printStackTrace();
        }

    }

    public static class Controller {

    }
}
