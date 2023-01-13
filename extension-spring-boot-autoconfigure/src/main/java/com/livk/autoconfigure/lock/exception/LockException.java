package com.livk.autoconfigure.lock.exception;

/**
 * <p>
 * LockException
 * </p>
 *
 * @author livk
 */
public class LockException extends RuntimeException {
    /**
     * Instantiates a new Lock exception.
     */
    public LockException() {
        super();
    }

    /**
     * Instantiates a new Lock exception.
     *
     * @param message the message
     */
    public LockException(String message) {
        super(message);
    }

    /**
     * Instantiates a new Lock exception.
     *
     * @param message the message
     * @param cause   the cause
     */
    public LockException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Instantiates a new Lock exception.
     *
     * @param cause the cause
     */
    public LockException(Throwable cause) {
        super(cause);
    }

    /**
     * Instantiates a new Lock exception.
     *
     * @param message            the message
     * @param cause              the cause
     * @param enableSuppression  the enable suppression
     * @param writableStackTrace the writable stack trace
     */
    protected LockException(String message, Throwable cause, boolean enableSuppression,
                            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
