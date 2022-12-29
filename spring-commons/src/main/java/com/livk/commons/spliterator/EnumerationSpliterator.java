package com.livk.commons.spliterator;

import java.util.Enumeration;
import java.util.Objects;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;

/**
 * <p>
 * EnumerationSpliterator
 * </p>
 *
 * @author livk
 * @date 2022/12/29
 */
public class EnumerationSpliterator<T> extends Spliterators.AbstractSpliterator<T> {

    private final Enumeration<T> enumeration;

    protected EnumerationSpliterator(Enumeration<T> enumeration, int additionalCharacteristics) {
        super(Long.MAX_VALUE, additionalCharacteristics);
        this.enumeration = enumeration;
    }

    public static <T> Spliterator<T> spliteratorUnknownSize(Enumeration<T> enumeration) {
        return new EnumerationSpliterator<>(Objects.requireNonNull(enumeration), Spliterator.ORDERED);
    }

    @Override
    public boolean tryAdvance(Consumer<? super T> action) {
        if (enumeration.hasMoreElements()) {
            action.accept(enumeration.nextElement());
            return true;
        }
        return false;
    }

    @Override
    public void forEachRemaining(Consumer<? super T> action) {
        while (enumeration.hasMoreElements()) {
            action.accept(enumeration.nextElement());
        }
    }
}
