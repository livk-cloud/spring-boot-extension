package com.livk.autoconfigure.lock.exception;

/**
 * <p>
 * UnSupportLockException
 * </p>
 *
 * @author livk
 */
public class UnSupportLockException extends RuntimeException {

    /**
     * Instantiates a new Un support lock exception.
     *
     * @param message the message
     */
    public UnSupportLockException(String message) {
        super(message);
    }

    /**
     * Instantiates a new Un support lock exception.
     *
     * @param message the message
     * @param cause   the cause
     */
    public UnSupportLockException(String message, Throwable cause) {
        super(message, cause);
    }
}
