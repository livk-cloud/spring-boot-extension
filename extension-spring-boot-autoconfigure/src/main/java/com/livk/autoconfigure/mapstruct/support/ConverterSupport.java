package com.livk.autoconfigure.mapstruct.support;

import com.livk.autoconfigure.mapstruct.converter.Converter;
import com.livk.autoconfigure.mapstruct.converter.ConverterPair;
import lombok.experimental.UtilityClass;
import org.springframework.core.ResolvableType;

/**
 * The type Converter support.
 *
 * @author livk
 */
@UtilityClass
public class ConverterSupport {

    /**
     * Parser converter pair.
     *
     * @param converter the converter
     * @return the converter pair
     */
    public ConverterPair parser(Converter<?, ?> converter) {
        ResolvableType resolvableType = ResolvableType.forClass(converter.getClass());
        Class<?> sourceType = resolvableType.getInterfaces()[0].getInterfaces()[0].getGeneric(0).resolve();
        Class<?> targetType = resolvableType.getInterfaces()[0].getInterfaces()[0].getGeneric(1).resolve();
        return ConverterPair.of(sourceType, targetType);
    }
}
