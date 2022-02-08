package com.livk.annotation;

import com.livk.enums.SqlFill;
import com.livk.handler.FunctionHandle;

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
 * @date 2022/1/29
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface SqlFunction {

    SqlFill fill();

    Class<? extends FunctionHandle<?>> supplier();
}
