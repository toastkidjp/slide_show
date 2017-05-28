/*
 * Copyright (c) 2017 toastkidjp.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompany this distribution.
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package jp.toastkid.slideshow.message;

/**
 * Make marker message of generating pdf.
 * @author Toast kid
 *
 */
public class PdfMessage implements Message {

    /**
     * Empty constructor.
     */
    private PdfMessage() {
        // NOP.
    }

    /**
     * Make empty message.
     * @return
     */
    public static PdfMessage make() {
        return new PdfMessage();
    }

}
