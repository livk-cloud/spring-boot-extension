package com.livk.autoconfigure.mapstruct.support;

import com.livk.autoconfigure.mapstruct.converter.Converter;
import com.livk.autoconfigure.mapstruct.converter.ConverterPair;
import com.livk.commons.util.ObjectUtils;
import lombok.experimental.UtilityClass;
import org.springframework.core.GenericTypeResolver;

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
        Class<?>[] types = GenericTypeResolver.resolveTypeArguments(converter.getClass(), Converter.class);
        if (ObjectUtils.isEmpty(types)) {
            return null;
        }
        return ConverterPair.of(types[0], types[1]);
    }
}
