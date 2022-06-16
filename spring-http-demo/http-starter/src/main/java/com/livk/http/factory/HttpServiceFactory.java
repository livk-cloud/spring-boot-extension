package com.livk.http.factory;

import com.google.common.base.CaseFormat;
import com.livk.http.annotation.BeanName;
import com.livk.util.SpringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.lang.NonNull;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * HttpFactory
 * </p>
 *
 * @author livk
 * @date 2022/5/20
 */
public class HttpServiceFactory implements BeanFactoryAware, ImportBeanDefinitionRegistrar, ResourceLoaderAware {

	private final HttpServiceProxyFactory proxyFactory;

	private BeanFactory beanFactory;

	private ResourceLoader resourceLoader;

	public HttpServiceFactory() {
		WebClient client = WebClient.builder().build();
		this.proxyFactory = HttpServiceProxyFactory.builder(new WebClientAdapter(client)).build();
	}

	@Override
	public void registerBeanDefinitions(@NonNull AnnotationMetadata importingClassMetadata,
			@NonNull BeanDefinitionRegistry registry) {
		List<String> packages = AutoConfigurationPackages.get(this.beanFactory);
		Set<Class<?>> typesAnnotatedClass = SpringUtils.findByAnnotationType(HttpExchange.class, resourceLoader,
				packages.toArray(String[]::new));
		for (Class<?> exchangeClass : typesAnnotatedClass) {
			BeanName name = AnnotationUtils.getAnnotation(exchangeClass, BeanName.class);
			String beanName = name != null ? name.value()
					: CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, exchangeClass.getSimpleName());
			registry.registerBeanDefinition(beanName, getBeanDefinition(exchangeClass));
		}
	}

	private <T> BeanDefinition getBeanDefinition(Class<T> exchangeClass) {
		return new RootBeanDefinition(exchangeClass, () -> proxyFactory.createClient(exchangeClass));
	}

	@Override
	public void setResourceLoader(@NonNull ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}

	@Override
	public void setBeanFactory(@NonNull BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}

}
