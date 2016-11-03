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
     * Get path to css.
     * @param s Style name.
     * @return path to css.
     */
    public static String findUri(final String s) {
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
