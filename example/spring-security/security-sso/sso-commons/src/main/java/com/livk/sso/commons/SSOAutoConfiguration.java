package com.livk.sso.commons;

import com.livk.auto.service.annotation.SpringAutoService;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * <p>
 * SSOAutoConfiguration
 * </p>
 *
 * @author livk
 */
@SpringAutoService
@AutoConfiguration
@EnableConfigurationProperties(RsaKeyProperties.class)
public class SSOAutoConfiguration {
}
