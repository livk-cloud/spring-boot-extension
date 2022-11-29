package com.livk.sso.commons;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * <p>
 * SSOAutoConfiguration
 * </p>
 *
 * @author livk
 * @date 2022/11/29
 */
@AutoConfiguration
@EnableConfigurationProperties(RsaKeyProperties.class)
public class SSOAutoConfiguration {
}
