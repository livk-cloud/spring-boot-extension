package com.livk.auto.service.processor;

import com.google.auto.service.AutoService;
import com.livk.auto.service.annotation.AotFactories;
import com.livk.auto.service.annotation.SpringFactories;

import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.util.Optional;
import java.util.Set;

/**
 * @author livk
 */
@AutoService(Processor.class)
public class AotFactoriesProcessor extends AbstractFactoriesProcessor {

	private static final Class<AotFactories> SUPPORT_CLASS = AotFactories.class;

	static final String AOT_LOCATION = "META-INF/spring/aot.factories";

	protected AotFactoriesProcessor() {
		super(AOT_LOCATION);
	}

	@Override
	protected Set<Class<?>> getSupportedAnnotation() {
		return Set.of(SUPPORT_CLASS);
	}

	@Override
	protected Set<? extends Element> getElements(RoundEnvironment roundEnv) {
		return roundEnv.getElementsAnnotatedWith(SUPPORT_CLASS);
	}

	@Override
	protected Optional<Set<TypeElement>> getAnnotationAttributes(Element element) {
		return TypeElements.getAnnotationAttributes(element, SUPPORT_CLASS, VALUE);
	}

}
