package com.dragonslayers.exceptions;

/**
 * Thrown when loading a save slot that has no save file.
 */
public class SaveNotFoundException extends Exception {

    /**
     * Creates the exception with a message.
     *
     * @param message what went wrong
     */
    public SaveNotFoundException(String message) {
        super(message);
    }
}
