package com.livk.autoconfigure.easyexcel.exception;

import lombok.Getter;

/**
 * <p>
 * ExcelExportException
 * </p>
 *
 * @author livk
 */
@Getter
public class ExcelExportException extends RuntimeException {

    /**
     * Instantiates a new Excel export exception.
     *
     * @param message the message
     */
    public ExcelExportException(String message) {
        super(message);
    }

    /**
     * Instantiates a new Excel export exception.
     *
     * @param message the message
     * @param cause   the cause
     */
    public ExcelExportException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Instantiates a new Excel export exception.
     *
     * @param cause the cause
     */
    public ExcelExportException(Throwable cause) {
        super(cause);
    }

}
