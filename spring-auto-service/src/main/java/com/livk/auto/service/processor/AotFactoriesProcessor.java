package com.livk.auto.service.processor;

import com.google.auto.service.AutoService;
import com.livk.auto.service.annotation.AotFactories;

import javax.annotation.processing.Processor;
import java.lang.annotation.Annotation;

/**
 * @author livk
 */
@AutoService(Processor.class)
public class AotFactoriesProcessor extends AbstractFactoriesProcessor {

	private static final Class<AotFactories> SUPPORT_CLASS = AotFactories.class;

	static final String AOT_LOCATION = "META-INF/spring/aot.factories";

	public AotFactoriesProcessor() {
		super(AOT_LOCATION);
	}

	@Override
	protected Class<? extends Annotation> getSupportedAnnotation() {
		return SUPPORT_CLASS;
	}

}
