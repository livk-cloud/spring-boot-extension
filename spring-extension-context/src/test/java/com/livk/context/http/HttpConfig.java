package com.livk.context.http;

import org.springframework.boot.test.context.TestConfiguration;

/**
 * @author livk
 */
@TestConfiguration
@HttpProviderScan(basePackageClasses = HttpConfig.class)
public class HttpConfig {

}
