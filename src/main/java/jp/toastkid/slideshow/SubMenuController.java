package jp.toastkid.slideshow;

import com.jfoenix.controls.JFXSlider;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.NumberValidator;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;
import javafx.beans.property.IntegerProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import jp.toastkid.slideshow.control.Stopwatch;
import jp.toastkid.slideshow.message.HidingMenuMessage;
import jp.toastkid.slideshow.message.Message;
import jp.toastkid.slideshow.message.MoveMessage;
import jp.toastkid.slideshow.message.PdfMessage;
import jp.toastkid.slideshow.message.QuitMessage;

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
    private final Subject<Message> messenger;

    /**
     * NOP.
     */
    public SubMenuController() {
        messenger = PublishSubject.create();
    }

    /**
     * Back to first slide.
     */
    @FXML
    private void moveToStart() {
        messenger.onNext(MoveMessage.makeStart());
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
        messenger.onNext(MoveMessage.makeTo((int) indexSlider.getMax()));
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

        final int parseInt = Integer.parseInt(text);
        messenger.onNext(MoveMessage.makeTo(parseInt));
        if ((int) roundSliderValue() == parseInt) {
            return;
        }
        indexSlider.setValue(parseInt);
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
            final long rounded = roundSliderValue();
            indexSlider.setValue(rounded);
            indexInput.setText(Long.toString(rounded));
            indexInput.fireEvent(new ActionEvent());
        });
        indexInput.getValidators().add(new NumberValidator());
    }

    /**
     * Return rounded value in slider.
     * @return rounded value(long)
     */
    private long roundSliderValue() {
        return Math.round(indexSlider.getValue());
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
     * Return message subscription.
     * @return {@link TopicProcessor}.
     */
    public Disposable subscribe(final Consumer<Message> c) {
        return messenger.subscribe(c);
    }

}
