package com.livk.http;

import com.livk.http.factory.HttpServiceRegistrar;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Import;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * <p>
 * HttpAutoConfiguration
 * </p>
 *
 * @author livk
 * @date 2022/5/20
 */
@Import(HttpServiceRegistrar.class)
@AutoConfiguration
@ConditionalOnClass(WebClient.class)
public class HttpAutoConfiguration {

}
