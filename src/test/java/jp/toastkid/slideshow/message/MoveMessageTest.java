package jp.toastkid.slideshow.message;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import jp.toastkid.slideshow.message.MoveMessage.Command;

/**
 * {@link MoveMessage}'s test case.
 * @author Toast kid
 *
 */
public class MoveMessageTest {

    /**
     * Test of {@link MoveMessage#makeStart()}.
     */
    @Test
    public void test_makeStart() {
        final MoveMessage m = MoveMessage.makeStart();
        assertEquals(Command.START, m.getCommand());
        assertEquals(1,             m.getTo());
    }

    /**
     * Test of {@link MoveMessage#makeBack()}.
     */
    @Test
    public void test_makeBack() {
        final MoveMessage m = MoveMessage.makeBack();
        assertEquals(Command.BACK, m.getCommand());
        assertEquals(-1,           m.getTo());
    }

    /**
     * Test of {@link MoveMessage#makeForward()}.
     */
    @Test
    public void test_makeForward() {
        final MoveMessage m = MoveMessage.makeForward();
        assertEquals(Command.FORWARD, m.getCommand());
        assertEquals(-1,              m.getTo());
    }

    /**
     * Test of {@link MoveMessage#makeTo()}.
     */
    @Test(expected=IllegalArgumentException.class)
    public void test_makeTo_nega() {
        MoveMessage.makeTo(-1);
    }

    /**
     * Test of {@link MoveMessage#makeTo()}.
     */
    @Test
    public void test_makeTo_zero() {
        final MoveMessage m = MoveMessage.makeTo(0);
        assertEquals(Command.TO, m.getCommand());
        assertEquals(0,          m.getTo());
    }

    /**
     * Test of {@link MoveMessage#makeTo()}.
     */
    @Test
    public void test_makeTo_posi() {
        final MoveMessage m = MoveMessage.makeTo(1);
        assertEquals(Command.TO, m.getCommand());
        assertEquals(1,          m.getTo());
    }

}
