package com.livk.mapstruct;

import com.livk.mapstruct.converter.MapstructRegistry;
import com.livk.mapstruct.factory.MapstructFactory;
import com.livk.mapstruct.support.ConverterRepository;
import com.livk.mapstruct.support.GenericMapstructService;
import com.livk.mapstruct.support.InmemoryConverterRepository;
import org.springframework.beans.factory.ListableBeanFactory;
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
	public MapstructFactory mapstructFactory(MapstructRegistry registry, ListableBeanFactory beanFactory) {
		return new MapstructFactory(registry, beanFactory);
	}

	@Bean
	public GenericMapstructService genericMapstructService(ConverterRepository repository) {
		return new GenericMapstructService(repository);
	}

	@Bean
	public ConverterRepository converterRepository() {
		return new InmemoryConverterRepository();
	}

}
