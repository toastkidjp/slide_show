package jp.toastkid.slideshow.message;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

/**
 * {@link HidingMenuMessage}'s test case.
 * @author Toast kid
 *
 */
public class HidingMenuMessageTest {

    /**
     * Test of {@link HidingMenuMessage#make()}.
     */
    @Test
    public void test() {
        assertNotNull(HidingMenuMessage.make());
    }

}
