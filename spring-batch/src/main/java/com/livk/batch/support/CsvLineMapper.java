package com.livk.batch.support;

import com.livk.util.BeanUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

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
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class CsvLineMapper<T> implements LineMapper<T> {

    private final Class<T> targetClass;

    private final String[] fields;

    private final String delimiter;

    public static <T> Builder<T> builder(Class<T> targetClass) {
        return new Builder<>(targetClass);
    }

    @NonNull
    @Override
    public T mapLine(@NonNull String line, int lineNumber) {
        T instance = BeanUtils.instantiateClass(targetClass);
        String[] fieldArray = line.split(delimiter);
        if (fieldArray.length != fields.length) {
            throw new ArrayIndexOutOfBoundsException();
        }
        for (int i = 0; i < fields.length; i++) {
            set(instance, fields[i], fieldArray[i]);
        }
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
            field = StringUtils.capitalize(field);
            Method method = targetClass.getMethod("set" + field, type);
            method.invoke(t, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Builder<T> {
        private final Class<T> targetClass;

        private String[] fields;

        private String delimiter;

        public Builder<T> fields(String... fields) {
            this.fields = fields;
            return this;
        }

        public Builder<T> delimiter(String delimiter) {
            this.delimiter = delimiter;
            return this;
        }

        public CsvLineMapper<T> build() {
            Assert.notNull(targetClass, "targetClass not null");
            Assert.notNull(fields, "fields not null");
            Assert.notNull(delimiter, "delimiter not null");
            return new CsvLineMapper<>(targetClass, fields, delimiter);
        }
    }
}
