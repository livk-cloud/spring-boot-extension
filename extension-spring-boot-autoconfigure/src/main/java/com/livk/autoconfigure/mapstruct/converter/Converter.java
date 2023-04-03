package com.livk.autoconfigure.mapstruct.converter;

import com.livk.commons.util.ObjectUtils;
import org.springframework.core.GenericTypeResolver;

/**
 * <p>
 * Converter
 * </p>
 *
 * @param <S> the type parameter
 * @param <T> the type parameter
 * @author livk
 */
public interface Converter<S, T> {

    /**
     * Gets source.
     *
     * @param t the t
     * @return the source
     */
    S getSource(T t);

    /**
     * Gets target.
     *
     * @param s the s
     * @return the target
     */
    T getTarget(S s);

    /**
     * Type converter pair.
     *
     * @return the converter pair
     */
    default ConverterPair type() {
        Class<?>[] types = GenericTypeResolver.resolveTypeArguments(this.getClass(), Converter.class);
        if (ObjectUtils.isEmpty(types)) {
            return null;
        }
        return ConverterPair.of(types[0], types[1]);
    }

}
