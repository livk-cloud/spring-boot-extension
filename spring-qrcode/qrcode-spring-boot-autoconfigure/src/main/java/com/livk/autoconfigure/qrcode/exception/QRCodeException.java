package com.livk.autoconfigure.qrcode.exception;

import lombok.Getter;

/**
 * <p>
 * QRCodeException
 * </p>
 *
 * @author livk
 * @date 2022/11/4
 */
@Getter
public class QRCodeException extends RuntimeException {
    private final int code = 512;

    public QRCodeException(String message) {
        super(message);
    }

    public QRCodeException(String message, Throwable cause) {
        super(message, cause);
    }

    public QRCodeException(Throwable cause) {
        super(cause);
    }
}
