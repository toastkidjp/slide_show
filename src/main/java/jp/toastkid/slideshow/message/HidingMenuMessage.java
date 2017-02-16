package jp.toastkid.slideshow.message;

/**
 * Make visibility control message
 * @author Toast kid
 *
 */
public class HidingMenuMessage implements Message {

    /**
     * Empty constructor.
     */
    private HidingMenuMessage() {
        // NOP.
    }

    /**
     * Make marker instance.
     * @return
     */
    public static HidingMenuMessage make() {
        return new HidingMenuMessage();
    }
}
