package com.livk.autoconfigure.oss.exception;

/**
 * The type Oss client factory not found exception.
 */
public class OSSClientFactoryNotFoundException extends RuntimeException {

    /**
     * Instantiates a new Oss client factory not found exception.
     */
    public OSSClientFactoryNotFoundException() {
        super();
    }

    /**
     * Instantiates a new Oss client factory not found exception.
     *
     * @param message the message
     */
    public OSSClientFactoryNotFoundException(String message) {
        super(message);
    }

    /**
     * Instantiates a new Oss client factory not found exception.
     *
     * @param message the message
     * @param cause   the cause
     */
    public OSSClientFactoryNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Instantiates a new Oss client factory not found exception.
     *
     * @param cause the cause
     */
    public OSSClientFactoryNotFoundException(Throwable cause) {
        super(cause);
    }

    /**
     * Instantiates a new Oss client factory not found exception.
     *
     * @param message            the message
     * @param cause              the cause
     * @param enableSuppression  the enable suppression
     * @param writableStackTrace the writable stack trace
     */
    protected OSSClientFactoryNotFoundException(String message, Throwable cause, boolean enableSuppression,
                                                boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
