package com.livk.autoconfigure.mapstruct.exception;

/**
 * <p>
 * ConverterNotFoundException
 * </p>
 *
 * @author livk
 */
public class ConverterNotFoundException extends RuntimeException {

    /**
     * Instantiates a new Converter not found exception.
     */
    public ConverterNotFoundException() {
        super();
    }

    /**
     * Instantiates a new Converter not found exception.
     *
     * @param message the message
     */
    public ConverterNotFoundException(String message) {
        super(message);
    }

    /**
     * Instantiates a new Converter not found exception.
     *
     * @param message the message
     * @param cause   the cause
     */
    public ConverterNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Instantiates a new Converter not found exception.
     *
     * @param cause the cause
     */
    public ConverterNotFoundException(Throwable cause) {
        super(cause);
    }

    /**
     * Instantiates a new Converter not found exception.
     *
     * @param message            the message
     * @param cause              the cause
     * @param enableSuppression  the enable suppression
     * @param writableStackTrace the writable stack trace
     */
    protected ConverterNotFoundException(String message, Throwable cause, boolean enableSuppression,
                                         boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
