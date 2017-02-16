package jp.toastkid.slideshow.message;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

/**
 * {@link QuitMessage}'s test case.
 * @author Toast kid
 *
 */
public class QuitMessageTest {

    /**
     * Test of {@link QuitMessage#make()}.
     */
    @Test
    public void test() {
        assertNotNull(QuitMessage.make());
    }

}
