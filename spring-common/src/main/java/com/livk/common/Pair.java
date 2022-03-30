package com.livk.common;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;

/**
 * <p>
 * Pair
 * </p>
 *
 * @author livk
 * @date 2022/3/30
 */
@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class Pair<K, V> implements Serializable, Cloneable {

    @Serial
    private static final long serialVersionUID = -2303547536834226401L;

    private final K key;
    private final V value;

    public static <K, V> Pair<K, V> of(K key, V value) {
        return new Pair<>(key, value);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Pair<K, V> clone() {
        try {
            return (Pair<K, V>) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
