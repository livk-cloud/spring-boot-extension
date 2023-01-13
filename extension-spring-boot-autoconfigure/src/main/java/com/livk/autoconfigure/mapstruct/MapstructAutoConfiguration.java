package com.livk.autoconfigure.mapstruct;

import com.livk.auto.service.annotation.SpringAutoService;
import com.livk.autoconfigure.mapstruct.support.ConverterRepository;
import com.livk.autoconfigure.mapstruct.support.GenericMapstructService;
import com.livk.autoconfigure.mapstruct.support.InMemoryConverterRepository;
import org.mapstruct.factory.Mappers;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;

/**
 * <p>
 * MapstructAutoConfiguration
 * </p>
 *
 * @author livk
 */
@AutoConfiguration
@SpringAutoService
@ConditionalOnClass(Mappers.class)
public class MapstructAutoConfiguration {

    /**
     * Generic mapstruct service generic mapstruct service.
     *
     * @param repository the repository
     * @return the generic mapstruct service
     */
    @Bean
    public GenericMapstructService genericMapstructService(ConverterRepository repository) {
        return new GenericMapstructService(repository);
    }

    /**
     * Converter repository converter repository.
     *
     * @return the converter repository
     */
    @Bean
    public ConverterRepository converterRepository() {
        return new InMemoryConverterRepository();
    }

}
