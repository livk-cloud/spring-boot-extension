package com.livk.annotation;

import com.livk.constant.FunctionEnum;
import com.livk.enums.SqlFill;
import com.livk.handler.FunctionHandle;
import com.livk.handler.NullFunction;

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
	 */
	SqlFill fill();

	/**
	 * 优先级低于{@link SqlFunction#time()}
	 */
	Class<? extends FunctionHandle<?>> supplier() default NullFunction.class;

	/**
	 * 优先级高于 {@link SqlFunction#supplier()}
	 */
	FunctionEnum time() default FunctionEnum.DEFAULT;

}
