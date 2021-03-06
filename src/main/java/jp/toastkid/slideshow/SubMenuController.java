/*
 * Copyright (c) 2017 toastkidjp.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompany this distribution.
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package jp.toastkid.slideshow;

import com.jfoenix.controls.JFXSlider;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.NumberValidator;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import jp.toastkid.slideshow.control.Stopwatch;
import jp.toastkid.slideshow.message.HidingMenuMessage;
import jp.toastkid.slideshow.message.Message;
import jp.toastkid.slideshow.message.MoveMessage;
import jp.toastkid.slideshow.message.PdfMessage;
import jp.toastkid.slideshow.message.QuitMessage;

import java.util.concurrent.TimeUnit;

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

    /** Auto play button. */
    @FXML
    private Button autoPlayButton;

    /** Message sender. */
    private final Subject<Message> messenger;

    /** Auto play disposable. */
    private Disposable autoPlay;

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
     * Auto play.
     */
    @FXML
    private void autoPlay() {
        if (autoPlay != null && !autoPlay.isDisposed()) {
            autoPlay.dispose();
            autoPlayButton.setText("Auto play");
            return;
        }
        autoPlayButton.setText("Stop");
        autoPlay = Observable.interval(5, TimeUnit.SECONDS).forEach(i -> forward());
    }

    /**
     * Set min and max value.
     * @param min min value
     * @param max max value
     */
    void setRange(final int min, final int max) {
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
     * @return Root pane
     */
    public Pane getRoot() {
        return root;
    }

    /**
     * Set focus on index input.
     */
    void setFocus() {
        indexInput.requestFocus();
    }

    /**
     * Return message subscription.
     * @return {@link Consumer}.
     */
    public Disposable subscribe(final Consumer<Message> c) {
        return messenger.subscribe(c);
    }

}
