package com.livk.handler;

import java.time.LocalDate;

/**
 * <p>
 * LocalDateFunction
 * </p>
 *
 * @author livk
 * @date 2022/3/28
 */
public class LocalDateFunction implements FunctionHandle<LocalDate> {
    @Override
    public LocalDate handler() {
        return LocalDate.now();
    }
}
