/*
 * Copyright (c) 2017 toastkidjp.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompany this distribution.
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package jp.toastkid.slideshow.style;

import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Stylesheet manager.
 *
 * @author Toast kid
 */
public class StyleManager {

    /** CSS file extension. */
    private static final String CSS = ".css";

    /** Path of user defined css. */
    private static final Path USER_DIR = Paths.get("user/css/slide");

    /**
     * Deny make instance.
     */
    private StyleManager() {
        // NOP.
    }

    /**
     * Get path to css.
     * @param s Style name.
     * @return path to css.
     */
    public static String findUri(final String s) {
        if (s == null || s.length() == 0) {
            return s;
        }
        final URL resource = StyleManager.class.getResource("/css/slide/" + s.toLowerCase() + CSS);
        if (resource == null) {
            final File userDefined = new File(USER_DIR.toString(), s.toLowerCase() + CSS);
            if (!userDefined.exists()) {
                return (resource != null) ? resource.toString() : s.toString();
            }
            return userDefined.toURI().toString();
        }
        return resource.toString();
    }

}
