package com.livk.batch.support;

import com.livk.util.BeanUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.file.LineMapper;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * <p>
 * CsvLineMapper
 * </p>
 *
 * @author livk
 * @date 2022/6/20
 */
@Slf4j
public class CsvLineMapper<T> implements LineMapper<T> {

    private final Class<T> targetClass;

    private String[] fields;

    private String delimiter;

    public CsvLineMapper(Class<T> targetClass) {
        this.targetClass = targetClass;
    }

    public void setFiled(String delimiter, String... fields) {
        this.fields = fields;
        this.delimiter = delimiter;
    }

    @Nonnull
    @Override
    public T mapLine(@Nonnull String line, int lineNumber) {
        T instance = BeanUtils.instantiateClass(targetClass);
        String[] fieldArray = line.split(delimiter);
        if (fieldArray.length != fields.length) {
            throw new ArrayIndexOutOfBoundsException();
        }
        for (int i = 0; i < fields.length; i++) {
            set(instance, fields[i], fieldArray[i]);
        }
        log.info("{}", instance);
        return instance;
    }

    private void set(T t, String field, String valueStr) {
        try {
            Object value;
            Class<?> targetClass = t.getClass();
            Field declaredField = targetClass.getDeclaredField(field);
            Class<?> type = declaredField.getType();
            if (Integer.class.equals(type)) {
                value = Integer.parseInt(valueStr);
            } else if (Long.class.equals(type)) {
                value = Long.parseLong(valueStr);
            } else if (Float.class.equals(type)) {
                value = Float.parseFloat(valueStr);
            } else if (Double.class.equals(type)) {
                value = Double.parseDouble(valueStr);
            } else if (Boolean.class.equals(type)) {
                value = Boolean.parseBoolean(valueStr);
            } else {
                value = valueStr;
            }
            field = field.substring(0, 1).toUpperCase() + field.substring(1);
            Method method = targetClass.getMethod("set" + field, type);
            method.invoke(t, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
