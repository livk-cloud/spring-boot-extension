package com.livk.example.config;

import org.mapstruct.MapperConfig;
import org.mapstruct.extensions.spring.SpringMapperConfig;

/**
 * <p>
 * MapperSpringConfig
 * </p>
 *
 * @author livk
 * @date 2022/6/27
 */
@MapperConfig(componentModel = "spring", uses = ConversionServiceAdapter.class)
@SpringMapperConfig
public interface MapperSpringConfig {
}
