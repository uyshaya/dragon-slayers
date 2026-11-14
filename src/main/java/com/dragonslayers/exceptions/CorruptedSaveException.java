package com.dragonslayers.exceptions;

/**
 * Thrown when a save file exists but cannot be read.
 */
public class CorruptedSaveException extends Exception {

    /**
     * Creates the exception with a message.
     *
     * @param message what went wrong
     */
    public CorruptedSaveException(String message) {
        super(message);
    }

    /**
     * Creates the exception with a message and the original error.
     *
     * @param message what went wrong
     * @param cause   the error that caused it
     */
    public CorruptedSaveException(String message, Throwable cause) {
        super(message, cause);
    }
}
