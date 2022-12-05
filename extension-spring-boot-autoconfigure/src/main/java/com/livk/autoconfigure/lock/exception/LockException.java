package com.livk.autoconfigure.lock.exception;

/**
 * <p>
 * LockException
 * </p>
 *
 * @author livk
 *
 */
public class LockException extends RuntimeException {
    public LockException() {
        super();
    }

    public LockException(String message) {
        super(message);
    }

    public LockException(String message, Throwable cause) {
        super(message, cause);
    }

    public LockException(Throwable cause) {
        super(cause);
    }

    protected LockException(String message, Throwable cause, boolean enableSuppression,
                            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
