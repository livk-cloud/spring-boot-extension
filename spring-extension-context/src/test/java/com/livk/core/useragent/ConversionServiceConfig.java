package com.livk.core.useragent;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.support.GenericConversionService;

/**
 * <p>
 * ConversionServiceConfig
 * </p>
 *
 * @author livk
 */
@Configuration
class ConversionServiceConfig {

	@Bean
	public ConversionService conversionService(ObjectProvider<Converter<?, ?>> converters) {
		GenericConversionService conversionService = new GenericConversionService();
		converters.orderedStream().forEach(conversionService::addConverter);
		return conversionService;
	}

}
