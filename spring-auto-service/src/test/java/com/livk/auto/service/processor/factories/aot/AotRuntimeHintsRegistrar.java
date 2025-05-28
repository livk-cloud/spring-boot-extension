package com.livk.auto.service.processor.factories.aot;

import com.livk.auto.service.annotation.AotFactories;
import org.jspecify.annotations.NonNull;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;

/**
 * @author livk
 */
@AotFactories
public class AotRuntimeHintsRegistrar implements RuntimeHintsRegistrar {

	@Override
	public void registerHints(@NonNull RuntimeHints hints, ClassLoader classLoader) {

	}

}
