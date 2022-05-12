package com.livk.mapstruct;

import com.livk.mapstruct.commom.Converter;
import com.livk.mapstruct.factory.AbstractFactory;
import com.livk.mapstruct.factory.MapstructFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * <p>
 * MapstructAutoConfiguration
 * </p>
 *
 * @author livk
 * @date 2022/5/11
 */
@AutoConfiguration
public class MapstructAutoConfiguration {

	@Bean
	public MapstructFactory mapstructFactory() {
		return new MapstructFactory();
	}

	@Bean
	public MapstructService mapstructService(@SuppressWarnings("rawtypes") AbstractFactory<Converter> factory) {
		return new MapstructService(factory);
	}

}
