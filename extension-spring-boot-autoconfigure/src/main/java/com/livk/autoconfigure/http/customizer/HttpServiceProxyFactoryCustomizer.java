package com.livk.autoconfigure.http.customizer;

import com.livk.function.Customizer;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

/**
 * <p>
 * HttpServiceProxyFactoryCustomizer
 * </p>
 *
 * @author livk
 * @date 2022/11/30
 */
public interface HttpServiceProxyFactoryCustomizer extends Customizer<HttpServiceProxyFactory.Builder> {

}
