package com.livk.commons.domain;

import com.livk.commons.function.FieldFunction;
import com.livk.commons.util.FieldUtils;
import lombok.*;
import org.springframework.cglib.beans.BeanMap;

import java.util.Map;

/**
 * <p>
 * Column
 * </p>
 *
 * @author livk
 */
@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Column<T> {

    private final T entity;

    private final Map<String, Object> map;

    @SuppressWarnings("unchecked")
    public static <T> Column<T> create(T t) {
        return new Column<T>(t, BeanMap.create(t));
    }

    public Object get(FieldFunction<T> function) {
        return map.get(FieldUtils.getFieldName(function));
    }
}
