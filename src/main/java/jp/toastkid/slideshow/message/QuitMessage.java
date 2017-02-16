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
