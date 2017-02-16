package jp.toastkid.slideshow.message;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

/**
 * {@link PdfMessage}'s test.
 * @author Toast kid
 *
 */
public class PdfMessageTest {

    /**
     * Test of {@link PdfMessage#make()}.
     */
    @Test
    public void test() {
        assertNotNull(PdfMessage.make());
    }

}
