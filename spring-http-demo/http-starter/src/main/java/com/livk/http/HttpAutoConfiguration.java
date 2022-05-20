package com.livk.http;

import com.livk.http.factory.HttpServiceFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Import;

/**
 * <p>
 * HttpAutoConfiguration
 * </p>
 *
 * @author livk
 * @date 2022/5/20
 */
@Import(HttpServiceFactory.class)
@AutoConfiguration
public class HttpAutoConfiguration {

}
