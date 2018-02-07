/*
 * Copyright (c) 2018 toastkidjp.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompany this distribution.
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html.
 */

package jp.toastkid.slideshow.slide;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.VBox;

/**
 * @author toastkidjp
 */
class HorizontalMarginSetter {

    /**
     * Common horizontal insets.
     */
    private static final Insets INSETS
            = new javafx.geometry.Insets(0.0d, 40.0d, 0.0d, 40.0d);

    /**
     * Set horizontal margin.
     *
     * @param node {@link Node}
     */
    static void invoke(final Node node) {
        VBox.setMargin(node, INSETS);
    }
}
