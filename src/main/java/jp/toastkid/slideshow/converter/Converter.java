package jp.toastkid.slideshow.converter;

import org.eclipse.collections.api.list.MutableList;

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
    public MutableList<Slide> convert();

    /**
     * Getter of css.
     * @return
     */
    public String getCss();
}
