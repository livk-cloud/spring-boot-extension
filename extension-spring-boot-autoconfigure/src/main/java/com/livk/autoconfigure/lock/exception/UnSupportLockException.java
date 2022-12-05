package com.livk.autoconfigure.lock.exception;

/**
 * <p>
 * UnSupportLockException
 * </p>
 *
 * @author livk
 *
 */
public class UnSupportLockException extends RuntimeException {

    public UnSupportLockException(String message) {
        super(message);
    }

    public UnSupportLockException(String message, Throwable cause) {
        super(message, cause);
    }
}
