package com.livk.excel.mvc.config;

import com.livk.excel.mvc.resolver.ExcelMethodArgumentResolver;
import com.livk.excel.mvc.resolver.ExcelMethodReturnValueHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * <p>
 * WebMvcConfig
 * </p>
 *
 * @author livk
 * @date 2022/1/17
 */
@Configuration(proxyBeanMethods = false)
public class WebMvcConfig implements WebMvcConfigurer {

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		resolvers.add(new ExcelMethodArgumentResolver());
	}

	@Override
	public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> handlers) {
		handlers.add(new ExcelMethodReturnValueHandler());
	}

}
