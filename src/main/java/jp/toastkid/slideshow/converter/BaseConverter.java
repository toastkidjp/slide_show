/*
 * Copyright (c) 2017 toastkidjp.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompany this distribution.
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package jp.toastkid.slideshow.converter;

/**
 * Base of Converter.
 *
 * @author Toast kid
 */
public abstract class BaseConverter implements Converter {

    /** System line separator. */
    protected static final String LINE_SEPARATOR = System.lineSeparator();

    /** CSS title. */
    private String css;

    /** Footer text. */
    private String footerText;

    @Override
    public String getCss() {
        return css;
    }

    /**
     * Setter of css.
     * @param css
     */
    protected void setCss(final String css) {
        this.css = css;
    }

    /**
     * Getter of footer text.
     * @return footerText
     */
    public String getFooterText() {
        return footerText;
    }

    /**
     * Setter of footer text.
     * @param footerText
     */
    public void setFooterText(final String footerText) {
        this.footerText = footerText;
    }

}
