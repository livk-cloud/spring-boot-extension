package com.livk.handler.time;

import com.livk.handler.FunctionHandle;

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
