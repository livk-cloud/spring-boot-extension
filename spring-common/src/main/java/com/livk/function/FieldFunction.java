package com.livk.function;

import java.io.Serializable;
import java.util.function.Function;

/**
 *
 * @author livk
 * @date 2022/2/25
 */
@FunctionalInterface
public interface FieldFunction<T> extends Function<T, Object>, Serializable {

}
