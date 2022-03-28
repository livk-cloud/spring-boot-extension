package com.livk.function;

import org.springframework.util.StringUtils;

import java.lang.invoke.SerializedLambda;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.Function;

/**
 * <p>
 * FieldFunction
 * </p>
 *
 * @author livk
 * @date 2022/2/25
 */
@FunctionalInterface
public interface FieldFunction<T> extends Function<T, Object> {

	default String getFieldName() {
		try {
			Method method = this.getClass().getDeclaredMethod("writeReplace");
			method.setAccessible(true);
			SerializedLambda serializedLambda = (SerializedLambda) method.invoke(this);
			String getter = serializedLambda.getImplMethodName();
			if (getter.startsWith("get")) {
				getter = getter.substring(3);
			}
			else if (getter.startsWith("is")) {
				getter = getter.substring(2);
			}
			else {
				return null;
			}
			if (!StringUtils.hasText(getter)) {
				return null;
			}
			char[] cs = getter.toCharArray();
			cs[0] += 32;
			return String.valueOf(cs);
		}
		catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

}
