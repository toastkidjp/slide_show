/*
 * Copyright (c) 2017 toastkidjp.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompany this distribution.
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package jp.toastkid.slideshow.slide;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.text.Font;

/**
 * Slide's line factory.
 *
 * @author Toast kid
 */
class LineFactory {

    /** Title label's font. */
    private static final Font LINE_FONT = new Font(80);

    /**
     * Deny make instance.
     */
    private LineFactory() {
        // NOP.
    }

    /**
     * Make simple line.
     * @param text line's text
     * @return Label
     */
    static Label normal(final String text) {
        final Label line = new Label(text);
        line.getStyleClass().add("line");
        line.setFont(LINE_FONT);
        line.setWrapText(true);
        line.setMinHeight(Region.USE_PREF_SIZE);
        return line;
    }

    /**
     * Return centering item.
     * @param n Node
     * @return
     */
    static Node centering(final Node n) {
        final HBox forCentering = new HBox(n);
        forCentering.setAlignment(Pos.CENTER);
        return forCentering;
    }

    /**
     * Return centered label.
     * @param text
     * @return Node
     */
    static Node centeredText(final String text) {
        final HBox forCentering = new HBox(normal(text));
        forCentering.setAlignment(Pos.CENTER);
        return forCentering;
    }
}
