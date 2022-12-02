package com.livk.autoconfigure.mapstruct.converter;

import com.livk.commons.domain.Pair;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.util.Assert;

/**
 * <p>
 * ConverterPair
 * </p>
 *
 * @author livk
 * @date 2022/11/26
 */
@Getter
@ToString
@EqualsAndHashCode
public class ConverterPair {
    private final Pair<Class<?>, Class<?>> pair;

    private ConverterPair(Class<?> sourceType, Class<?> targetType) {
        Assert.notNull(sourceType, "Source type must not be null");
        Assert.notNull(targetType, "Target type must not be null");
        this.pair = Pair.of(sourceType, targetType);
    }

    public static ConverterPair of(Class<?> sourceType, Class<?> targetType) {
        return new ConverterPair(sourceType, targetType);
    }
}
