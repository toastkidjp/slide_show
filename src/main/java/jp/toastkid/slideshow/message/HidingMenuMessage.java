/*
 * Copyright (c) 2017 toastkidjp.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompany this distribution.
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package jp.toastkid.slideshow.message;

/**
 * Make visibility control message
 * @author Toast kid
 *
 */
public class HidingMenuMessage implements Message {

    /**
     * Empty constructor.
     */
    private HidingMenuMessage() {
        // NOP.
    }

    /**
     * Make marker instance.
     * @return
     */
    public static HidingMenuMessage make() {
        return new HidingMenuMessage();
    }
}
