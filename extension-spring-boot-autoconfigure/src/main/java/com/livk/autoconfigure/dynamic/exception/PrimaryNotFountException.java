package com.livk.autoconfigure.dynamic.exception;

/**
 * The type Primary not fount exception.
 */
public class PrimaryNotFountException extends RuntimeException {

    /**
     * Instantiates a new Primary not fount exception.
     */
    public PrimaryNotFountException() {
        super();
    }

    /**
     * Instantiates a new Primary not fount exception.
     *
     * @param message the message
     */
    public PrimaryNotFountException(String message) {
        super(message);
    }

    /**
     * Instantiates a new Primary not fount exception.
     *
     * @param message the message
     * @param cause   the cause
     */
    public PrimaryNotFountException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Instantiates a new Primary not fount exception.
     *
     * @param cause the cause
     */
    public PrimaryNotFountException(Throwable cause) {
        super(cause);
    }

    /**
     * Instantiates a new Primary not fount exception.
     *
     * @param message            the message
     * @param cause              the cause
     * @param enableSuppression  the enable suppression
     * @param writableStackTrace the writable stack trace
     */
    protected PrimaryNotFountException(String message, Throwable cause, boolean enableSuppression,
                                       boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
