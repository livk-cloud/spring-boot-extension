package com.livk.crypto.exception;

import lombok.Getter;

/**
 * The type Metadata illegal exception.
 */
public class MetadataIllegalException extends RuntimeException {

    @Getter
    private final String action;

    /**
     * Instantiates a new Metadata illegal exception.
     *
     * @param message the message
     */
    public MetadataIllegalException(String message, String action) {
        super(message);
        this.action = action;
    }
}
