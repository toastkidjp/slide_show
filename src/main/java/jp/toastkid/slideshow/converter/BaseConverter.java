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

}
