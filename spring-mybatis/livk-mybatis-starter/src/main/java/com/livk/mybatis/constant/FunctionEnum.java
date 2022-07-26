package com.livk.mybatis.constant;

import com.livk.mybatis.handler.FunctionHandle;
import com.livk.mybatis.handler.NullFunction;
import com.livk.mybatis.handler.time.DateFunction;
import com.livk.mybatis.handler.time.LocalDateFunction;
import com.livk.mybatis.handler.time.LocalDateTimeFunction;
import com.livk.mybatis.handler.time.TimestampFunction;
import lombok.RequiredArgsConstructor;

/**
 * <p>
 * TimeEnum
 * </p>
 *
 * @author livk
 * @date 2022/3/28
 */
@RequiredArgsConstructor
public enum FunctionEnum implements FunctionHandle<Object> {

    DEFAULT(new NullFunction()),
    DATE(new DateFunction()),
    LOCAL_DATE(new LocalDateFunction()),
    LOCAL_DATE_TIME(new LocalDateTimeFunction()),
    TIMESTAMP(new TimestampFunction());

    private final FunctionHandle<?> function;

    @Override
    public Object handler() {
        return function.handler();
    }

    @SuppressWarnings("unchecked")
    public <T> T handler(Class<T> targetClass) {
        Object value = handler();
        if (targetClass.isInstance(value)) {
            return (T) value;
        }
        throw new ClassCastException("class " + value.getClass() + " can not to be class " + targetClass);
    }

}
