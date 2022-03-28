package com.livk.handler;

import java.time.LocalDateTime;

/**
 * <p>
 * LocalDateTimeFunction
 * </p>
 *
 * @author livk
 * @date 2022/3/28
 */
public class LocalDateTimeFunction implements FunctionHandle<LocalDateTime> {

    @Override
    public LocalDateTime handler() {
        return LocalDateTime.now();
    }
}
