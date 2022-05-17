package com.livk.mapstruct;

import com.livk.mapstruct.commom.Converter;
import com.livk.mapstruct.support.ConverterRepository;
import com.livk.mapstruct.factory.MapstructFactoryRegister;
import com.livk.mapstruct.support.InmemoryConverterRepository;
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
	public MapstructFactoryRegister mapstructFactoryRegister(ConverterRepository repository) {
		return new MapstructFactoryRegister(repository);
	}

	@Bean
	public MapstructService mapstructService(ConverterRepository repository) {
		return new MapstructService(repository);
	}

	@Bean
	public ConverterRepository converterRepository() {
		return new InmemoryConverterRepository();
	}

}
