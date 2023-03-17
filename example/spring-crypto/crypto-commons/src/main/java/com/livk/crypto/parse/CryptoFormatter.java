package com.livk.crypto.parse;

import com.livk.commons.spring.context.SpringContextHolder;
import com.livk.crypto.CryptoType;
import org.springframework.core.GenericTypeResolver;
import org.springframework.core.ResolvableType;
import org.springframework.format.Formatter;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * The interface Spring context parser.
 *
 * @param <T> the type parameter
 * @author livk
 */
public interface CryptoFormatter<T> extends Formatter<T> {

    /**
     * From context object provider.
     *
     * @return the object provider
     */
    static Map<Class<?>, List<CryptoFormatter<?>>> fromContext() {
        ResolvableType resolvableType = ResolvableType.forClass(CryptoFormatter.class);
        return SpringContextHolder.<CryptoFormatter<?>>getBeanProvider(resolvableType)
                .orderedStream()
                .collect(Collectors.groupingBy(CryptoFormatter::supportClass));
    }

    /**
     * Support class class.
     *
     * @return the class
     */
    default Class<?> supportClass() {
        return GenericTypeResolver.resolveTypeArgument(this.getClass(), CryptoFormatter.class);
    }

    /**
     * Type crypto type.
     *
     * @return the crypto type
     */
    CryptoType type();
}
