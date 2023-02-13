package com.livk.commons.collect;

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
 * @param <T> the type parameter
 * @author livk
 */
public class EnumerationSpliterator<T> extends Spliterators.AbstractSpliterator<T> {

    private final Enumeration<T> enumeration;

    /**
     * Instantiates a new Enumeration spliterator.
     *
     * @param enumeration               the enumeration
     * @param additionalCharacteristics the additional characteristics
     */
    protected EnumerationSpliterator(Enumeration<T> enumeration, int additionalCharacteristics) {
        super(Long.MAX_VALUE, additionalCharacteristics);
        this.enumeration = enumeration;
    }

    /**
     * Spliterator unknown size spliterator.
     *
     * @param <T>         the type parameter
     * @param enumeration the enumeration
     * @return the spliterator
     */
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
