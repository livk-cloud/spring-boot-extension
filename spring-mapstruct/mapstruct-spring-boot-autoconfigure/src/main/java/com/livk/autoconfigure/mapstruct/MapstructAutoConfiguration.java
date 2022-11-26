package com.livk.autoconfigure.mapstruct;

import com.livk.autoconfigure.mapstruct.support.ConverterRepository;
import com.livk.autoconfigure.mapstruct.support.GenericMapstructService;
import com.livk.autoconfigure.mapstruct.support.InMemoryConverterRepository;
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
    public GenericMapstructService genericMapstructService(ConverterRepository repository) {
        return new GenericMapstructService(repository);
    }

    @Bean
    public ConverterRepository converterRepository() {
        return new InMemoryConverterRepository();
    }

}
