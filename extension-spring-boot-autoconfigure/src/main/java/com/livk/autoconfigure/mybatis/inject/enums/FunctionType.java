package com.livk.autoconfigure.mybatis.inject.enums;

import com.livk.autoconfigure.mybatis.inject.handler.FunctionHandle;
import com.livk.autoconfigure.mybatis.inject.handler.NullFunction;
import com.livk.autoconfigure.mybatis.inject.handler.time.DateFunction;
import com.livk.autoconfigure.mybatis.inject.handler.time.LocalDateFunction;
import com.livk.autoconfigure.mybatis.inject.handler.time.LocalDateTimeFunction;
import com.livk.autoconfigure.mybatis.inject.handler.time.TimestampFunction;
import lombok.RequiredArgsConstructor;

/**
 * <p>
 * TimeEnum
 * </p>
 *
 * @author livk
 */
@RequiredArgsConstructor
public enum FunctionType implements FunctionHandle<Object> {

    /**
     * The Default.
     */
    DEFAULT(new NullFunction()),
    /**
     * The Date.
     */
    DATE(new DateFunction()),
    /**
     * The Local date.
     */
    LOCAL_DATE(new LocalDateFunction()),
    /**
     * The Local date time.
     */
    LOCAL_DATE_TIME(new LocalDateTimeFunction()),
    /**
     * The Timestamp.
     */
    TIMESTAMP(new TimestampFunction());

    private final FunctionHandle<?> function;

    @Override
    public Object handler() {
        return function.handler();
    }

    /**
     * Handler t.
     *
     * @param <T>         the type parameter
     * @param targetClass the target class
     * @return the t
     */
    @SuppressWarnings("unchecked")
    public <T> T handler(Class<T> targetClass) {
        Object value = handler();
        if (targetClass.isInstance(value)) {
            return (T) value;
        }
        throw new ClassCastException("class " + value.getClass() + " can not to be class " + targetClass);
    }

}
