package com.dragonslayers.exceptions;

/**
 * Thrown when the player tries an action the game rules do not allow.
 */
public class InvalidActionException extends RuntimeException {

    /**
     * Creates the exception with a message.
     *
     * @param message what went wrong, shown to the player
     */
    public InvalidActionException(String message) {
        super(message);
    }
}
