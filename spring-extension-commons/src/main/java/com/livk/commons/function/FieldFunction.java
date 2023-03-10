package com.livk.commons.function;

import java.io.Serializable;
import java.util.function.Function;

/**
 * The interface Field function.
 *
 * @param <T> the type parameter
 * @author livk
 */
@FunctionalInterface
public interface FieldFunction<T> extends Function<T, Object>, Serializable {

}
