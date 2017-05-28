/*
 * Copyright (c) 2017 toastkidjp.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompany this distribution.
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package jp.toastkid.slideshow.message;

/**
 * Quit message.
 * @author Toast kid
 *
 */
public class QuitMessage implements Message {

    /**
     * Empty constructor.
     */
    private QuitMessage() {
        // NOP.
    }

    /**
     * Make marker message.
     * @return
     */
    public static QuitMessage make() {
        return new QuitMessage();
    }
}
