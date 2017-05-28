/*
 * Copyright (c) 2017 toastkidjp.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompany this distribution.
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package jp.toastkid.slideshow.message;

/**
 * Slide move message.
 * @author Toast kid
 *
 */
public class MoveMessage implements Message {

    /**
     * Commands.
     *
     * @author Toast kid
     *
     */
    public enum Command {
        START, BACK, FORWARD, TO
    }

    /** This message's command. */
    private final Command command;

    /** This message's index of move to. */
    private final int to;

    /**
     * Internal constructor.
     * @param command
     * @param to
     */
    private MoveMessage(final Command command, final int to) {
        this.command = command;
        this.to      = to;
    }

    /**
     * Return this message's command.
     * @return
     */
    public Command getCommand() {
        return this.command;
    }

    /**
     * Return this message's target index.
     * @return
     */
    public int getTo() {
        return this.to;
    }

    /**
     * Make start event.
     * @return
     */
    public static MoveMessage makeStart() {
        return new MoveMessage(Command.START, 1);
    }

    /**
     * Make back event.
     * @return
     */
    public static MoveMessage makeBack() {
        return new MoveMessage(Command.BACK, -1);
    }

    /**
     * Make forward event.
     * @return
     */
    public static MoveMessage makeForward() {
        return new MoveMessage(Command.FORWARD, -1);
    }

    /**
     * Make move event with target index.
     * @param to
     * @return
     */
    public static MoveMessage makeTo(final int to) {
        if (to < 0) {
            throw new IllegalArgumentException("You should pass positive int.");
        }
        return new MoveMessage(Command.TO, to);
    }

}
