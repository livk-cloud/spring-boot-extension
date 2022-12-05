package com.livk.example.config;

import org.mapstruct.MapperConfig;
import org.mapstruct.MappingConstants;
import org.mapstruct.extensions.spring.SpringMapperConfig;

/**
 * <p>
 * MapperSpringConfig
 * </p>
 *
 * @author livk
 */
@MapperConfig(componentModel = MappingConstants.ComponentModel.SPRING, uses = ConversionServiceAdapter.class)
@SpringMapperConfig
public interface MapperSpringConfig {
}
