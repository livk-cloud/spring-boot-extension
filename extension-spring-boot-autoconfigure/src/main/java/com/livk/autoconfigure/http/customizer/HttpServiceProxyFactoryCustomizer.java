package com.livk.autoconfigure.http.customizer;

import com.livk.commons.function.Customizer;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

/**
 * <p>
 * HttpServiceProxyFactoryCustomizer
 * </p>
 *
 * @author livk
 */
@FunctionalInterface
public interface HttpServiceProxyFactoryCustomizer extends Customizer<HttpServiceProxyFactory.Builder> {

}
