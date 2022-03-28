package com.livk.constant;

import com.livk.handler.*;
import com.livk.handler.time.DateFunction;
import com.livk.handler.time.LocalDateFunction;
import com.livk.handler.time.LocalDateTimeFunction;
import com.livk.handler.time.TimestampFunction;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * <p>
 * TimeEnum
 * </p>
 *
 * @author livk
 * @date 2022/3/28
 */
@Getter
@RequiredArgsConstructor
public enum TimeEnum {
    DEFAULT(new NullFunction()),
    DATE(new DateFunction()),
    LOCAL_DATE(new LocalDateFunction()),
    LOCAL_DATE_TIME(new LocalDateTimeFunction()),
    TIMESTAMP(new TimestampFunction());

    private final FunctionHandle<?> function;

    public Object handler() {
        return function.handler();
    }
}
