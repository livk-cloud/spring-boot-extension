package com.livk.autoconfigure.qrcode.exception;

import lombok.Getter;

/**
 * <p>
 * QRCodeException
 * </p>
 *
 * @author livk
 */
@Getter
public class QRCodeException extends RuntimeException {
    /**
     * Instantiates a new Qr code exception.
     *
     * @param message the message
     */
    public QRCodeException(String message) {
        super(message);
    }

    /**
     * Instantiates a new Qr code exception.
     *
     * @param message the message
     * @param cause   the cause
     */
    public QRCodeException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Instantiates a new Qr code exception.
     *
     * @param cause the cause
     */
    public QRCodeException(Throwable cause) {
        super(cause);
    }
}
