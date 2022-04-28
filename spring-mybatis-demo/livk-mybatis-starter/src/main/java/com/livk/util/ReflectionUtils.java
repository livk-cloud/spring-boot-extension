package com.livk.util;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.ArrayUtils;

import java.lang.reflect.Field;

/**
 * <p>
 * ObjectUtils
 * </p>
 *
 * @author livk
 * @date 2022/3/25
 */
@UtilityClass
public class ReflectionUtils {

	public Field[] getFields(Object parameter) {
		var declaredFields = parameter.getClass().getDeclaredFields();
		if (parameter.getClass().getSuperclass() != null) {
			var superFiled = parameter.getClass().getSuperclass().getDeclaredFields();
			declaredFields = ArrayUtils.addAll(declaredFields, superFiled);
		}
		return declaredFields;
	}

	public void set(Field field, Object parameter, Object value) throws IllegalAccessException {
		field.setAccessible(true);
		field.set(parameter, value);
	}

}
