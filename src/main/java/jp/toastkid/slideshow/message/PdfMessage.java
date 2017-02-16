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
