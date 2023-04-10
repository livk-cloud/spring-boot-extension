package com.livk.autoconfigure.limit.exception;

/**
 * The type Limit exception.
 *
 * @author livk
 */
public class LimitException extends RuntimeException {

    /**
     * Instantiates a new Limit exception.
     */
    public LimitException() {
        super();
    }

    /**
     * Instantiates a new Limit exception.
     *
     * @param message the message
     */
    public LimitException(String message) {
        super(message);
    }

    /**
     * Instantiates a new Limit exception.
     *
     * @param message the message
     */
    public LimitException(String message, Throwable cause) {
        super(message, cause);
    }
}
