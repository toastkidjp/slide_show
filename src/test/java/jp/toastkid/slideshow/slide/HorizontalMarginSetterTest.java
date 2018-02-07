/*
 * Copyright (c) 2018 toastkidjp.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompany this distribution.
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html.
 */

package jp.toastkid.slideshow.slide;

import javafx.geometry.Insets;
import javafx.scene.layout.VBox;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * {@link HorizontalMarginSetter}'s test cases.
 *
 * @author toastkidjp
 */
public class HorizontalMarginSetterTest {

    /**
     * Test of invoke.
     */
    @Test
    public void test_invoke() {
        final VBox box = new VBox();
        HorizontalMarginSetter.invoke(box);

        final Insets margin = VBox.getMargin(box);
        assertEquals(0.0d, margin.getTop(), 0.0d);
        assertEquals(40.0d, margin.getRight(), 0.0d);
        assertEquals(0.0d, margin.getBottom(), 0.0d);
        assertEquals(40.0d, margin.getLeft(), 0.0d);
    }

}