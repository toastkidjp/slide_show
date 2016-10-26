package jp.toastkid.slideshow;

import java.util.function.Consumer;

import com.jfoenix.controls.JFXSlider;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.NumberValidator;

import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import jp.toastkid.slideshow.control.Stopwatch;

/**
 * SubMenu controller.
 *
 * @author Toast kid
 */
public class SubMenuController {

    /** Root. */
    @FXML
    private Pane root;

    /** Slider. */
    @FXML
    private JFXSlider indexSlider;

    /** Input. */
    @FXML
    private JFXTextField indexInput;

    /** Stopwatch. */
    @FXML
    private Stopwatch stopwatch;

    /** Action of generatePdf(). */
    private Runnable generatePdf;

    /** Action of quit(). */
    private Runnable quit;

    /** Action of back(). */
    private Runnable back;

    /** Action of forward(). */
    private Runnable forward;

    /** Action of moveTo(). */
    private Consumer<Integer> moveTo;

    /** Action of hide(). */
    private Runnable hide;

    /**
     * NOP.
     */
    public SubMenuController() {
        //readScene();
    }

    /**
     * Back slide.
     */
    @FXML
    private void back() {
        if (back == null) {
            throw new IllegalStateException();
        }
        back.run();
    }

    /**
     * Forward slide.
     */
    @FXML
    private void forward() {
        if (forward == null) {
            throw new IllegalStateException();
        }
        forward.run();
    }

    /**
     * Generate PDF.
     */
    @FXML
    private void generatePdf() {
        if (generatePdf == null) {
            throw new IllegalStateException();
        }
        generatePdf.run();
    }

    /**
     * Move to index.
     */
    @FXML
    private void moveToWithText() {
        final String text = indexInput.getText();
        if (text == null || text.length() == 0) {
            return;
        }
        final int index = Integer.parseInt(text);
        moveTo.accept(index);
    }

    /**
     * Hide control.
     */
    @FXML
    private void hide() {
        if (hide == null) {
            throw new IllegalStateException();
        }
        hide.run();
    }

    /**
     * Quit slide show.
     */
    @FXML
    private void quit() {
        if (quit == null) {
            throw new IllegalStateException();
        }
        quit.run();
    }

    /**
     * Set on generatePdf() action.
     * @param generatePdf
     */
    public void setOnGeneratePdf(final Runnable generatePdf) {
        this.generatePdf = generatePdf;
    }

    /**
     * Set on quit() action.
     * @param quit
     */
    public void setOnQuit(final Runnable quit) {
        this.quit = quit;
    }

    /**
     * Set on back() action.
     * @param quit
     */
    public void setOnBack(final Runnable back) {
        this.back = back;
    }

    /**
     * Set on forward() action.
     * @param quit
     */
    public void setOnForward(final Runnable forward) {
        this.forward = forward;
    }

    /**
     * Set on moveTo() action.
     * @param moveTo
     */
    public void setOnMoveTo(final Consumer<Integer> moveTo) {
        this.moveTo = moveTo;
        indexInput.getValidators().add(new NumberValidator());
    }

    /**
     * Set on hide subMenu.
     * @param hide
     */
    public void setOnHide(Runnable hide) {
        this.hide = hide;
    }

    /**
     * Pass root pane.
     * @return
     */
    public Pane getRoot() {
        return root;
    }

}
