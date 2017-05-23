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
