package com.livk.function;

import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * <p>
 * PresentOrElseHandler
 * </p>
 *
 * @author livk
 * @date 2021/11/26
 */
@FunctionalInterface
public interface Present<T> {

	void present(Consumer<T> action, Runnable emptyAction);

	static <T> Present<T> handler(T t, Predicate<T> predicate) {
		return (action, emptyAction) -> {
			if (predicate.test(t))
				action.accept(t);
			else
				emptyAction.run();
		};
	}

}
