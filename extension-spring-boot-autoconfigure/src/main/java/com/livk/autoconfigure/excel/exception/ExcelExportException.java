package com.livk.autoconfigure.excel.exception;

import lombok.Getter;

/**
 * <p>
 * ExcelExportException
 * </p>
 *
 * @author livk
 * @date 2022/4/26
 */
@Getter
public class ExcelExportException extends RuntimeException {

    private final int code = 511;

    public ExcelExportException(String message) {
        super(message);
    }

    public ExcelExportException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExcelExportException(Throwable cause) {
        super(cause);
    }

}
