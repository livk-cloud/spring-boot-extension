package com.livk.auto.service.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author livk
 */
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface AotFactories {

	/**
	 * 指定aot.factories文件生成接口 如当前类仅有一个接口，则可以自动推断
	 * @return class
	 */
	Class<?> value() default Void.class;

}
