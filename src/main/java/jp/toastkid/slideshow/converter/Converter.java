/*
 * Copyright (c) 2017 toastkidjp.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompany this distribution.
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package jp.toastkid.slideshow.converter;

import java.util.List;

import jp.toastkid.slideshow.slide.Slide;

/**
 * Slide converter interface.
 *
 * @author Toast kid
 */
public interface Converter {

    /**
     * Convert text to Slide list.
     * @return
     */
    public List<Slide> convert();

    /**
     * Getter of css.
     * @return css
     */
    public String getCss();

    /**
     * Getter of footer text.
     * @return footer text
     */
    public String getFooterText();
}
