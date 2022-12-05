package com.livk.autoconfigure.mybatis.handler.time;

import com.livk.autoconfigure.mybatis.handler.FunctionHandle;

import java.time.LocalDateTime;

/**
 * <p>
 * LocalDateTimeFunction
 * </p>
 *
 * @author livk
 *
 */
public class LocalDateTimeFunction implements FunctionHandle<LocalDateTime> {

    @Override
    public LocalDateTime handler() {
        return LocalDateTime.now();
    }

}
