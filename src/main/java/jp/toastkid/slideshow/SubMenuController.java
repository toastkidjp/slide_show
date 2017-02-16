package jp.toastkid.slideshow;

import com.jfoenix.controls.JFXSlider;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.NumberValidator;

import javafx.beans.property.IntegerProperty;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import jp.toastkid.slideshow.control.Stopwatch;
import jp.toastkid.slideshow.message.HidingMenuMessage;
import jp.toastkid.slideshow.message.Message;
import jp.toastkid.slideshow.message.MoveMessage;
import jp.toastkid.slideshow.message.PdfMessage;
import jp.toastkid.slideshow.message.QuitMessage;
import reactor.core.publisher.TopicProcessor;

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

    /** Message sender. */
    private final TopicProcessor<Message> messenger;

    /**
     * NOP.
     */
    public SubMenuController() {
        messenger = TopicProcessor.create();
    }

    /**
     * Back to first slide.
     */
    @FXML
    private void moveToStart() {
        indexInput.setText("1");
        moveToWithText();
    }

    /**
     * Back slide.
     */
    @FXML
    private void back() {
        messenger.onNext(MoveMessage.makeBack());
    }

    /**
     * Forward slide.
     */
    @FXML
    private void forward() {
        messenger.onNext(MoveMessage.makeForward());
    }

    /**
     * Forward slide.
     */
    @FXML
    private void moveToEnd() {
        indexInput.setText(Integer.toString((int) indexSlider.getMax()));
        moveToWithText();
    }

    /**
     * Generate PDF.
     */
    @FXML
    private void generatePdf() {
        messenger.onNext(PdfMessage.make());
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
        messenger.onNext(MoveMessage.makeTo(Integer.parseInt(text)));
    }

    /**
     * Hide control.
     */
    @FXML
    private void hide() {
        messenger.onNext(HidingMenuMessage.make());
    }

    /**
     * Quit slide show.
     */
    @FXML
    private void quit() {
        messenger.onNext(QuitMessage.make());
    }

    /**
     * Set min and max value.
     * @param min
     * @param max
     * @param current
     * @param index
     */
    public void setRange(final int min, final int max, final IntegerProperty current) {
        indexSlider.setMin(min);
        indexSlider.setMax(max);
        indexSlider.setBlockIncrement(1.0d);
        indexSlider.setMajorTickUnit(1.0d);
        indexSlider.setShowTickMarks(true);
        indexSlider.setShowTickLabels(true);
        indexSlider.setValue(min);
        indexSlider.valueProperty().addListener((prev, next, val) -> {
            indexInput.setText(Integer.toString(val.intValue()));
            moveToWithText();
        });
        indexSlider.valueProperty().bindBidirectional(current);
        indexInput.getValidators().add(new NumberValidator());
    }

    /**
     * Pass root pane.
     * @return
     */
    public Pane getRoot() {
        return root;
    }

    /**
     * Set focus on index input.
     */
    public void setFocus() {
        indexInput.requestFocus();
    }

    /**
     * Return message sender.
     * @return {@link TopicProcessor}.
     */
    public TopicProcessor<Message> getMessenger() {
        return messenger;
    }

}
