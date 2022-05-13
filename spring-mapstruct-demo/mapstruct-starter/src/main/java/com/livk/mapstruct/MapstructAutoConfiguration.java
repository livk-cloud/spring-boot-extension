package com.livk.mapstruct;

import com.livk.mapstruct.commom.Converter;
import com.livk.mapstruct.support.ConverterRepository;
import com.livk.mapstruct.factory.MapstructFactoryRegister;
import com.livk.mapstruct.support.MapstructService;
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
	public MapstructFactoryRegister mapstructFactoryRegister(
			@SuppressWarnings("rawtypes") ConverterRepository<Converter> factory) {
		return new MapstructFactoryRegister(factory);
	}

	@Bean
	public MapstructService mapstructService() {
		return new MapstructService();
	}

}
