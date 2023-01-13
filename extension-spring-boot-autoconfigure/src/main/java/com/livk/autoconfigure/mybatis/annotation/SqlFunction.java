package com.livk.autoconfigure.mybatis.annotation;

import com.livk.autoconfigure.mybatis.constant.FunctionEnum;
import com.livk.autoconfigure.mybatis.enums.SqlFill;
import com.livk.autoconfigure.mybatis.handler.FunctionHandle;
import com.livk.autoconfigure.mybatis.handler.NullFunction;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * SqlFunction
 * </p>
 *
 * @author livk
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface SqlFunction {

    /**
     * <p>
     * {@link SqlFunction#supplier()}
     * </p>
     * <p>
     * {@link SqlFunction#time()}
     * </p>
     * <p>
     * 两个必须指定一个，否则无法注入
     * </p>
     *
     * @return the sql fill
     */
    SqlFill fill();

    /**
     * 优先级低于{@link SqlFunction#time()}
     *
     * @return the class
     */
    Class<? extends FunctionHandle<?>> supplier() default NullFunction.class;

    /**
     * 优先级高于 {@link SqlFunction#supplier()}
     *
     * @return the function enum
     */
    FunctionEnum time() default FunctionEnum.DEFAULT;

}
